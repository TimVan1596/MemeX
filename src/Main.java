package src;


import javafx.stage.FileChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;


/**
 * @author TimVan
 */
public class Main extends JPanel implements ActionListener {

    /**
     * tf 用户输入的表情包文字
     * WORD_WIDTH 单个中文的字宽
     * WORD_HEIGHT 每行的行高
     * FIRST_ROW_Y  第一行文字的行高
     * openFile 用户选择的图片模板
     * img 图片
     */

    static final int WORD_WIDTH = 39;
    static final int WORD_HEIGHT = 45;
    JTextField tf;
    File openFile;
    JLabel img;


    public Main() {
        super(new BorderLayout());
        /*图片模块*/
        JPanel imgPanel = new JPanel();
        imgPanel.setLayout(new BorderLayout());


        /*点击更换模板按钮*/
        JButton changePic = new JButton("更换模板");
        changePic.setFont(new Font("黑体",Font.BOLD,18));
        changePic.setPreferredSize(new Dimension(10, 30));
        changePic.setActionCommand("changePic");
        changePic.addActionListener(this);

        /*展示图片*/
        img = new JLabel("");
        openFile = new File("pics/熊猫人不屑.jpg");
        img.setIcon(new ImageIcon("pics/"+openFile.getName()));
        img.setHorizontalAlignment(JLabel.CENTER);
        img.setVerticalAlignment(JLabel.CENTER);


        img.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")+"/pics"));
                //文件名过滤器
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "请选择jpg格式图片", "jpg");
                //给文件选择器加入文件过滤器
                fileChooser.setFileFilter(filter);

                fileChooser.showOpenDialog(null);
                openFile = fileChooser.getSelectedFile();
                if(openFile != null){
                    img.setIcon(new ImageIcon(openFile.getAbsolutePath()));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });



        //设置img标签的边距
        //TODO 需要用SpringLayout给imgPanel一个margin
        imgPanel.add(changePic,BorderLayout.NORTH);
        imgPanel.add(img,BorderLayout.SOUTH);
        /*end 展示图片*/

        /*文字编辑区*/
        JPanel textEditPanel = new JPanel();
        textEditPanel.setLayout(new BorderLayout());

        /*用户输入表情包文字*/
        tf = new JTextField(20);
        tf.setFont(new Font("黑体",Font.BOLD,19));
        tf.setPreferredSize(new Dimension(50, 60));
        tf.setHorizontalAlignment(JTextField.CENTER);
        textEditPanel.add(tf, BorderLayout.CENTER);
        /*生成按钮*/
        JButton submit = new JButton("生成表情包！");
        submit.setFont(new Font("黑体",Font.BOLD,18));
        submit.setPreferredSize(new Dimension(50, 60));
        submit.setActionCommand("submit");
        submit.addActionListener(this);

        textEditPanel.add(submit, BorderLayout.SOUTH);


        this.add(imgPanel, BorderLayout.NORTH);
        this.add(textEditPanel, BorderLayout.SOUTH);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String Command = e.getActionCommand();

        switch (Command) {
//            生成表情包
            case "submit":
                String text = tf.getText();
//                JOptionPane.showMessageDialog(null,"hello", "QUESTION_MESSAGE", JOptionPane.QUESTION_MESSAGE);

                exportImg(text);
                break;
            case "changePic":
                JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")+"/pics"));
                //文件名过滤器
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "请选择jpg格式图片", "jpg");
                //给文件选择器加入文件过滤器
                fileChooser.setFileFilter(filter);

                fileChooser.showOpenDialog(null);
                openFile = fileChooser.getSelectedFile();
                if(openFile != null){
                    img.setIcon(new ImageIcon(openFile.getAbsolutePath()));
                }
                break;
            default:
        }

    }

    //实例输入：
    //我想卖可乐
    //你到底是要卖一辈子糖水还是要跟我一起改变世界
    //你到底是要卖一辈子糖水还是要跟我一起改变世界。不，我只想卖可口可乐

    public void exportImg(String inputStr) {
        try {
            //读入表情包图片
            InputStream is = new FileInputStream(openFile.getAbsolutePath());
            System.out.println(openFile.getName());
            //读入图片
            BufferedImage buffImg = ImageIO.read(is);

            final int FIRST_ROW_Y = buffImg.getHeight()+WORD_HEIGHT-5;
            int imageWidth = buffImg.getWidth();
            //maxWordNums 每行最多多少字
            final int maxWordNums = imageWidth/WORD_WIDTH;
            //输入的字符总长度
            int strLenth = inputStr.length();
            //将会有几行
            int rowCount = strLenth / maxWordNums + 1;


            //bufferedImage = 创建新的BufferedImage，高度随文字的行数改变
            BufferedImage bufferedImage = new BufferedImage(buffImg.getWidth(),buffImg.getHeight()+rowCount*WORD_HEIGHT+15,BufferedImage.TYPE_INT_RGB);
            //将扩增的部分用矩形填充为白色（默认为黑色）
            bufferedImage.getGraphics().fillRect(0,0,bufferedImage.getWidth(),bufferedImage.getHeight());
            bufferedImage.getGraphics().setColor(Color.white);
            bufferedImage.getGraphics().dispose();

            bufferedImage.getGraphics().drawImage(buffImg,0,0,buffImg.getWidth(),buffImg.getHeight(),null);

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
                int finish = i!=rowCount?(i)*maxWordNums:inputStr.length();

                for(int j=(i-1)*maxWordNums ; j<finish; j++){
                    stringBuffer.append(inputStr.charAt(j));
                }
                everyRowStr=stringBuffer.toString();
                x = (imageWidth - WORD_WIDTH * (everyRowStr.length())) / 2;
                y = FIRST_ROW_Y + (i - 1) * WORD_HEIGHT;

                //绘制文字
                graphics.drawString(everyRowStr, x, y);
            }


            graphics.dispose();

            //获取桌面路径
            FileSystemView fsv = FileSystemView.getFileSystemView();
            //这便是读取桌面路径的方法了
            File com = fsv.getHomeDirectory();
            String path = com.getPath();

            OutputStream os = new FileOutputStream(path + "/新表情.jpg");
            //将输出流写入图片中
            ImageIO.write(bufferedImage, "jpg", os);

            is.close();
            os.close();

            //打开产生图片的文件夹
            Runtime.getRuntime().exec("cmd /c start explorer " + path);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block

            JOptionPane.showMessageDialog(null, "图片路径不存在", "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    //  创建GUI
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("MemeX - 表情包生成器 v0.2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new Main();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
