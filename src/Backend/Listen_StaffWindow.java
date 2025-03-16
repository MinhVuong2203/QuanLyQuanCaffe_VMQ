package Backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Entity.Sign_Window;
import Entity.Staff_Sign;

public class Listen_StaffWindow implements ActionListener {
    private Staff_Sign action;

    public Listen_StaffWindow(Staff_Sign action) {
        this.action = action;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
         String str = e.getActionCommand();
        if (str.equals("Quay lại")) {
            action.dispose();
            new Sign_Window();
        }
    }

}
