package src.com.timvan.picschooser;

import src.com.timvan.memexsql.InfoMysql;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author TimVan
 * @date 2018/5/31 18:27
 */
public class ImageChooserFrame  {
    public ImageChooserFrame(){


    }

    //获取到的表情包URL
    public static String URL;
    //获取到的表情包名称（PlaceHolder）
    public static String name;

    //创建图形界面
    public void createGUI(){
        JFrame frame = new JFrame("请选择一个表情包图片");
        //实例化由IDEA UIDesign 的Form文件
        ImageChooser imageChooser =  new ImageChooser();
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
                //获取点击中的URL
                int column = imageTable.getSelectedColumn();
                int row =  imageTable.getSelectedRow();
//                URL = (String)imageTable.getValueAt(row,2);
//                name = (String)imageTable.getValueAt(row,1);
                frame.dispose();
            }
        });

        frame.setSize(new Dimension(800,500));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }




}
