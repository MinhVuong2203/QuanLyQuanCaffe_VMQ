package Controller.ManagerController;

import View.ManagerView.ManagerStaff.AddOrFixEmployeeJDialog;
import View.ManagerView.ManagerStaff.StaffManagerJPanel;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class StaffManagerController implements ActionListener {
    private StaffManagerJPanel staffManagerJPanel;
    
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
                addEmployeeJDialog = new AddOrFixEmployeeJDialog("add", null);
                addEmployeeJDialog.setVisible(true);
            } catch (ClassNotFoundException | IOException | SQLException e1) {
                System.out.println("Error: " + e1.getMessage());
            }
        	
            // Code to remove staff
        } else if (command.equals("Cập nhật")) {
        	System.out.println("Bạn đã nhấn: " + command);
            try {
                System.out.println(this.staffManagerJPanel.getEmployeeSelected());
            	addEmployeeJDialog = new AddOrFixEmployeeJDialog("update", this.staffManagerJPanel.getEmployeeSelected());
                addEmployeeJDialog.setVisible(true);
            } catch (ClassNotFoundException | IOException | SQLException e1) {
                System.out.println("Error: " + e1.getMessage());
            } 
        } else if (command.equals("Nghỉ việc")) {
        	System.out.println("Bạn đã nhấn: " + command);
            try {
                System.out.println(this.staffManagerJPanel.getEmployeeSelected());
            } catch (ClassNotFoundException | IOException | SQLException e1) {
                System.out.println("Error: " + e1.getMessage());
            } 
        }
    }







}
