package Backend;

import Fontend.Sign_Window;
import Fontend.Staff_Sign;
import Fontend.WelcomeScreen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

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
            SwingUtilities.invokeLater(() -> new WelcomeScreen().setVisible(true));
        }
    }

}
