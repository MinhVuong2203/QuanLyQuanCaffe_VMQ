package Controller.Window;

import Repository.*;
import Utils.ConvertInto;
import Utils.ValidationUtils;
import View.Window.Login;
import View.Window.SignUp_Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class SignUpController implements ActionListener {
private SignUp_Window action;
	public SignUpController(SignUp_Window action) {
		this.action = action;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
        String str = e.getActionCommand();
        if (str.equals("Quay lại")) {
            action.dispose();
            new Login();
        }
        else if (str.equals("Đăng ký")) {
            try {
                // Đăng ký
                String name = action.getTextField_Ten().getText();
                String phone = action.getTextField_SDT().getText();
                String username = action.getTextField_TK().getText();
                String password = new String(action.getPasswordField().getPassword());
                String confirmPassword = new String(action.getPasswordField_CF().getPassword());
                
                UserAccountRepository userAccountDao = new UserAccountRepository();
                CustomerRepository customerDao = new CustomerRepository();
                EmployeeRepository employeeDao = new EmployeeRepository();
                
                if (!ValidationUtils.isNotEmpty(name))
                    JOptionPane.showMessageDialog(action, "Họ và tên không được bỏ trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
                else if (!ValidationUtils.isNotEmpty(phone))
                    JOptionPane.showMessageDialog(action, "Số điện thoại không được bỏ trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
                else if (!ValidationUtils.isNotEmpty(username))
                    JOptionPane.showMessageDialog(action, "Tên đăng nhập không được bỏ trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
                else if (!ValidationUtils.isNotEmpty(password))
                    JOptionPane.showMessageDialog(action, "Mật khẩu không được bỏ trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
                else if (!ValidationUtils.isNotEmpty(confirmPassword))
                    JOptionPane.showMessageDialog(action, "Xác nhập mật khẩu không được bỏ trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
                else if (!ValidationUtils.isName(name))
                    JOptionPane.showMessageDialog(action, "Họ và tên không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                else if (!ValidationUtils.isPhoneNumber(phone))
                    JOptionPane.showMessageDialog(action, "Số điện thoại không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                else if (customerDao.checkEqualsPhone(phone) || employeeDao.checkEqualsPhone(phone))
                    JOptionPane.showMessageDialog(action, "Số điện thoại đã tồn tại. Không được dùng số điện thoại của người khác nhé!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                else if (!ValidationUtils.isUsername(username))
                    JOptionPane.showMessageDialog(action, "Tên đăng nhập phải ít nhất 8 kí tự gồm: chữ hoa, chữ thường và số", "Lỗi", JOptionPane.ERROR_MESSAGE);
                else if (userAccountDao.checkEqualsUserName(username))
                    JOptionPane.showMessageDialog(action, "Tên đăng nhập đã tồn tại. Vui lòng suy nghĩ một cái tên khác hay hơn", "Lỗi", JOptionPane.ERROR_MESSAGE);
                else if (!ValidationUtils.isPassword(password))
                    JOptionPane.showMessageDialog(action, "Mật khẩu phải ít nhất 8 kí tự gồm: chữ hoa, chữ thường, số hoặc kí tự đặc biệt", "Lỗi", JOptionPane.ERROR_MESSAGE);
                else if (!password.equals(confirmPassword))
                    JOptionPane.showMessageDialog(action, "Nhập lại mật khẩu không khớp", "Lỗi", JOptionPane.ERROR_MESSAGE);
                else{
                    String hashedPassword = ConvertInto.hashPassword(password);
                    System.out.println("Tên: " + name + " SĐT: " + phone + " TK: " + username + " MK: " + password + " Mã hóa: " + hashedPassword);
                    userAccountDao.signUp(name, phone, username, hashedPassword);
                    action.dispose();
                    new Login();
                }
            } catch (IOException ex) {
            } catch (ClassNotFoundException ex) {
            } catch (SQLException ex) {
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
