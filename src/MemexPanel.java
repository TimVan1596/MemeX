package src;

import src.com.timvan.picschooser.*;
import src.com.timvan.memexsql.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * @author TimVan
 */
public class MemexPanel extends JPanel
        implements ActionListener {

    /**
     * versionInfo 当前版本号
     * releaseDate  发布日期
     */
    private static final String versionInfo = "v0.4";
    private static final String releaseDate =
            "2018年6月3日21:49:55";


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

    //缩放图片的尺寸
    int scaleWidth = 0;
    int scaleHeight = 0;


    private JTextPane textPane;
    private File openFile;
    private JLabel imgLabel;
    //图片上的右键菜单
    private JPopupMenu imgPopMenu;
    //状态栏
    private JLabel statusBar;
    //状态栏显示信息
    private String statusBarString;
    //图片右键菜单
    private JMenuItem mCopy, mChange, mStore;
    //创建一个菜单栏
    protected JMenuBar menuBar;


    private void changePicByUrl(String url) {

//        JOptionPane.showMessageDialog(null,
//                "fuck", url, 0);

        String address = ImageProcess.saveToFile(url);

        openFile = new File(address);
        ImageIcon image = new ImageIcon(openFile.getAbsolutePath());
        //图片等比缩放函数
        Map<String, Integer> imgWidthAndHeight = ImageProcess.scaleImage(image, IMG_WIDTH, IMG_HEIGHT);
        scaleWidth = imgWidthAndHeight.get("width");
        scaleHeight = imgWidthAndHeight.get("height");

        image = new ImageIcon(image.getImage().getScaledInstance(
                scaleWidth, scaleHeight, Image.SCALE_DEFAULT));
        imgLabel.setIcon(image);
    }

     public MemexPanel(JFrame frame) {
        super(new BorderLayout());


        /*图片模块*/
        JPanel imgPanel = new JPanel();
        imgPanel.setLayout(new BorderLayout());

        /* 操作面板（包括MenuBar和点击进入商店按钮） */
        JPanel operatePanel = new JPanel(new BorderLayout());
        /*MenuBar 菜单栏*/
        //创建一个菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu settingMenu = new JMenu("设置");
        //菜单栏中的“编辑”一级菜单
        JMenu editMenu = new JMenu("编辑");

        // 一级菜单添加到菜单栏
        menuBar.add(editMenu);
        menuBar.add(settingMenu);


        JMenuItem helpMenu = new JMenuItem("帮助教程");
        helpMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String filePath = System.getProperty("java.class.path");
                //得到当前操作系统的分隔符，windows下是";",linux下是":"
                String pathSplit = System.getProperty("path.separator");
                /**
                 * 若没有其他依赖，则filePath的结果应当是该可运行jar包的绝对路径，
                 * 此时我们只需要经过字符串解析，便可得到jar所在目录
                 */
                if(filePath.contains(pathSplit)){
                    filePath = filePath.substring(0,filePath.indexOf(pathSplit));
                }else if (filePath.endsWith(".jar")) {//截取路径中的jar包名,可执行jar包运行的结果里包含".jar"
                    filePath = filePath.substring(0, filePath.lastIndexOf(File.separator) + 1);
                }

                String path =filePath+"/使用教程.png";

                try {
                    //打开“帮助教程”图片
                    if (ImageProcess.isLinux()) {
                        Runtime.getRuntime().exec("sh nautilus "
                                + path);
                    } else {
                        System.out.println(path);
                        Runtime.getRuntime().exec("cmd /c "+path);
                    }
                }
                catch (IOException ie){
                    ie.printStackTrace();
                }

            }
        });
        JMenuItem aboutMenu = new JMenuItem("关于");
        aboutMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        "版本号:" + versionInfo + "\n" +
                                "发布时间:" + releaseDate + "\n" +
                                "开发团队：我在芜湖玩Java\n",
                        "关于", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        settingMenu.add(helpMenu);
        settingMenu.add(aboutMenu);



        /*点击表情包商店*/
        JButton storeBTN = new JButton("表情包商店");
        storeBTN.setFont(new Font("黑体", Font.BOLD, 18));
        storeBTN.setPreferredSize(new Dimension(10, 30));
        storeBTN.setActionCommand("storeBTN");
        storeBTN.addActionListener(this);

        /*展示图片*/
        imgLabel = new JLabel("");
        openFile = new File("熊猫人不屑.jpg");
        ImageIcon image = new ImageIcon("pics/" + openFile.getName());
        scaleWidth = (int) (IMG_WIDTH * 1.2);
        scaleHeight = IMG_HEIGHT;
        image = new ImageIcon(image.getImage().getScaledInstance(
                scaleWidth, scaleHeight, Image.SCALE_DEFAULT));

        imgLabel.setIcon(image);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
        imgLabel.setVerticalAlignment(JLabel.CENTER);


        imgPopMenu = new JPopupMenu();

        mCopy = new JMenuItem("复制到剪切板");
        imgPopMenu.add(mCopy);
        mCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon icon = (ImageIcon) imgLabel.getIcon();
                ImageProcess.CopyToClipboard(icon.getImage());
            }
        });

        mChange = new JMenuItem("更换图片");
        imgPopMenu.add(mChange);
        mChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePic();
            }
        });

        mStore = new JMenuItem("打开表情包商店");
        mStore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //切换状态栏内容
                statusBarString = "加载需要一点时间，优化成这样你还想让我怎样";
                statusBar.setText(statusBarString);
                picsChooser();
            }
        });
        imgPopMenu.add(mStore);

//        editMenu.add(mCopy);
//        editMenu.add(mStore);
//        editMenu.add(mChange);
        editMenu.add(mCopy);
        editMenu.add(mChange);
        editMenu.add(mStore);
        frame.setJMenuBar(menuBar);


        imgLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //picsChooser();
                if (e.isPopupTrigger()) {
                    imgPopMenu.show(imgLabel, e.getX(), e.getY());
                }
                else {
                    JOptionPane.showMessageDialog(null,
                            "ada","sad",JOptionPane.WARNING_MESSAGE);
                    changePic();

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
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
        imgPanel.add(storeBTN, BorderLayout.NORTH);
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
        doc.setCharacterAttributes(105, doc.getLength() - 105, simpleAttributeSet, false);
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

        //状态栏
        statusBarString = "点击中间表情包可切换本地图片，右键有惊喜";
        statusBar = new JLabel(statusBarString);
        statusBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        //完成面板，textEditPanel的子Panel
        JPanel finishPanel = new JPanel(new BorderLayout());
        finishPanel.add(submit, BorderLayout.CENTER);
        finishPanel.add(statusBar, BorderLayout.SOUTH);

        textEditPanel.add(finishPanel, BorderLayout.SOUTH);

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
                //切换状态栏内容
                statusBarString = "表情包已复制到剪切板，您可以直接粘贴；新图片保存在桌面";
                statusBar.setText(statusBarString);
                break;
            case "storeBTN":
                //打开表情包商店
                //切换状态栏内容
                statusBarString = "加载需要一点时间，优化成这样你还想让我怎样";
                statusBar.setText(statusBarString);
                picsChooser();
                //changePic();
                break;
            default:
        }

    }

    //实例输入：Width
    //我想卖可乐
    //你到底是要卖一辈子糖水还是要跟我一起改变世界
    //你到底是要卖一辈子糖水还是要跟我一起改变世界。不，我只想卖可口可乐

//    /**
//     * 创建GUI
//     */
//    private  void createAndShowGUI() {
//        //Create and set up the window.
//        JFrame frame = new JFrame("MemeX - 斗图神器 " + versionInfo);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//
//        //Create and set up the content pane.
//        JComponent newContentPane = new MemexPanel();
//        newContentPane.setOpaque(true);
//        frame.setContentPane(newContentPane);
//
//        //使用Toolkit设置图标
//        Toolkit tk = Toolkit.getDefaultToolkit();
//        Image image = tk.createImage("icon.jpg");
//        frame.setIconImage(image);
//
//
//
//        /*MenuBar 菜单栏*/
//        //创建一个菜单栏
//        menuBar = new JMenuBar();
//        JMenu settingMenu = new JMenu("设置");
//        //菜单栏中的“编辑”一级菜单
//        JMenu editMenu = new JMenu("编辑");
//
//        // 一级菜单添加到菜单栏
//        menuBar.add(editMenu);
//        menuBar.add(settingMenu);
//
//
//        JMenuItem helpMenu = new JMenuItem("帮助教程");
//        helpMenu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                String filePath = System.getProperty("java.class.path");
//                //得到当前操作系统的分隔符，windows下是";",linux下是":"
//                String pathSplit = System.getProperty("path.separator");
//                /**
//                 * 若没有其他依赖，则filePath的结果应当是该可运行jar包的绝对路径，
//                 * 此时我们只需要经过字符串解析，便可得到jar所在目录
//                 */
//                if(filePath.contains(pathSplit)){
//                    filePath = filePath.substring(0,filePath.indexOf(pathSplit));
//                }else if (filePath.endsWith(".jar")) {//截取路径中的jar包名,可执行jar包运行的结果里包含".jar"
//                    filePath = filePath.substring(0, filePath.lastIndexOf(File.separator) + 1);
//                }
//
//                String path =filePath+"/使用教程.png";
//
//                try {
//                    //打开“帮助教程”图片
//                    if (ImageProcess.isLinux()) {
//                        Runtime.getRuntime().exec("sh nautilus "
//                                + path);
//                    } else {
//                        System.out.println(path);
//                        Runtime.getRuntime().exec("cmd /c "+path);
//                    }
//                }
//                catch (IOException ie){
//                    ie.printStackTrace();
//                }
//
//            }
//        });
//        JMenuItem aboutMenu = new JMenuItem("关于");
//        aboutMenu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(frame,
//                        "版本号:" + versionInfo + "\n" +
//                                "发布时间:" + releaseDate + "\n" +
//                                "开发团队：我在芜湖玩Java\n",
//                        "关于", JOptionPane.INFORMATION_MESSAGE);
//            }
//        });
//
//        settingMenu.add(helpMenu);
//        settingMenu.add(aboutMenu);
//
//        frame.setJMenuBar(menuBar);
//
//
//        //Display the window.
//        frame.pack();
//        frame.setVisible(true);
//        //窗口在屏幕中间显示
//        frame.setLocationRelativeTo(null);
//    }
//

    /**
     * 生成图片事件
     */
    private void summitCommend() {
        String text = textPane.getText();
        String newMemePath = ImageProcess.drawImg(text, WORD_WIDTH, WORD_HEIGHT, openFile);
        //打开错误值
        final String WRONG_PATH = "0";
        if (!newMemePath.equals(WRONG_PATH)) {

            ImageIcon image = new ImageIcon(newMemePath);
            //图片等比缩放函数
            Map<String, Integer> imgWidthAndHeight = ImageProcess.scaleImage(image, IMG_WIDTH, IMG_HEIGHT);
            scaleWidth = imgWidthAndHeight.get("width");
            scaleHeight = imgWidthAndHeight.get("height");

            System.out.println("scaleWidth = " + scaleWidth);
            System.out.println("scaleHeight = " + scaleHeight);

            image = new ImageIcon(image.getImage().getScaledInstance(
                    scaleWidth, scaleHeight, Image.SCALE_DEFAULT));
            imgLabel.setIcon(image);
        }

    }

    /**
     * 更换模板
     */
    private void changePic() {

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
            Map<String, Integer> imgWidthAndHeight = ImageProcess.scaleImage(image, IMG_WIDTH, IMG_HEIGHT);
            scaleWidth = imgWidthAndHeight.get("width");
            scaleHeight = imgWidthAndHeight.get("height");

            image = new ImageIcon(image.getImage().getScaledInstance(
                    scaleWidth, scaleHeight, Image.SCALE_DEFAULT));
            imgLabel.setIcon(image);
        }
    }

    //打开表情模板选择器
    private void picsChooser() {


///使用多线程改变状态栏消息，失败
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //切换状态栏内容
//                statusBarString = "加载需要一点时间，优化成这样你还想让我怎样";
//                statusBar.setText(statusBarString);
//            }
//        });
//
//        try{
//            thread.join();
//
//        }
//        catch (InterruptedException ie){
//            ie.printStackTrace();
//        }


         JFrame frame = new JFrame("请选择一个表情包图片");

        //实例化由IDEA UIDesign 的Form文件
        ImageChooser imageChooser = new ImageChooser();
        frame.setContentPane(imageChooser.panel);

        Container container = frame.getContentPane();
        //在通过getMemesJTable()方法传入图片表格，并将JTable套入JScrollPane
        JTable imageTable = new InfoMysql().getMemesJTable(imageChooser);
        JScrollPane jScrollPane = new JScrollPane(imageTable);
        container.add(jScrollPane);


        //添加关闭事件
        imageChooser.okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int row = imageTable.getSelectedRow();
                //增加热度
                int id = (int) imageTable.getValueAt(row, 0);
                JDBCUtil.updatePicsTimes(id);

                //获取点击中的URL
                int column = 2;
                String URL = (String) imageTable.getValueAt(row, column);
                changePicByUrl(URL);
                frame.dispose();
            }
        });

        frame.setSize(new Dimension(800, 500));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }


}
