package com.src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;

public class Main {
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("小明计算器");
        frame.setLayout(new BorderLayout());
//        总体使用BorderLayout，North是JTextField，
//        Center是用JPanel，它里面是按钮，其layout是GridLayout。

        JTextField showPanel = new JTextField("",20);
        showPanel.setPreferredSize(new Dimension(0, 50));
        frame.add(showPanel,BorderLayout.NORTH);

        JPanel BtnPanel = new JPanel();

        Integer ROWS_LEN = 4 , COLS_LEN = 4;
        BtnPanel.setLayout(new GridLayout(ROWS_LEN,COLS_LEN));

        String str[]={"7","8","9","-","4","5","6","+","1","2","3","*","0",".","=","/"};
        for (int i = 0; i < ROWS_LEN; i++) {
            for (int j = 0; j < COLS_LEN; j++) {
                BtnPanel.add(new JButton(str[i*ROWS_LEN+j]));
            }
        }
        BtnPanel.setPreferredSize(new Dimension(0, 180));
        frame.add(BtnPanel);

        frame.pack();
        //frame.setResizable(false);  加上即不能调整窗口大小
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // 显示应用 GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}