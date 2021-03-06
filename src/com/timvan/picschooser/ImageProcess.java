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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static Map<String, Integer> scaleImage(
            ImageIcon image,
            double conWidth,
            double conHeight ) {

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
            if (imgHeight < conHeight && imgWidth >= imgHeight) {
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
            e.printStackTrace();
        } finally {
            try {
                //读入图片

                BufferedImage buffImg = null;
                if (bis == null) {

                } else {
                    buffImg = ImageIO.read(bis);

                }


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

    /**复制图片到剪切板
     * */
    public static void CopyToClipboard(final Image image) {
        Transferable trans = new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{DataFlavor.imageFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.imageFlavor.equals(flavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                if (isDataFlavorSupported(flavor)) {
                    return image;
                }
                throw new UnsupportedFlavorException(flavor);
            }

        };
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
    }

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    /** 将传入的字符串重绘图像，并输出图片文件
     * 返回图片文件的路径，返回“0”为错误
     * int Y ;输入字的垂直位置，从上到下0~100
     * Color fontColor : 输出的字体颜色
     *  int fontSize : 字体大小
     * */

    /**
     * DEFAULT_FONT_SIZE ： 默认字体大小
     * WORD_WIDTH : 默认字体大小下文字的宽
     * WORD_HEIGHT : 默认字体大小下文字的高
     * */
    private static final int DEFAULT_FONT_SIZE = 35;
    private static final int WORD_WIDTH = 39;
    private static final int WORD_HEIGHT = 45;

    public static String drawImg(String inputStr,
                                 int Y,
                                 int fontSize,
                                 Color fontColor,
                                 File openFile) {
        //输入字的垂直位置的最大值
        final int MAX_Y = 100;
        //改变传入字的大小
        int WORDWIDTH  = (int)(WORD_WIDTH*((double)fontSize/DEFAULT_FONT_SIZE));
        int WORDHEIGHT = (int)(WORD_HEIGHT*((double)fontSize/DEFAULT_FONT_SIZE));

        String newMemePath = "0";
        try {
            //读入表情包图片
            InputStream is = new FileInputStream(openFile.getAbsolutePath());
            //System.out.println(openFile.getName());
            //读入图片
            BufferedImage buffImg = ImageIO.read(is);

            int FIRST_ROW_Y = buffImg.getHeight();
            //改变第一行的位置
            double scaleRateY = (double)Y/MAX_Y;
            FIRST_ROW_Y = (int)(FIRST_ROW_Y*scaleRateY);
            FIRST_ROW_Y += WORDHEIGHT - 5;

            System.out.println("总高度："+buffImg.getHeight());
            System.out.println("第一行高度："+FIRST_ROW_Y);

            int imageWidth = buffImg.getWidth();
            //maxWordNums 每行最多多少字
            final int maxWordNums = imageWidth / WORDWIDTH;
            //输入的字符总长度
            int strLenth = inputStr.length();
            //将会有几行
            int rowCount = strLenth / maxWordNums + 1;
            //最后保存的表情的名称
            String picTitle = "";


            //bufferedImage = 创建新的BufferedImage，高度随文字的行数改变
            int newPicHeight = FIRST_ROW_Y+ rowCount * WORDHEIGHT-15;
            if(newPicHeight <= buffImg.getHeight()){
                //若第一行的位置比原图低，保持图片不变
                newPicHeight = buffImg.getHeight();
            }
            BufferedImage bufferedImage = new BufferedImage(buffImg.getWidth(),
                    newPicHeight, BufferedImage.TYPE_INT_RGB);
            //将扩增的部分用矩形填充为白色（默认为黑色）
            bufferedImage.getGraphics().fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
            bufferedImage.getGraphics().setColor(Color.white);
            bufferedImage.getGraphics().dispose();

            bufferedImage.getGraphics().drawImage(buffImg, 0, 0, buffImg.getWidth(), buffImg.getHeight(), null);

            //得到画笔对象
            Graphics graphics = bufferedImage.getGraphics();


            //最后一个参数用来设置字体的大小
            graphics.setColor(fontColor);
            graphics.setFont(new Font("黑体", Font.BOLD, fontSize));


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
                    char cacheChar = inputStr.charAt(j);
                    stringBuffer.append(cacheChar);
                }
                everyRowStr = stringBuffer.toString();
                x = (imageWidth - WORDWIDTH * (everyRowStr.length())) / 2;
                y = FIRST_ROW_Y + (i - 1) * WORDHEIGHT;

                if (i == 1) {
                    //将第一行作为标题
                    picTitle = stringBuffer.toString();
                    picTitle = trimStringFromAbnormal(picTitle);
                }

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
            if (!bFile) {
                dirFile.mkdir();
            }


            newMemePath = path + "/MemeX表情包/"+picTitle+".jpg";
            OutputStream os = new FileOutputStream(newMemePath);
            //将输出流写入图片中
            ImageIO.write(bufferedImage, "jpg", os);
            //将图片复制到剪切板
            CopyToClipboard(bufferedImage);

            is.close();
            os.close();

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

    /** 去除字符中的特殊字符（如符号等）
     * */
    public static String trimStringFromAbnormal(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

}
