package src;

import javax.swing.*;
import java.awt.*;

/**
 * @author TimVan
 * @date 2018/6/4 22:05
 */
public class MemeX {

    private static final String versionInfo = "v0.4";
    private static final String releaseDate =
            "2018年6月3日21:49:55";


    /**
     * 创建GUI
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("MemeX - 斗图神器 " + versionInfo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Create and set up the content pane.
        JComponent newContentPane = new MemexPanel();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        //使用Toolkit设置图标
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.createImage("icon.jpg");
        frame.setIconImage(image);


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

}
