package src;

import src.com.timvan.picschooser.ImageProcess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
        //Create and set up the content pane.
        MemexPanel memexPanel = new MemexPanel(frame);

        /**/


        JComponent newContentPane = memexPanel;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        //使用Toolkit设置图标
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.createImage("icon.jpg");
        frame.setIconImage(image);





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
