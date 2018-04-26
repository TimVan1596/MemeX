package src;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class Main {
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("MemeX - 表情包生成器");
        frame.setLayout(new BorderLayout());
//        总体使用BorderLayout，North是JTextField，
//        Center是用JPanel，它里面是按钮，其layout是GridLayout。


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
        JTextField tf = new JTextField(20);
        tf.setPreferredSize(new Dimension(50,60));
        tf.setHorizontalAlignment(JTextField.CENTER);
        TextEditPanel.add(tf,BorderLayout.CENTER);
        /*生成按钮*/
        JButton summit = new JButton("生成表情包！");
        summit.setPreferredSize(new Dimension(50,60));
        TextEditPanel.add(summit,BorderLayout.SOUTH);


        summit.addMouseListener(new MouseListener() {


            public void mouseClicked(MouseEvent e) {
                //一个弹框，此处不细说其语法
                JOptionPane.showMessageDialog(null,"内部类事件监听监听","注意",0,null);
            }
        });



        frame.add(imgPanel, BorderLayout.NORTH);
        frame.add(TextEditPanel, BorderLayout.SOUTH);
        /*end文字编辑区*/

        frame.pack();
        //frame.setResizable(false);  加上即不能调整窗口大小
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // 显示应用 GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}