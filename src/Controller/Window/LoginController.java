package Controller.Window;

import Model.Customer;
import Model.Employee;
import Repository.UserAccountRepository;
import View.CustomerView.Customer_view;
import View.StaffView.Staff_view;
import View.Window.Login;
import View.Window.SignUp_Window;
import View.Window.WelcomeScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class LoginController implements ActionListener {
    private Login action;

    public LoginController(Login action) {
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
        }else if (str.equals("Đăng ký")) {
            action.dispose();
            new SignUp_Window();
        }

        else if (str.equals("Đăng nhập")) {
            // Đăng nhập
            String userName = action.getTextField().getText(); // Lấy tên đăng nhập từ TextField
            String password = new String(action.getPasswordField().getPassword()); // Lấy mật khẩu từ PasswordField
            if (userName.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(action, "Vui lòng nhập đầy đủ tài khoản và mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    UserAccountRepository userAccountDao = new UserAccountRepository(); // Tạo đối tượng UserAccountDao để lấy dữ liệu từ database
                    int id = userAccountDao.login(userName, password);  // thực hiện đăng nhập và trả về id
                    System.out.println(id);
                    if (id != -1){ // Nếu đăng nhập thành công thì lấy role
                        String role = userAccountDao.getRoleFromID(id);
                        System.out.println(role);
                        action.dispose();
                        
                        // if (role.equals("Quản lí")) System.out.println("Giao diện quản lí"); Tạm thời chưa có giao diện quản lí nên quản lí và nhân viên dùng chung cái này
                        if (role.equals("Thu ngân") || role.equals("Quản lí")) {
                            System.out.println("Giao diện thu ngân");
                            try {
                                Employee employee = userAccountDao.getEmployeeFromID(id);  // Lấy ra nhân viên khi đăng nhập đúng
                                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                                
                                SwingUtilities.invokeLater(() -> {
                                    try {
                                        new Staff_view(employee).setVisible(true);
                                    } catch (ClassNotFoundException | IOException | SQLException e1) {
                                        e1.printStackTrace();
                                    }
                                });
                            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
                                ex.printStackTrace();
                            }
                        }
                        else if (role.equals("Khách")){
                            System.out.println("Giao diện khách");
//                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            Customer customer = userAccountDao.getCustomerFromID(id);
                            SwingUtilities.invokeLater(() -> {
                                    try {
                                        new Customer_view(customer).setVisible(true);
                                    } catch (ClassNotFoundException | IOException | SQLException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }   
                            });
                        }
                    }
                } catch (IOException ex) {
                } catch (ClassNotFoundException ex) {
                } catch (SQLException ex) {
                }
            }
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
