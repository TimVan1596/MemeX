package src;

import src.com.timvan.picschooser.*;
import src.com.timvan.memexsql.*;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.*;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * @author TimVan
 */
public class MemexFrame extends JPanel
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

    //主窗口
    private JFrame frame;
    private JTextPane textPane;
    private File openFile;
    private JLabel imgLabel;
    //图片上的右键菜单
    private JPopupMenu imgPopMenu;
    //状态栏
    private JLabel statusbar;


    public void changePicByUrl(String url) {

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
        scaleWidth = (int) (IMG_WIDTH * 1.2);
        ;
        scaleHeight = IMG_HEIGHT;
        image = new ImageIcon(image.getImage().getScaledInstance(
                scaleWidth, scaleHeight, Image.SCALE_DEFAULT));

        imgLabel.setIcon(image);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
        imgLabel.setVerticalAlignment(JLabel.CENTER);

        //图片右键菜单
        JMenuItem mCopy, mChange, mStore;
        imgPopMenu = new JPopupMenu();

        mCopy = new JMenuItem("复制到剪切板");
        imgPopMenu.add(mCopy);


        mCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Image image = new Image();
                Image image = null;
                try {
                     image = ImageIO.read(new File(openFile.getAbsolutePath()));
                }
                catch (IOException ie){
                    ie.printStackTrace();
                }
                finally {
                    ImageProcess.CopyToClipboard(image);
                }
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
                picsChooser();
            }
        });
        imgPopMenu.add(mStore);

        imgLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                picsChooser();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    imgPopMenu.show(imgLabel, e.getX(), e.getY());
                }
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

        textEditPanel.add(submit, BorderLayout.SOUTH);
        this.add(imgPanel, BorderLayout.NORTH);


        //状态栏
        statusbar = new JLabel("Status: Runing ! ");
        statusbar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        this.add(statusbar, BorderLayout.SOUTH);

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
     * 创建GUI
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("MemeX - 表情包生成器 " + versionInfo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Create and set up the content pane.
        JComponent newContentPane = new MemexFrame();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        //使用Toolkit设置图标
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.createImage("icon.jpg");
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

        frame = new JFrame("请选择一个表情包图片");

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
