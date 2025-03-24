package Backend;

import Dao.UserAccountDao;
import Fontend.SignUp_Window;
import Fontend.Staff_Interface;
import Fontend.Staff_Sign;
import Fontend.WelcomeScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JPasswordField;
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
        }else if (str.equals("Đăng Ký")) {
            action.dispose();
            new SignUp_Window();
        }

        else if (str.equals("Đăng Nhập")) {
            // Đăng nhập
            UserAccountDao userAccountDao = new UserAccountDao(); // Tạo đối tượng UserAccountDao để lấy dữ liệu từ database
            String userName = action.getTextField().getText(); // Lấy tên đăng nhập từ TextField
            String password = new String(action.getPasswordField().getPassword()); // Lấy mật khẩu từ PasswordField
            String id = userAccountDao.login(userName, password);  // thực hiện đăng nhập và trả về id
            System.out.println(id);
            if (id != null){ // Nếu đăng nhập thành công thì lấy role
                String role = userAccountDao.getRoleFromID(id);
                System.out.println(role);
                action.dispose();
                new Staff_Interface(); // Mở giao diện Staff_Interface
            }
            userAccountDao.closeConnection();
        }
        
        if (e.getSource() instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            
            if (checkBox == action.getShowMK()) { 
                togglePasswordVisibility(action.getPasswordField(), checkBox);
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
