package src.com.picschooser;

import src.com.memexsql.InfoMysql;

import javax.swing.*;
import java.awt.*;

/**
 * @author TimVan
 * @date 2018/5/31 18:27
 */
public class ImageChooserFrame  {
    public ImageChooserFrame(){


    }

    //创建图形界面
    public void createGUI(){
        JFrame frame = new JFrame("请选择一个表情包图片");
        frame.setContentPane(new ImageChooser().panel);

        Container container = frame.getContentPane();
        container.add(new InfoMysql().getMemesJTable());

        frame.setSize(new Dimension(800,500));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

}
