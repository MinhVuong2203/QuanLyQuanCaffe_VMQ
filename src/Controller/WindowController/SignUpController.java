package Controller.WindowController;

import Service.Implements.CustomerService;
import Service.Interface.ICustomerService;
import Utils.ValidationUtils;
import View.Window.LoginView;
import View.Window.SignUpView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class SignUpController implements ActionListener {
    private SignUpView signUpView;
    

	public SignUpController(SignUpView signUpView) {
		this.signUpView = signUpView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
        String str = e.getActionCommand();
        if (str.equals("Quay lại")) {
            signUpView.dispose();
            new LoginView();
        }
        else if (str.equals("OK")) {
            try {
                // Đăng ký
                String name = signUpView.getTextField_Ten().getText();
                String phone = signUpView.getTextField_SDT().getText();
            
                ICustomerService customerService = new CustomerService();
              
                
                if (!ValidationUtils.isNotEmpty(name))
                    signUpView.showMessage("Họ và tên không được bỏ trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                else if (!ValidationUtils.isNotEmpty(phone))
                    signUpView.showMessage("Số điện thoại không được bỏ trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                else if (!ValidationUtils.isName(name))
                   signUpView.showMessage("Họ và tên không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                else if (!ValidationUtils.isPhoneNumber(phone))
                    signUpView.showMessage("Số điện thoại không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                else{
                    try {
                        System.out.println("Tên: " + name + " SĐT: " + phone);
                        customerService.addCustomer(name, phone);
                        signUpView.showMessage("Đăng kí tài khoản người dùng thành công", "Chúc mừng", JOptionPane.INFORMATION_MESSAGE);
                        signUpView.dispose();

                        new LoginView();
                    } catch (RuntimeException ex) {
                        signUpView.showMessage(ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }          
            } catch (ClassNotFoundException ex) {
            } catch (SQLException ex) {
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
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
