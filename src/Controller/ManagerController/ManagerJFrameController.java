package Controller.ManagerController;

import Model.Manager;
import View.ManagerView.ManagerJFrame;
import View.ManagerView.ManagerShift.EmployeeShiftPanel;
import View.ManagerView.ManagerStaff.StaffManagerJPanel;
import View.ManagerView.ManagerTable.TablePanel;
import View.StaffView.RollCall;
import View.StaffView.Table_JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.*;

public class ManagerJFrameController {
    private ManagerJFrame managerJFrame;
    private Manager manager;
    private JPanel contentPanel;
    private EmployeeShiftPanel employeeShiftPanel;
    private TablePanel tablePanel;
    private StaffManagerJPanel staffManagerJPanel;

    public ManagerJFrameController(ManagerJFrame managerJFrame, Manager manager, JPanel contentPanel, EmployeeShiftPanel employeeShiftPanel, TablePanel tablePanel,StaffManagerJPanel staffManagerJPanel) {
        this.managerJFrame = managerJFrame;
        this.manager = manager;
        this.contentPanel = contentPanel;
        this.employeeShiftPanel = employeeShiftPanel;
        this.tablePanel = tablePanel;
        this.staffManagerJPanel = staffManagerJPanel;
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
                contentPanel.removeAll();
                try {
                    switch (command) {
                        case "BÁN HÀNG":
                            contentPanel.add(new Table_JPanel(), BorderLayout.CENTER);
                            break;
                        case "ĐIỂM DANH":
                            contentPanel.add(new RollCall(), BorderLayout.CENTER);
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
                            }
                            break;
                    }
                } catch (ClassNotFoundException | SQLException | IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(managerJFrame, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        };
    }
}
