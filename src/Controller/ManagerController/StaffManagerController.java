package Controller.ManagerController;

import Service.Implements.EmployeeService;
import View.ManagerView.ManagerStaff.AddOrFixEmployeeJDialog;
import View.ManagerView.ManagerStaff.StaffManagerJPanel;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class StaffManagerController implements ActionListener {
    private StaffManagerJPanel staffManagerJPanel;
    private EmployeeService employeeService;


    public StaffManagerController(StaffManagerJPanel staffManagerJPanel) {
        this.staffManagerJPanel = staffManagerJPanel;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        String command = e.getActionCommand();
        AddOrFixEmployeeJDialog addEmployeeJDialog;
       if (command.equalsIgnoreCase("Thêm")) {
        	System.out.println("Bạn đã nhấn: " + command);
            try {
                addEmployeeJDialog = new AddOrFixEmployeeJDialog("add", null, staffManagerJPanel);
                addEmployeeJDialog.setVisible(true);
            } catch (ClassNotFoundException | IOException | SQLException e1) {
                System.out.println("Error: " + e1.getMessage());
            }
        	
            // Code to remove staff
        } else if (command.equals("Cập nhật")) {
        	System.out.println("Bạn đã nhấn: " + command);
            try {
                System.out.println(this.staffManagerJPanel.getEmployeeSelected());
            	addEmployeeJDialog = new AddOrFixEmployeeJDialog("update", this.staffManagerJPanel.getEmployeeSelected(), staffManagerJPanel);
                addEmployeeJDialog.setVisible(true);
                staffManagerJPanel.setEmployeeSelected(null);
                
            } catch (ClassNotFoundException | IOException | SQLException e1) {
                System.out.println("Error: " + e1.getMessage());
            } 
        } else if (command.equals("Nghỉ việc")) {
        	System.out.println("Bạn đã nhấn: " + command);
            try {
                System.out.println("Đang cho nhân nghỉ việc nhân viên: " + this.staffManagerJPanel.getEmployeeSelected());
                JOptionPane .showConfirmDialog(staffManagerJPanel, "Bạn có chắc chắn muốn nghỉ việc nhân viên này không?","Xác nhận",JOptionPane.YES_NO_OPTION);
                if (JOptionPane.YES_OPTION == 0) {        
                    employeeService = new EmployeeService();
                    if (employeeService.quitJob(this.staffManagerJPanel.getEmployeeSelected().getId())) {
                        staffManagerJPanel.removeEmployeeFromTable();
                        JOptionPane.showMessageDialog(staffManagerJPanel, "Nghỉ việc nhân viên thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(staffManagerJPanel, "Không thể nghỉ việc nhân viên này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }

                }
            } catch (ClassNotFoundException | IOException | SQLException e1) {
                System.out.println("Error: " + e1.getMessage());
            } 
        }
    }







}
