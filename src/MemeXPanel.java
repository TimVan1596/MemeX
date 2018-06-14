package src;

import src.com.timvan.memexsql.JDBCUtil;
import src.com.timvan.picschooser.*;
import src.com.timvan.memexsql.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import static src.com.timvan.memexutil.MemexConstants.RealeseInfos.*;
import static src.com.timvan.memexutil.MemexConstants.StatusBarString.FINISH_STATUS_BAR_STRING;
import static src.com.timvan.memexutil.MemexConstants.StatusBarString.INIT_STATUS_BAR_STRING;
import static src.com.timvan.memexutil.MemexConstants.StatusBarString.WAIT_FOR_STORE_STRING;
import static src.com.timvan.picschooser.ImageProcess.isLinux;

/**
 * 作为主界面MemeX的主面板方法，各控件在此绘制
 * @author TimVan
 */
public class MemeXPanel extends JPanel
        implements ActionListener {

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
    private int scaleWidth = 0;
    private int scaleHeight = 0;


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
    /**
     * verticalSlider:文字位置垂直滑动条
     * */
    private JSlider verticalSlider;


    private void changePicByUrl(String url) {

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

    public MemeXPanel(JFrame frame) {
        super(new BorderLayout());


        /*图片模块*/
        JPanel imgPanel = new JPanel();
        imgPanel.setLayout(new BorderLayout());

        /*MenuBar 菜单栏*/
        //创建一个菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu settingMenu = new JMenu("      设 置      ");
        //菜单栏中的“编辑”一级菜单
        JMenu editMenu = new JMenu("      编 辑      ");

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
                }else if (filePath.endsWith(".jar")) {
                    //截取路径中的jar包名,可执行jar包运行的结果里包含".jar"
                    filePath = filePath.substring(0, filePath.lastIndexOf(File.separator) + 1);
                }

                String path =filePath+"/使用教程.png";

                try {
                    //打开“帮助教程”图片
                    if (isLinux()) {
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
                        "                              "
                                +SOFT_NAME_EN+
                                " - " + SOFT_NAME_CN + "\n"
                                + "版本号: "
                                + versionInfo + "\n" +
                                "发布时间: "
                                + releaseDate + "\n" +
                                "开发团队：我在芜湖玩Java\n\n"
                        +"本页面的软件遵照GPL-3.0协议开放源代码\n"
                        +"GitHub 地址 ：https://github.com/TimVan1596/MemeX\n",
                        "关于", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        settingMenu.add(helpMenu);
        settingMenu.add(aboutMenu);

        /*点击表情包商店*/
        JButton storeBTN = new JButton("打开表情包商店");
        // 设置前景/字体颜色
        storeBTN.setForeground(Color.white);
        //设置背景颜色
        storeBTN.setBackground(new Color(86, 119, 252));
        storeBTN.setFont(new Font("黑体", Font.BOLD, 19));
        storeBTN.setPreferredSize(new Dimension(10, 35));
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
                statusBar.setText(WAIT_FOR_STORE_STRING);
                picsChooser();
            }
        });
        imgPopMenu.add(mStore);


        JMenuItem mShowInExplore = new JMenuItem("在文件夹中打开");
        mShowInExplore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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


                try{
                    //打开产生图片的文件夹，判断是否是Linux
                    if (isLinux()) {
                        Runtime.getRuntime().exec("sh nautilus "
                                + path + "/MemeX表情包");
                    } else {
                        System.out.println(path);
                        Runtime.getRuntime().exec("cmd /c start explorer "
                                + path + "\\MemeX表情包");
                    }

                }
                catch (IOException ie){
                    ie.printStackTrace();
                }



            }
        });
        imgPopMenu.add(mShowInExplore);


        JMenuItem editMenuCopy = new JMenuItem("复制到剪切板");
        editMenuCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon icon = (ImageIcon) imgLabel.getIcon();
                ImageProcess.CopyToClipboard(icon.getImage());
            }
        });

        JMenuItem editMenuChange = new JMenuItem("更换图片");
        editMenuChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePic();
            }
        });

        JMenuItem editMenuStore = new JMenuItem("打开表情包商店");
        editMenuStore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //切换状态栏内容
                statusBar.setText(WAIT_FOR_STORE_STRING);
                picsChooser();
            }
        });

        JMenuItem editMenuShowInExplore = new JMenuItem("打开表情包所在文件夹");
        editMenuShowInExplore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //获取桌面路径
                FileSystemView fsv = FileSystemView.getFileSystemView();
                //读取桌面路径
                File com = fsv.getHomeDirectory();
                String path = com.getPath();
                //更改最终返回的表情路径
                try{
                    //打开产生图片的文件夹，判断是否是Linux
                    if (isLinux()) {
                        Runtime.getRuntime().exec("sh nautilus "
                                + path + "/MemeX表情包");
                    } else {
                        System.out.println(path);
                        Runtime.getRuntime().exec("cmd /c start explorer "
                                + path + "\\MemeX表情包");
                    }

                }
                catch (IOException ie){
                    ie.printStackTrace();
                }



            }
        });

        editMenu.add(editMenuCopy);
        editMenu.add(editMenuChange);
        editMenu.add(editMenuStore);
        editMenu.add(editMenuShowInExplore);
        frame.setJMenuBar(menuBar);


        imgLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1)
                {
                    changePic();
                }
                else if(e.getButton() == MouseEvent.BUTTON3)
                {
                    imgPopMenu.show(imgLabel, e.getX(), e.getY());
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

        //具体操作区，如调整文字位置，颜色，大小
        JPanel operatePane = new JPanel(new BorderLayout());

        //-----------添加文字位置垂直滑动条-----------

        JPanel operateVerticalRowPane = new JPanel(new BorderLayout());
        JLabel verticalTips = new JLabel("  位 置  ");
        verticalSlider = new JSlider(JSlider.HORIZONTAL);
        //设置绘制刻度标签
        verticalSlider.setPaintLabels(true);
        verticalSlider.setMaximum(100);
        Hashtable<Integer, Component> verticalLabelTable = new Hashtable<Integer, Component>();
        verticalLabelTable.put(0, new JLabel("上"));
        verticalLabelTable.put(50, new JLabel("中"));
        verticalLabelTable.put(100, new JLabel("下"));
        verticalSlider.setLabelTable(verticalLabelTable);
        verticalSlider.setValue(100);
        verticalTips.setFont(new Font("黑体", Font.PLAIN, 18));
        operateVerticalRowPane.add(verticalTips,BorderLayout.WEST);
        operateVerticalRowPane.add(verticalSlider,BorderLayout.CENTER);

//        JLabel label=new JLabel("证件类型:");
//        contentPane.add(label);
//        JComboBox comboBox=new JComboBox();
//        comboBox.addItem("身份证");
//        comboBox.addItem("驾驶证");
//        comboBox.addItem("军官证");
//        contentPane.add(comboBox);



        operatePane.add(operateVerticalRowPane,BorderLayout.NORTH);

        textEditPanel.add(operatePane,BorderLayout.NORTH);

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
        // 设置前景/字体颜色
        submit.setForeground(Color.white);
        //设置背景颜色
        submit.setBackground(new Color(86, 119, 252));
        submit.setFont(new Font("黑体", Font.BOLD, 22));
        submit.setPreferredSize(new Dimension(50, 60));
        submit.setActionCommand("submit");
        submit.addActionListener(this);

        //状态栏
        statusBar = new JLabel(INIT_STATUS_BAR_STRING);
        statusBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        statusBar.setFont(new Font("黑体", Font.PLAIN, 14));

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
                statusBar.setText(FINISH_STATUS_BAR_STRING);
                break;
            case "storeBTN":
                //打开表情包商店
                //切换状态栏内容
                statusBar.setText(WAIT_FOR_STORE_STRING);
                picsChooser();
                //changePic();
                break;
            default:
        }

    }


    /**
     * 生成图片事件
     */
    private void summitCommend() {
        String text = textPane.getText();
        String newMemePath = ImageProcess.drawImg(text,
                WORD_WIDTH, WORD_HEIGHT,
                verticalSlider.getValue(),
                openFile);
        //打开错误值
        final String WRONG_PATH = "0";
        if (!newMemePath.equals(WRONG_PATH)) {

            ImageIcon image = new ImageIcon(newMemePath);
            //图片等比缩放函数
            Map<String, Integer> imgWidthAndHeight = ImageProcess.scaleImage(image, IMG_WIDTH, IMG_HEIGHT);
            scaleWidth = imgWidthAndHeight.get("width");
            scaleHeight = imgWidthAndHeight.get("height");

//            System.out.println("scaleWidth = " + scaleWidth);
//            System.out.println("scaleHeight = " + scaleHeight);

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

    /**打开表情模板选择器
     * */
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
                //获取点击中的URL
                int column = 2;
                String URL = (String) imageTable.
                        getValueAt(row, column);
                column = 1;
                //获取到的表情包名称（PlaceHolder）
                String placeHolder = (String)imageTable.
                        getValueAt(row,column);
                textPane.setText(placeHolder);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //改变图片
                        changePicByUrl(URL);
                    }
                });

//                try {
//                    thread.start();
//                    //thread.join();
//                }
//                catch (InterruptedException ie){
//                    ie.printStackTrace();
//                }
                thread.start();

                //增加热度
                int id = Integer.parseInt(
                        (String) imageTable.
                                getValueAt(row, 0));
                JDBCUtil.updatePicsTimes(id);

                frame.dispose();


            }
        });

        frame.setSize(new Dimension(800, 500));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //使用Toolkit设置图标
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.createImage("icon.jpg");
        frame.setIconImage(image);
        frame.setVisible(true);
    }
}
