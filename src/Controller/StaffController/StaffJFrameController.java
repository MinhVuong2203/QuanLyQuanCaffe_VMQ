package Controller.StaffController;

import Model.Employee;
import View.StaffView.GamePanel;
import View.StaffView.RegisterWorkJPanel;
import View.StaffView.RollCall;
import View.StaffView.StaffInforJpanel;
import View.StaffView.StaffJFrame;
import View.StaffView.StaffJPanel;
import View.StaffView.Table_JPanel;
import View.StaffView.TakeAwayJPanel;
import View.Window.WelcomeScreen;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.*;

public class StaffJFrameController {
    private StaffJFrame staffJFrame;
    private JPanel contentPanel;
    private Employee employee;
    private JTabbedPane takeAwayTabbedPane;

    public StaffJFrameController(StaffJFrame staffJFrame, JPanel contentPanel, Employee employee) {
        this.staffJFrame = staffJFrame;
        this.contentPanel = contentPanel;
        this.employee = employee;
    }

    public ActionListener getButtonActionListener(String command) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Đặt màu cho button được chọn
                    for (Component btn : staffJFrame.getMenuPanel().getComponents()) {
                        if (btn instanceof JButton) {
                            btn.setBackground(new Color(39, 174, 96));
                        }
                    }
                    ((JButton) e.getSource()).setBackground(new Color(88, 214, 141));

                    if (!command.equalsIgnoreCase("ĐĂNG XUẤT"))
                        contentPanel.removeAll();
                    switch (command) {
                        case "BÁN HÀNG":
                            contentPanel.add(new Table_JPanel(employee.getId()), BorderLayout.CENTER);
                            break;
                        case "MANG VỀ":
                            // Tạo JTabbedPane mới để chứa các tab đơn mang về
                            takeAwayTabbedPane = new JTabbedPane();
                            // contentPanel.add(new TakeAwayJPanel(employee.getId()), BorderLayout.CENTER);
                            System.out.println("ID NV: " + employee.getId());
                            // Tạo panel điều khiển với nút thêm tab mới
                            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                            controlPanel.setBackground(new Color(231, 215, 200));

                            // Tạo nút thêm đơn mang về mới
                            JButton addTakeAwayButton = new JButton("+ Thêm đơn mang về");
                            addTakeAwayButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
                            addTakeAwayButton.setBackground(new Color(144, 238, 144));
                            addTakeAwayButton.setBorderPainted(false);
                            addTakeAwayButton.setFocusPainted(false);

                            // Thêm sự kiện cho nút
                            addTakeAwayButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    staffJFrame.addNewTakeAwayTab(takeAwayTabbedPane, employee.getId());
                                }
                            });

                            controlPanel.add(addTakeAwayButton);

                            // Thêm các thành phần vào contentPanel
                            contentPanel.setLayout(new BorderLayout());
                            contentPanel.add(controlPanel, BorderLayout.NORTH);
                            contentPanel.add(takeAwayTabbedPane, BorderLayout.CENTER);

                            staffJFrame.addNewTakeAwayTab(takeAwayTabbedPane, employee.getId());
                            break;
                        case "ĐIỂM DANH":
                            contentPanel.add(new RollCall(), BorderLayout.CENTER);
                            break;
                        case "ĐĂNG KÝ CA":
                            contentPanel.add(new RegisterWorkJPanel(employee), BorderLayout.CENTER);
                            break;
                        case "MINI GAME":
                            contentPanel.add(new GamePanel(), BorderLayout.CENTER);
                            break;
                        case "<html>THÔNG TIN CÁ <br>NHÂN</html>":
                            StaffInforJpanel staffInfoPanel = new StaffInforJpanel(employee);
                            // Thiết lập thông tin nhân viên từ đối tượng employee
                            // staffInfoPanel.setUserInfo(
                            //         employee.getName(),
                            //         employee.getGender(),
                            //         employee.getBirthDate(),
                            //         employee.getCCCD(),
                            //         employee.getPhone(),
                            //         employee.getRole());
                            contentPanel.add(staffInfoPanel, BorderLayout.CENTER);
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
                } catch (ClassNotFoundException ex) {
                } catch (SQLException ex) {
                } catch (IOException ex) {
                }
            }
        };
    }
}
