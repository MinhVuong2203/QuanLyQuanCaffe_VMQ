package Backend;

import Fontend.Sign_Window;
import Fontend.Staff_Sign;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Listen_SignWindow implements ActionListener {
    private Sign_Window action;


    public Listen_SignWindow(Sign_Window action) {
        this.action = action;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
        String str = e.getActionCommand();
        if (str.equals("Nhân Viên")) {
            action.dispose();
            new Staff_Sign();
        }
        else if (str.equals("Quay lại")) {
            action.dispose();
            new Sign_Window();
        }
    }

}
