package src.com.timvan.memexsql;

import src.com.timvan.picschooser.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * 用于从Mysql里返回相关数据
 *
 * @author TimVan
 * @date 2018/5/30 20:36
 */
public class InfoMysql {

    /**
     * imageChooser 获取到的选择界面
     * ROW_HEIGHT   固定行高
     */
    private ImageChooser imageChooser;
    private final static int ROW_HEIGHT = 140;

    /**获取线上模板图片的JTable
     * */
    public JTable getMemesJTable(ImageChooser imageChooser) {
        //获取窗口
        this.imageChooser = imageChooser;

        Object[][] realObject = JDBCUtil.getPicsInfo();


        // 新建一个默认数据模型
        DefaultTableModel model = new DefaultTableModel(realObject
                ,columnNames);

        JTable imageTable = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //表格不允许被编辑，但可以选中
                return false;
            }
        };
        imageTable.setModel(model);

        TableCellRenderer tcr = new ColorTableCellRenderer();
        // 让内容居中
       // tcr.setHorizontalAlignment(SwingConstants.CENTER);

        ((ColorTableCellRenderer) tcr).setHorizontalAlignment(SwingConstants.CENTER);
        //为JTable增加渲染器，因为是针对于表格中的所有单元格，所有用Object.class
        imageTable.setDefaultRenderer(Object.class,tcr);


        //设置字体
        imageTable.setFont(new Font("黑体", Font.BOLD, 18));
        imageTable.getTableHeader().setFont(
                new Font("黑体", Font.BOLD, 23));

        // 设置内容居中
//        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
//        tcr.setHorizontalAlignment(SwingConstants.CENTER);
//        imageTable.setDefaultRenderer(Object.class, tcr);

        //自动调整列宽
        resizeColumnWidth(imageTable);
        //设置行高
        imageTable.setRowHeight(ROW_HEIGHT);
        //设置初始的选择行
        imageTable.setRowSelectionInterval(0, 0);


        return imageTable;


    }

    /**
     *设置JTable的列名
     */
    private String[] columnNames = {"图号", "名称", "预览", "下载量","UP主"};

    public enum ColumnIndex {
        id(0), name(1), pic(2), times(3),author(4);

        private final int index;

        private ColumnIndex(int name) {
            this.index = name;
        }

        public int getIndex() {
            return index;
        }
    }


    /**
     * 自动调整JTable列宽度
     * src:https://cloud.tencent.com/developer/ask/84252
     */
    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            // Min width
            int width = 15;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            if (width > 400) {
                width = 400;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }


    class ColorTableCellRenderer extends DefaultTableCellRenderer {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            if (column  == ColumnIndex.pic.getIndex()) {
                //获取图片的URL
                String URL = (String) table.getValueAt(row,column);
                if (URL != null){
                    ImageIcon img = new ImageIcon(
                            ImageProcess.getImageBufferStream(URL));
                    img=new ImageIcon(img.getImage().getScaledInstance(
                            ROW_HEIGHT, ROW_HEIGHT, Image.SCALE_DEFAULT));
                    return new JLabel(img);
                }

                ImageIcon img = new ImageIcon("pics/熊猫人不屑.jpg");
                img.setImage(img.getImage().getScaledInstance(
                        ROW_HEIGHT,ROW_HEIGHT,Image.SCALE_DEFAULT));
                return new JLabel(img);


            } else {
                return super.getTableCellRendererComponent(table, value, isSelected,hasFocus, row, column);            }
        }

        @Override
        public void setHorizontalAlignment(int alignment){
            super.setHorizontalAlignment(alignment);
        }

    }


}
