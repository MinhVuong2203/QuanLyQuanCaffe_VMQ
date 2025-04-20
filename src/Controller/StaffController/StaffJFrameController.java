package Controller.StaffController;

import Model.Employee;
import View.StaffView.RollCall;
import View.StaffView.StaffJFrame;
import View.StaffView.Table_JPanel;
import View.Window.WelcomeScreen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.*;

public class StaffJFrameController {
    private StaffJFrame staffJFrame;
    private Employee employee;
    private JPanel contentPanel;

    public StaffJFrameController(StaffJFrame staffJFrame, Employee employee, JPanel contentPanel) {
        this.staffJFrame = staffJFrame;
        this.employee = employee;
        this.contentPanel = contentPanel;
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

                contentPanel.removeAll();
                try {
                    switch (command) {
                        case "BÁN HÀNG":
                            contentPanel.add(new Table_JPanel(employee.getId()), BorderLayout.CENTER);
                            break;
                        case "ĐIỂM DANH":
                            contentPanel.add(new RollCall(), BorderLayout.CENTER);
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
                } catch (ClassNotFoundException | SQLException | IOException ex) {
                    ex.printStackTrace();
                }
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        };
    }
}
