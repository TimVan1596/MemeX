package src.com.timvan.picschooser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author TimVan
 * ImageProcess 自用图像处理类
 */
public class ImageProcess {

    /**
     * 判断是linux系统还是其他系统
     * 如果是Linux系统，返回true，否则返回false
     */
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }


    /**
     * scaleImg()； 图像缩放方法,将图片缩放到适合控件的大小
     * ImageIcon image为显示的图片
     * int conWidth,int conHeight 分别是图片的长和宽
     */
    public static Map<String, Integer> scaleImage(ImageIcon image, double conWidth, double conHeight) {

        Map<String, Integer> imgWidthAndHeight = new HashMap<String, Integer>();

        //默认的边框间距
        final double SMALL_SCALE = 0.95;

        double imgWidth = image.getIconWidth();
        double imgHeight = image.getIconHeight();
        //原图的宽长比
        double imgRatio = imgWidth / imgHeight;
        //最终输出宽和长
        double reImgWidth = 0;
        double reImgHeight = 0;


        //若原图的宽小于控件宽
        if (imgWidth < conWidth) {
            if (imgHeight < conHeight) {
                reImgWidth = conWidth * SMALL_SCALE;
                reImgHeight = reImgWidth / imgRatio;
            } else {
                reImgHeight = conHeight * SMALL_SCALE;
                reImgWidth = reImgHeight * imgRatio;
            }
        }
        //若原图的宽大于控件宽
        else {
            if (imgHeight < conHeight) {
                reImgWidth = conWidth * SMALL_SCALE;
                reImgHeight = reImgWidth / imgRatio;
            }
            //若原图的长宽同时大于控件的长宽，最复杂的情况
            else {
                //控件的长比宽大
                double conRatio = conWidth / conHeight;

                if (imgRatio < conRatio) {
                    reImgHeight = conHeight * SMALL_SCALE;
                    reImgWidth = reImgHeight * imgRatio;
                } else {
                    reImgWidth = conWidth * SMALL_SCALE;
                    reImgHeight = reImgWidth / imgRatio;
                }
            }
        }


        imgWidthAndHeight.put("width", (int) reImgWidth);
        imgWidthAndHeight.put("height", (int) reImgHeight);

        return imgWidthAndHeight;
    }


    /***
     * 从URL地址读取图片流
     */
    public static BufferedImage getImageBufferStream(String destUrl) {

        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        int BUFFER_SIZE = 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        try {
            url = new URL(destUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());

        } catch (IOException e) {
        } catch (ClassCastException e) {
        } finally {
            try {
                //读入图片
                BufferedImage buffImg = ImageIO.read(bis);
                bis.close();
                httpUrl.disconnect();
                return buffImg;
            } catch (IOException e) {
            } catch (NullPointerException e) {
            }
        }
        return null;

    }

    //输入URL地址，返回保存图片的路径
    public static String saveToFile(String destUrl) {
        String address = "cache.jpg";
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        int BUFFER_SIZE = 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        try {
            url = new URL(destUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            fos = new FileOutputStream(address);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.flush();
        } catch (IOException e) {
        } catch (ClassCastException e) {
        } finally {
            try {
                fos.close();
                bis.close();
                httpUrl.disconnect();
            } catch (IOException e) {
            } catch (NullPointerException e) {
            }
        }
        return address;
    }

    //复制图片到剪切板
    public static void CopyToClipboard(final Image image) {
        Transferable trans = new Transferable(){
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[] { DataFlavor.imageFlavor };
            }
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.imageFlavor.equals(flavor);
            }
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                if(isDataFlavorSupported(flavor))
                    return image;
                throw new UnsupportedFlavorException(flavor);
            }

        };
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
    }

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    /** 将传入的字符串重绘图像，并输出图片文件
     * 返回图片文件的路径，返回“0”为错误
     * */
    public static String drawImg(String inputStr, int WORD_WIDTH, int WORD_HEIGHT, File openFile) {
        String newMemePath = "0";
        try {
            //读入表情包图片
            InputStream is = new FileInputStream(openFile.getAbsolutePath());
            System.out.println(openFile.getName());
            //读入图片
            BufferedImage buffImg = ImageIO.read(is);

            final int FIRST_ROW_Y = buffImg.getHeight() + WORD_HEIGHT - 5;
            int imageWidth = buffImg.getWidth();
            //maxWordNums 每行最多多少字
            final int maxWordNums = imageWidth / WORD_WIDTH;
            //输入的字符总长度
            int strLenth = inputStr.length();
            //将会有几行
            int rowCount = strLenth / maxWordNums + 1;


            //bufferedImage = 创建新的BufferedImage，高度随文字的行数改变
            BufferedImage bufferedImage = new BufferedImage(buffImg.getWidth(), buffImg.getHeight() + rowCount * WORD_HEIGHT + 15, BufferedImage.TYPE_INT_RGB);
            //将扩增的部分用矩形填充为白色（默认为黑色）
            bufferedImage.getGraphics().fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
            bufferedImage.getGraphics().setColor(Color.white);
            bufferedImage.getGraphics().dispose();

            bufferedImage.getGraphics().drawImage(buffImg, 0, 0, buffImg.getWidth(), buffImg.getHeight(), null);

            //得到画笔对象
            Graphics graphics = bufferedImage.getGraphics();


            //最后一个参数用来设置字体的大小
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font("黑体", Font.BOLD, 35));


            for (int i = 1; i <= rowCount; ++i) {
                /**
                 *  xy为坐标
                 *  everyRowStr为每一行的字符串
                 *  stringBuffer作为缓存输入，把总字符串切割
                 *  finish作为每行切割的尾序号
                 * */
                int x = 0, y = 0;
                String everyRowStr = "";
                StringBuffer stringBuffer = new StringBuffer();
                //判断是否是最后一行
                int finish = i != rowCount ? (i) * maxWordNums : inputStr.length();

                for (int j = (i - 1) * maxWordNums; j < finish; j++) {
                    stringBuffer.append(inputStr.charAt(j));
                }
                everyRowStr = stringBuffer.toString();
                x = (imageWidth - WORD_WIDTH * (everyRowStr.length())) / 2;
                y = FIRST_ROW_Y + (i - 1) * WORD_HEIGHT;

                //绘制文字
                graphics.drawString(everyRowStr, x, y);
            }


            graphics.dispose();

            //获取桌面路径
            FileSystemView fsv = FileSystemView.getFileSystemView();
            //读取桌面路径
            File com = fsv.getHomeDirectory();
            String path = com.getPath();
            //更改最终返回的表情路径

            File dirFile = new File(path + "/MemeX表情包");
            //无则创建
            boolean bFile = dirFile.exists();
            if (bFile == false) {
                dirFile.mkdir();
            }


            newMemePath = path + "/MemeX表情包/新表情.jpg";

            OutputStream os = new FileOutputStream(newMemePath);
            //将输出流写入图片中
            ImageIO.write(bufferedImage, "jpg", os);
            //将图片复制到剪切板
            CopyToClipboard(bufferedImage);

            is.close();
            os.close();

            //打开产生图片的文件夹，判断是否是Linux
            if (isLinux() == true) {
                Runtime.getRuntime().exec("sh nautilus "
                        + path + "/MemeX表情包");
            } else {
                System.out.println(path);
                Runtime.getRuntime().exec("cmd /c start explorer "
                        + path + "\\MemeX表情包");
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block

            JOptionPane.showMessageDialog(null, "图片路径不存在", "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "图片路径不存在", "错误", JOptionPane.ERROR_MESSAGE);
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            return newMemePath;
        }

    }

}
