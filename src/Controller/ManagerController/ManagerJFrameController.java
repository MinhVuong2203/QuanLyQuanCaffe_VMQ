package Controller.ManagerController;

import Model.Manager;
import View.ManagerView.ManagerJFrame;
import View.ManagerView.ManagerProduct.ManageProduct;
import View.ManagerView.ManagerShift.EmployeeShiftPanel;
import View.ManagerView.ManagerStaff.StaffManagerJPanel;
import View.ManagerView.ManagerTable.TablePanel;
import View.ManagerView.ManagerShift.manageOrderAndSalary;
import View.StaffView.GamePanel;
import View.StaffView.RollCall;
import View.StaffView.StaffJPanel;
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
 
    private Manager manager;
    private manageOrderAndSalary manageOrderAndSalary;


    public ManagerJFrameController(ManagerJFrame managerJFrame, JPanel contentPanel, Manager manager , EmployeeShiftPanel employeeShiftPanel, TablePanel tablePanel,StaffManagerJPanel staffManagerJPanel, ManageProduct managerProduct, manageOrderAndSalary manageOrderAndSalary) {
        this.managerJFrame = managerJFrame;   
        this.contentPanel = contentPanel;
        this.employeeShiftPanel = employeeShiftPanel;
        this.tablePanel = tablePanel;
        this.staffManagerJPanel = staffManagerJPanel;
        this.manager = manager;
        this.manageOrderAndSalary = manageOrderAndSalary;
        this.managerProduct = managerProduct;
        
        
    }

    public ActionListener getButtonActionListener(String command) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
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
                            contentPanel.add(new Table_JPanel(manager.getId()), BorderLayout.CENTER);
                            break;
                        case "MANG VỀ":
                            contentPanel.add(new StaffJPanel(0, manager.getId()), BorderLayout.CENTER);
                            System.out.println("ID QLY: " + manager.getId());
                            break;
                        case "ĐIỂM DANH":
                            contentPanel.add(new RollCall(), BorderLayout.CENTER);
                            break;
                        case "MINI GAME":
                            contentPanel.add(new GamePanel(), BorderLayout.CENTER);
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
                            contentPanel.add(manageOrderAndSalary, BorderLayout.CENTER);
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
                } catch (ClassNotFoundException ex) {
                } catch (SQLException ex) {
                } catch (IOException ex) {
                }
            }
        };
    }
}
