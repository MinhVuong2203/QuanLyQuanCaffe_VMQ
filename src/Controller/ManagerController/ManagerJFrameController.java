package Controller.ManagerController;

import Model.Manager;
import View.ManagerView.ManagerJFrame;
import View.ManagerView.ManagerProduct.ManageProduct;
import View.ManagerView.ManagerShift.EmployeeShiftPanel;
import View.ManagerView.ManagerStaff.StaffManagerJPanel;
import View.ManagerView.ManagerTable.TablePanel;
import View.StaffView.GamePanel;
import View.StaffView.RollCall;
import View.StaffView.Table_JPanel;
import View.Window.WelcomeScreen;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.*;

public class ManagerJFrameController {
    private ManagerJFrame managerJFrame;
    private JPanel contentPanel;
    private EmployeeShiftPanel employeeShiftPanel;
    private TablePanel tablePanel;
    private StaffManagerJPanel staffManagerJPanel;
    private ManageProduct managerProduct;
    private GamePanel gamePanel;
    private Table_JPanel table_JPanel;
    private RollCall rollCall;

    public ManagerJFrameController(ManagerJFrame managerJFrame, JPanel contentPanel, Table_JPanel table_JPanel, RollCall rollcall, EmployeeShiftPanel employeeShiftPanel, TablePanel tablePanel,StaffManagerJPanel staffManagerJPanel, ManageProduct managerProduct, GamePanel gamePanel) {
        this.managerJFrame = managerJFrame;   
        this.contentPanel = contentPanel;
        this.employeeShiftPanel = employeeShiftPanel;
        this.tablePanel = tablePanel;
        this.staffManagerJPanel = staffManagerJPanel;
        this.managerProduct = managerProduct;
        this.gamePanel = gamePanel;
        this.table_JPanel = table_JPanel;
        this.rollCall = rollcall;
    }

    public ActionListener getButtonActionListener(String command) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Đặt màu cho button được chọn
                 for (Component btn : managerJFrame.getMenuPanel().getComponents()) {
                    if (btn instanceof JButton) {
                        btn.setBackground(new Color(39, 174, 96));
                    }
                }
                ((JButton) e.getSource()).setBackground(new Color(88, 214, 141));

                // Xử lý hành động
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
                        case "XẾP LỊCH":
                            contentPanel.add(employeeShiftPanel, BorderLayout.CENTER);
                            break;
                        case "BÀN":
                            contentPanel.add(tablePanel, BorderLayout.CENTER);
                            break;
                        case "NHÂN VIÊN":
                            contentPanel.add(staffManagerJPanel, BorderLayout.CENTER);
                            break;
                        case "SẢN PHẨM":
                            contentPanel.add(managerProduct, BorderLayout.CENTER);
                            break;
                        case "DOANH THU":
                            // Thêm logic cho DOANH THU (chưa được triển khai)
                            JOptionPane.showMessageDialog(managerJFrame, "Chức năng DOANH THU chưa được triển khai!");
                            break;
                        case "ĐĂNG XUẤT":
                            // Thêm logic đăng xuất (ví dụ: đóng frame, quay về màn hình đăng nhập)
                            int confirm = JOptionPane.showConfirmDialog(managerJFrame, 
                                    "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất", 
                                    JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                managerJFrame.dispose();
                                SwingUtilities.invokeLater(() -> new WelcomeScreen());  // WelcomeScrren
                            }
                            break;
                    }
                
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        };
    }
}
