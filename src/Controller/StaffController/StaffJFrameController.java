package Controller.StaffController;

import View.StaffView.GamePanel;
import View.StaffView.RollCall;
import View.StaffView.StaffJFrame;
import View.StaffView.Table_JPanel;
import View.Window.WelcomeScreen;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class StaffJFrameController {
    private StaffJFrame staffJFrame;
    private JPanel contentPanel;
    private Table_JPanel table_JPanel;
    private RollCall rollCall;
    private GamePanel gamePanel;

    public StaffJFrameController(StaffJFrame staffJFrame, JPanel contentPanel, Table_JPanel table_JPanel, RollCall rollCall, GamePanel gamePanel) {
        this.staffJFrame = staffJFrame;
        this.contentPanel = contentPanel;
        this.table_JPanel = table_JPanel;
        this.rollCall = rollCall;
        this.gamePanel = gamePanel;
    }

    public ActionListener getButtonActionListener(String command) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Đặt màu cho button được chọn
                for (Component btn : staffJFrame.getMenuPanel().getComponents()) {
                    if (btn instanceof JButton) {
                        btn.setBackground(new Color(39, 174, 96));
                    }
                }
                ((JButton) e.getSource()).setBackground(new Color(88, 214, 141));

                if (!command.equalsIgnoreCase("ĐĂNG XUẤT")) contentPanel.removeAll();
                switch (command) {
                    case "BÁN HÀNG":
                        contentPanel.add(table_JPanel, BorderLayout.CENTER);
                        break;
                    case "ĐIỂM DANH":
                        contentPanel.add(rollCall, BorderLayout.CENTER);
                        break;
                    case "MINI GAME":
                        contentPanel.add(gamePanel, BorderLayout.CENTER);
                        break;
                    case "ĐĂNG XUẤT":
                        // Thêm logic đăng xuất (ví dụ: đóng frame, quay về màn hình đăng nhập)
                        int confirm = JOptionPane.showConfirmDialog(staffJFrame,
                                "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất",
                                JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            staffJFrame.dispose();
                            // Ví dụ: new LoginJFrame().setVisible(true);
                            SwingUtilities.invokeLater(() -> new WelcomeScreen()); // WelcomeScrren
                        }
                        break;
                }
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        };
    }
}
