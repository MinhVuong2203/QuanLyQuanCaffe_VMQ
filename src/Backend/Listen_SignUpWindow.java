package Backend;

import javax.swing.JCheckBox;
import javax.swing.JPasswordField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Fontend.SignUp_Window;
import Fontend.Staff_Sign;

public class Listen_SignUpWindow implements ActionListener {
private SignUp_Window action;
	public Listen_SignUpWindow(SignUp_Window action) {
		this.action = action;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
        String str = e.getActionCommand();
        if (str.equals("Quay lại")) {
            action.dispose();
            new Staff_Sign();
        }
		if (e.getSource() instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            
            if (checkBox == action.getShowMK()) { 
                togglePasswordVisibility(action.getPasswordField(), checkBox);
            } else if (checkBox == action.getShowCFMK()) {
                togglePasswordVisibility(action.getPasswordField_CF(), checkBox);
            }
        }
	}

	private void togglePasswordVisibility(JPasswordField passwordField, JCheckBox checkBox) {
        if (checkBox.isSelected()) {
            passwordField.setEchoChar((char) 0); // Hiện mật khẩu
        } else {
            passwordField.setEchoChar('●'); // Ẩn mật khẩu
        }
        passwordField.repaint();
        passwordField.revalidate();
    }
}
