package Controller.StaffController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            staffInforJpanel.enableEdit();
        } else if (src.equals("Cập Nhật")) {
            staffInforJpanel.saveChanges();
            staffInforJpanel.getBtnUpdateInfo().setText("Chỉnh Sửa");
        }
    }
}
