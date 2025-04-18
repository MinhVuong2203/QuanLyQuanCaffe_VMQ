package Controller.WindowController;

import Model.Employee;
import Model.Manager;
import Model.User;
import Service.UserAccount.IUserAccountService;
import Service.UserAccount.UserAccountService;
import View.ManagerView.ManagerJFrame;
import View.StaffView.StaffJFrame;
import View.Window.LoginView;
import View.Window.SignUpView;
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


public class LoginController implements ActionListener {   // Controller gọi view và service
    private LoginView loginView;  
    private IUserAccountService u;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;   
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        if (str.equals("Quay lại")) {
            loginView.dispose();
            SwingUtilities.invokeLater(() -> new WelcomeScreen().setVisible(true));
        }else if (str.equals("Đăng ký")) {
            loginView.dispose();
            new SignUpView();
        }
        else if (str.equals("Đăng nhập")) {
            // Đăng nhập
            String userName = loginView.getTextField().getText(); // Lấy tên đăng nhập từ TextField
            String password = new String(loginView.getPasswordField().getPassword()); // Lấy mật khẩu từ PasswordField
            if (userName.isEmpty() || password.isEmpty()){
                loginView.showMessage("Vui lòng nhập đầy đủ tài khoản và mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    u = new UserAccountService(); // Gọi UserAccount Service
                    User user = u.login(userName, password); // Thực hiện đăng nhập và trả về đối tượng User
                    if (user == null){
                        loginView.showMessage("Thông tin đăng nhập không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                        loginView.dispose();
                            if (user.getRole().equalsIgnoreCase("Thu ngân")) {
                            System.out.println("Giao diện thu ngân");
                            try {
                                Employee employee = u.getEmployeeFromID(user.getId());  // Lấy ra nhân viên khi đăng nhập đúng
                                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                                SwingUtilities.invokeLater(() -> {
                                    try {
                                        new StaffJFrame(employee).setVisible(true);
                                    } catch (ClassNotFoundException | IOException | SQLException e1) {
                                        e1.printStackTrace();
                                    }
                                });
                            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
                                ex.printStackTrace();
                            }
                        }
                        else if (user.getRole().equals("Quản lí")){
                            System.out.println("Giao diện quản lý");
                           Manager manager = u.getManagerFromID(user.getId());  // Lấy ra nhân viên khi đăng nhập đúng
                            SwingUtilities.invokeLater(() -> {
                                    try {
                                        new ManagerJFrame(manager).setVisible(true);
                                    } catch (ClassNotFoundException | IOException | SQLException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }   
                            });
                        }
                        
                    } catch (IOException ex) {
                } catch (ClassNotFoundException ex) {
                } catch (SQLException ex) {
                }
            }
        }


    if (e.getSource() instanceof JCheckBox) {
        JCheckBox checkBox = (JCheckBox) e.getSource();
        
        if (checkBox == loginView.getShowMK()) { 
            togglePasswordVisibility(loginView.getPasswordField(), checkBox);
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
