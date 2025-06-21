package Controller.StaffController;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import View.ManagerView.ManagerStaff.AddOrFixEmployeeJDialog;
import View.ManagerView.ManagerStaff.StaffManagerJPanel;

public class FixEmployeeController  extends MouseAdapter {
    private final StaffManagerJPanel staffManagerJPanel;

    public FixEmployeeController (StaffManagerJPanel staffManagerJPanel) {
        this.staffManagerJPanel = staffManagerJPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            e.consume();
            try {
                AddOrFixEmployeeJDialog addEmployeeJDialog = new AddOrFixEmployeeJDialog("update",
                		staffManagerJPanel.getEmployeeSelected(), staffManagerJPanel);
                addEmployeeJDialog.setVisible(true);
                staffManagerJPanel.setEmployeeSelected(null);
            } catch (ClassNotFoundException | IOException | SQLException ex) {       
                JOptionPane.showMessageDialog(staffManagerJPanel,
                        "Lỗi khi mở form cập nhật: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
