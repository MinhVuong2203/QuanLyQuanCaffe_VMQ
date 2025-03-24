package Backend;

import Dao.UserAccountDao;
import Fontend.SignUp_Window;
import Fontend.Staff_Sign;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

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
        else if (str.equals("Đăng ký")) {
            // Đăng ký
            String name = action.getTextField_Ten().getText();
            String phone = action.getTextField_SDT().getText();
            String username = action.getTextField_TK().getText();
            String password = new String(action.getPasswordField().getPassword());
            String confirmPassword = new String(action.getPasswordField_CF().getPassword());

            this.action.setAll(name, phone, username, password, confirmPassword);

            //Kiểm tra xem mật khẩu nhập lại có khớp không
            if (confirmPassword.equals(password)) {
                System.out.println("Tên: " + name + " SĐT: " + phone + " TK: " + username + " MK: " + password);
                //Thêm vào database
                UserAccountDao userAccountDao = new UserAccountDao();
                userAccountDao.signUp(name, phone, username, password);
                action.dispose();
                new Staff_Sign();
            } else {
                JOptionPane.showMessageDialog(action, "Mật khẩu không khớp", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
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
