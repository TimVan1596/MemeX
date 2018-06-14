package src.com.timvan.picschooser;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

/**
 * @author TimVan
 * @date 2018/5/29 18:06
 */
public class ImageChooser extends JFrame {
    public JPanel panel;
    public JPanel imageBrowser;
    public JPanel operatePanel;
    public JButton okButton;
    public JLabel infoLabel;
    public JPanel infoPanel;
    public JLabel helpTips;


    public ImageChooser() {

        helpTips.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JOptionPane.showMessageDialog(null
                        , "请联系QQ: 877020296 \n" +
                                "并发送您要上传的表情包即可",
                        "如何成为一名UP主？",
                        INFORMATION_MESSAGE);

            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here


    }

}
