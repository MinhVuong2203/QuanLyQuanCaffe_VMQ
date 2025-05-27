package Controller.StaffController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import View.StaffView.StaffInforJpanel;

public class StaffInforController implements ActionListener {
    private StaffInforJpanel staffInforJpanel;
    public StaffInforController(StaffInforJpanel staffInforJpanel) {
        this.staffInforJpanel = staffInforJpanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String src = e.getActionCommand();
        if (src.equals("Chỉnh Sửa")) {
            staffInforJpanel.getBtnUpdateInfo().setText("Cập Nhật");
            staffInforJpanel.enableEditForEmp();
        } else if (src.equals("Cập Nhật")) {
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
