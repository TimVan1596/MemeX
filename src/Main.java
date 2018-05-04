package src;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class Main extends JPanel implements ActionListener {

    /*用户输入表情包文字*/
    JTextField tf;

//  单个中文的字宽
    static final  int word_width = 39;


    public Main() {
        super(new BorderLayout());
        /*展示图片*/
        JPanel imgPanel = new JPanel();
        JLabel img = new JLabel("");
        img.setIcon(new ImageIcon("meme1.jpg"));
        //设置img标签的边距
        //TODO 需要用SpringLayout给imgpanel一个margin
        imgPanel.add(img);
        imgPanel.setLayout(new FlowLayout());
        /*end 展示图片*/

        /*文字编辑区*/
        JPanel TextEditPanel = new JPanel();
        TextEditPanel.setLayout(new BorderLayout());

        /*用户输入表情包文字*/
        tf = new JTextField(20);
        tf.setPreferredSize(new Dimension(50,60));
        tf.setHorizontalAlignment(JTextField.CENTER);
        TextEditPanel.add(tf,BorderLayout.CENTER);
        /*生成按钮*/
        JButton summit = new JButton("生成表情包！");
        summit.setPreferredSize(new Dimension(50,60));
        summit.setActionCommand("submit");
        summit.addActionListener(this);

        TextEditPanel.add(summit,BorderLayout.SOUTH);


        this.add(imgPanel,BorderLayout.NORTH);
        this.add(TextEditPanel,BorderLayout.SOUTH);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String Command = e.getActionCommand();

        switch (Command){
//            生成表情包
            case "submit":
                String text = tf.getText();
//                JOptionPane.showMessageDialog(null,text, "QUESTION_MESSAGE", JOptionPane.QUESTION_MESSAGE);
                exportImg(text);
                break;
        }

    }

    public  void exportImg(String inputStr){
        try {
            //1.jpg是你的 主图片的路径
            InputStream is = new FileInputStream("meme1.jpg");

            //通过JPEG图象流创建JPEG数据流解码器
            JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(is);
            //解码当前JPEG数据流，返回BufferedImage对象
            BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();
            //得到画笔对象
            Graphics g = buffImg.getGraphics();

            int imageWidth = buffImg.getWidth();
            System.out.println(imageWidth);
            int StrLenth = inputStr.length();




            //最后一个参数用来设置字体的大小
            Font f = new Font("黑体",Font.BOLD,35);
            Color mycolor = Color.BLACK;//new Color(0, 0, 255);
            g.setColor(mycolor);
            g.setFont(f);

            //10,20 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
            g.drawString(inputStr,(imageWidth-word_width*StrLenth)/2,435);

            g.dispose();

            //获取桌面路径
            FileSystemView fsv = FileSystemView.getFileSystemView();
            File com=fsv.getHomeDirectory();    //这便是读取桌面路径的方法了
            String path = com.getPath();

            OutputStream os;
            os = new FileOutputStream(path+"/新表情.jpg");
            //创键编码器，用于编码内存中的图象数据。
            JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
            en.encode(buffImg);

            is.close();
            os.close();

        //    String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
         //   String path ="C:\\Users\\123\\IdeaProjects\\expression_package_generator\\union.jpg";

            Runtime.getRuntime().exec("cmd /c start explorer "+path);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block

            JOptionPane.showMessageDialog(null, "图片路径不存在", "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (ImageFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    //  创建GUI
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("MemeX - 表情包生成器");
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
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
