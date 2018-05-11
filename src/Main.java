package src;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class Main extends JPanel implements ActionListener {

    /*用户输入表情包文字*/
    JTextField tf;

    //单个中文的字宽
    static final  int WORD_WIDTH = 39;
    //


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
                default:
        }

    }

    public  void exportImg(String inputStr){
        try {
            //读入表情包图片
            InputStream is = new FileInputStream("meme1.jpg");
            //读入图片
            BufferedImage buffImg = ImageIO.read(is);
            //得到画笔对象
            Graphics g = buffImg.getGraphics();

            int imageWidth = buffImg.getWidth();
            System.out.println(imageWidth);
            int strLenth = inputStr.length();




            //最后一个参数用来设置字体的大小
            Font f = new Font("黑体",Font.BOLD,35);
            //new Color(0, 0, 255);
            Color mycolor = Color.BLACK;
            g.setColor(mycolor);
            g.setFont(f);


          //  while ()

            //10,20 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
            g.drawString(inputStr,(imageWidth- WORD_WIDTH *strLenth)/2,435);

            g.dispose();

            //获取桌面路径
            FileSystemView fsv = FileSystemView.getFileSystemView();
            //这便是读取桌面路径的方法了
            File com=fsv.getHomeDirectory();
            String path = com.getPath();

            OutputStream os = new FileOutputStream(path+"/新表情.jpg");
            //将输出流写入图片中
            ImageIO.write(buffImg, "jpg", os);

            is.close();
            os.close();



            Runtime.getRuntime().exec("cmd /c start explorer "+path);

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
