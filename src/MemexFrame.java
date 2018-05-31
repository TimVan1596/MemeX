package src;

import src.com.picschooser.*;
import src.com.memexsql.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * @author TimVan
 */
public class MemexFrame extends JPanel implements ActionListener {

    /**
     * versionInfo 当前版本号
     * releaseDate  发布日期
     * */

    private static final String versionInfo = "v0.4";
    private static final String releaseDate = "2018年5月30日21:17:11";

    /**
     * textPane 用户输入的表情包文字
     * WORD_WIDTH 单个中文的字宽
     * WORD_HEIGHT 每行的行高
     * FIRST_ROW_Y  第一行文字的行高
     * openFile 用户选择的图片模板
     * imgLabel 图片
     */

    private static final int WORD_WIDTH = 39;
    private static final int WORD_HEIGHT = 45;
    private static final int IMG_HEIGHT = 400;
    private static final int IMG_WIDTH = 400;


    int scaleWidth = 0;
    int scaleHeight = 0;

    JTextPane textPane;
    File openFile;
    JLabel imgLabel;

    public MemexFrame() {
        super(new BorderLayout());





        /*图片模块*/
        JPanel imgPanel = new JPanel();
        imgPanel.setLayout(new BorderLayout());


        /*点击更换模板按钮*/
        JButton changePic = new JButton("更换模板");
        changePic.setFont(new Font("黑体", Font.BOLD, 18));
        changePic.setPreferredSize(new Dimension(10, 30));
        changePic.setActionCommand("changePic");
        changePic.addActionListener(this);

        /*展示图片*/
        imgLabel = new JLabel("");
        openFile = new File("熊猫人不屑.jpg");
        ImageIcon image = new ImageIcon("pics/" + openFile.getName());
        scaleWidth = (int)(IMG_WIDTH*1.2);;
        scaleHeight= IMG_HEIGHT;
        image=new ImageIcon(image.getImage().getScaledInstance(
                scaleWidth, scaleHeight, Image.SCALE_DEFAULT));

        imgLabel.setIcon(image);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
        imgLabel.setVerticalAlignment(JLabel.CENTER);


        imgLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                picsChooser();
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
        imgPanel.add(changePic, BorderLayout.NORTH);
        imgPanel.add(imgLabel, BorderLayout.SOUTH);
        /*end 展示图片*/

        /*文字编辑区*/
        JPanel textEditPanel = new JPanel();
        textEditPanel.setLayout(new BorderLayout());

        /*用户输入表情包文字*/
        textPane = new JTextPane();
        textPane.setFont(new Font("黑体", Font.BOLD, 19));
        textPane.setPreferredSize(new Dimension(50, 80));

        //设置字体（黑色，居中，16）
        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        StyleConstants.setAlignment(simpleAttributeSet, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontFamily(simpleAttributeSet, "lucida bright bold");
        StyleConstants.setFontSize(simpleAttributeSet, 22);
        //将字体属性给textPane
        StyledDocument doc = textPane.getStyledDocument();
        doc.setCharacterAttributes(105, doc.getLength()-105, simpleAttributeSet, false);
        doc.setParagraphAttributes(0, 104, simpleAttributeSet, false);
        //加上滚动条
        JScrollPane scrollPane = new JScrollPane(textPane);
        textEditPanel.add(scrollPane, BorderLayout.CENTER);

        /*生成按钮*/
        JButton submit = new JButton("生成表情包！");
        submit.setFont(new Font("黑体", Font.BOLD, 18));
        submit.setPreferredSize(new Dimension(50, 60));
        submit.setActionCommand("submit");
        submit.addActionListener(this);

        textEditPanel.add(submit, BorderLayout.SOUTH);


        this.add(imgPanel, BorderLayout.NORTH);
        this.add(textEditPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
//            生成表情包
            case "submit":
                summitCommend();
                break;
            case "changePic":
                //picsChooser();
                changePic();
                break;
            default:
        }

    }

    //实例输入：Width
    //我想卖可乐
    //你到底是要卖一辈子糖水还是要跟我一起改变世界
    //你到底是要卖一辈子糖水还是要跟我一起改变世界。不，我只想卖可口可乐
    /**
     *  创建GUI
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("MemeX - 表情包生成器 "+versionInfo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Create and set up the content pane.
        JComponent newContentPane = new MemexFrame();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        //使用Toolkit设置图标
        Toolkit tk=Toolkit.getDefaultToolkit();
        Image image=tk.createImage("icon.jpg");
        frame.setIconImage(image);



        /*
         * 创建一个菜单栏
         */
        JMenuBar menuBar = new JMenuBar();

        /*
         * 创建一级菜单
         */
        JMenu fileMenu = new JMenu("文件");
        JMenu editMenu = new JMenu("编辑");
        JMenu viewMenu = new JMenu("视图");
        //JMenu aboutMenu = new JMenu("关于");
        JMenuItem aboutMenu = new JMenuItem("关于");
        // 一级菜单添加到菜单栏
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(aboutMenu);
        aboutMenu.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        "版本号:"+versionInfo+"\n" +
                                "发布时间:"+releaseDate+"\n" +
                                "开发团队：我在芜湖玩Java\n",
                        "关于",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        frame.setJMenuBar(menuBar);



        //Display the window.
        frame.pack();
        frame.setVisible(true);
        //窗口在屏幕中间显示
        frame.setLocationRelativeTo(null);
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

    /**生成图片事件
     * */
    private void summitCommend(){
        String text = textPane.getText();
        String newMemePath = ImageProcess.drawImg(text,WORD_WIDTH,WORD_HEIGHT,openFile);
        //打开错误值
        final String WRONG_PATH = "0";
        if (!newMemePath.equals(WRONG_PATH)) {

            ImageIcon image = new ImageIcon(newMemePath);
            //图片等比缩放函数
            Map<String, Integer> imgWidthAndHeight = ImageProcess.scaleImage(image,IMG_WIDTH,IMG_HEIGHT);
            scaleWidth = imgWidthAndHeight.get("width");
            scaleHeight= imgWidthAndHeight.get("height");

            System.out.println("scaleWidth = "+scaleWidth);
            System.out.println("scaleHeight = "+scaleHeight);

            image=new ImageIcon(image.getImage().getScaledInstance(
                    scaleWidth, scaleHeight, Image.SCALE_DEFAULT));
            imgLabel.setIcon(image);                }

    }

    /**更换模板
     * */
    private void changePic(){

        JFileChooser fileChooser = new PictureChooser();

        //文件名过滤器
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "请选择jpg格式的表情包图片", "jpg");
        //给文件选择器加入文件过滤器
        fileChooser.setFileFilter(filter);

        fileChooser.showOpenDialog(null);
        openFile = fileChooser.getSelectedFile();
        if (openFile != null) {
            ImageIcon image = new ImageIcon(openFile.getAbsolutePath());
            //图片等比缩放函数
            Map<String, Integer> imgWidthAndHeight = ImageProcess.scaleImage(image,IMG_WIDTH,IMG_HEIGHT);
            scaleWidth = imgWidthAndHeight.get("width");
            scaleHeight= imgWidthAndHeight.get("height");

            image=new ImageIcon(image.getImage().getScaledInstance(
                    scaleWidth, scaleHeight, Image.SCALE_DEFAULT));
            imgLabel.setIcon(image);                }
    }

    private void picsChooser(){
        ImageChooserFrame imageChooserFramenew = new ImageChooserFrame();
        imageChooserFramenew.createGUI();
    }


}
