package Controller.ManagerController;

import View.ManagerView.ManagerStaff.AddEmployeeJDialog;
import View.ManagerView.ManagerStaff.StaffManagerJPanel;
import java.awt.event.ActionListener;

public class StaffManagerController implements ActionListener {
    private StaffManagerJPanel staffManagerJPanel;
    
    
    
    
    public StaffManagerController(StaffManagerJPanel staffManagerJPanel) {
        this.staffManagerJPanel = staffManagerJPanel;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        String command = e.getActionCommand();
       if (command.equalsIgnoreCase("Thêm")) {
        	System.out.println("Bạn đã nhấn: " + command);
        	AddEmployeeJDialog addEmployeeJDialog = new AddEmployeeJDialog();
        	addEmployeeJDialog.setVisible(true);
            // Code to remove staff
        } else if (command.equals("Cập nhật")) {
        	System.out.println("Bạn đã nhấn: " + command);
            // Code to update staff
        } else if (command.equals("Nghỉ việc")) {
        	System.out.println("Bạn đã nhấn: " + command);
            // Code to view staff
        }
    }







}
