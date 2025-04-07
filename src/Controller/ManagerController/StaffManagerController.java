package Controller.ManagerController;

import View.ManagerView.StaffManager.StaffManagerJPanel;
import java.awt.event.ActionListener;

public class StaffManagerController implements ActionListener {
    private StaffManagerJPanel staffManagerJPanel;
    
    public StaffManagerController(StaffManagerJPanel staffManagerJPanel) {
        this.staffManagerJPanel = staffManagerJPanel;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Tìm")) {
            String atb = (String) staffManagerJPanel.comboBox.getSelectedItem();
            String value = staffManagerJPanel.textField.getText();
            System.out.println("Attribute: " + atb + ", Value: " + value);
            this.staffManagerJPanel.showStaff(atb, value);

        } else if (command.equals("Remove Staff")) {
            // Code to remove staff
        } else if (command.equals("Update Staff")) {
            // Code to update staff
        } else if (command.equals("View Staff")) {
            // Code to view staff
        }
    }







}
