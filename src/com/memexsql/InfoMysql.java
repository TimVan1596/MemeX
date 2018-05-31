package src.com.memexsql;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

/**
 * @author TimVan
 * @date 2018/5/30 20:36
 */
public class InfoMysql {

    public JTable getMemesJTable (){

        JDBCUtil jdbcUtil = new JDBCUtil();



        /*
         * 设置JTable的列名
         */
        String[] columnNames = { "简介","地址","下载次数"};

        ///尝试使用Object
//        Object[][] obj = new Object[3][6];
//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < 2; j++) {
//                switch (j) {
//                    case 0:
//                        obj[i][j] = "赵匡义";
//                        break;
//                    case 1:
//                        obj[i][j] = "123215";
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }

   //     jdbcUtil.excuteQuery("select * from memepics",null);

        JTable imageTable = new JTable(JDBCUtil.getPicsInfo(), columnNames);

        JTableHeader header = imageTable.getTableHeader();

        /*
         * 设置JTable的列默认的宽度和高度
         */
        TableColumn column = null;
        int colunms = imageTable.getColumnCount();
        for (int i = 0; i < colunms; i++) {
            column = imageTable.getColumnModel().getColumn(i);
            /*将每一列的默认宽度设置为100*/
            column.setPreferredWidth((colunms-i)*50);
        }
        //设置高度
        imageTable.setRowHeight(55);
        //字体
        JTableHeader header=imageTable.getTableHeader();
        header.setFont(new Font("微软雅黑",Font.BOLD,26));

        /*
         * 设置JTable自动调整列表的状态，此处设置为关闭
         */
        imageTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        return imageTable;


    }
}
