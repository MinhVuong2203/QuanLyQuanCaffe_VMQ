package Controller.StaffController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import View.StaffView.StaffInforJDialog;

public class StaffInforController implements ActionListener {
    private StaffInforJDialog staffInforJpanel;
    public StaffInforController(StaffInforJDialog staffInforJpanel) {
        this.staffInforJpanel = staffInforJpanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String src = e.getActionCommand();
        if (src.equals("Chỉnh Sửa")) {
            staffInforJpanel.getBtnUpdateInfo().setText("Yêu Cầu");
            staffInforJpanel.enableEditForEmp();
        } else if (src.equals("Yêu Cầu")) {
            try {
                staffInforJpanel.saveChanges();
                staffInforJpanel.getBtnUpdateInfo().setText("Chỉnh Sửa");
            } catch (ClassNotFoundException | IOException | SQLException e1) {
                e1.printStackTrace();
            }
        } else if (src.equals("Đổi ảnh")) {
            staffInforJpanel.chooseFile();
        }
    }
    
}
