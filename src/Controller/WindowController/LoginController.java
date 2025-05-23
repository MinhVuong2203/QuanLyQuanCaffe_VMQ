package Controller.WindowController;

import Model.Employee;
import Model.Manager;
import Model.User;
import Service.Implements.UserAccountService;
import Service.Interface.IUserAccountService;
import View.ManagerView.ManagerJFrame;
import View.StaffView.StaffJFrame;
import View.Window.ChangePasswordView;
import View.Window.ForgetPasswordView;
import View.Window.LoginView;
import View.Window.SignUpView;
import View.Window.WelcomeScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
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
        } else if (str.equals("Đăng ký")) {
            loginView.dispose();
            new SignUpView();
        } else if (str.equalsIgnoreCase("Quên mật khẩu?")) {
        	loginView.dispose();
        	new ForgetPasswordView();
        } else if (str.equals("Đăng nhập")) {
            // Đăng nhập
            String userName = loginView.getTextField().getText(); // Lấy tên đăng nhập từ TextField
            String password = new String(loginView.getPasswordField().getPassword()); // Lấy mật khẩu từ PasswordField
            if (userName.isEmpty() || password.isEmpty()) {
                loginView.showMessage("Vui lòng nhập đầy đủ tài khoản và mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
            	this.OpenLoading();
                // Sử dụng SwingWorker để đăng nhập
                SwingWorker<User, Void> worker = new SwingWorker<>() {
                    @Override
                    protected User doInBackground() throws Exception {
                        u = new UserAccountService(); // Gọi UserAccount Service
                        return u.login(userName, password); // Thực hiện đăng nhập
                    }

                    @Override
                    protected void done() {
                        try {
                            User user = get();

                            if (user == null) {
                            	CloseLoading();
                                loginView.showMessage("Thông tin đăng nhập không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);                             
                                return;
                            }
                            if (password.startsWith("VMQ") && password.endsWith("@")) {
                            	loginView.showMessage("Bạn cần phải đổi mật khẩu mới" , "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            	new ChangePasswordView(user.getId(),password).setVisible(true);
                            }
                            if (user.getRole().equalsIgnoreCase("Thu ngân") || user.getRole().equalsIgnoreCase("pha chế") || user.getRole().equalsIgnoreCase("phục vụ")) {
                                System.out.println("Giao diện nhân viên");
                                try {
                                    Employee employee = u.getEmployeeFromID(user.getId());  // Lấy ra nhân viên khi đăng nhập đúng
                                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                                    SwingUtilities.invokeLater(() -> {
                                        try {
                                            new StaffJFrame(employee).setVisible(true);
                                            loginView.dispose();
                                            CloseLoading();
                                        } catch (ClassNotFoundException | IOException | SQLException e1) {
                                            e1.printStackTrace();
                                        }
                                    });
                                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
                                    ex.printStackTrace();
                                }
                            } else if (user.getRole().equals("Quản lí")) {
                                System.out.println("Giao diện quản lý");
                                Manager manager = u.getManagerFromID(user.getId());  // Lấy ra nhân viên khi đăng nhập đúng
                                SwingUtilities.invokeLater(() -> {
                                    try {
                                        new ManagerJFrame(manager).setVisible(true);
                                        loginView.dispose();
                                        CloseLoading();
                                    } catch (ClassNotFoundException | IOException | SQLException e1) {
                                        e1.printStackTrace();
                                    }   
                                });
                            }
                        } catch (Exception ex) {
                        	CloseLoading();
                            loginView.showMessage("Lỗi đăng nhập: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    }
                };
                worker.execute(); // Bắt đầu thực thi
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
    
    public void CloseLoading() {
    	// Ẩn loading, mở lại các nút
    	loginView.getLoadingLabel().setVisible(false);
    	loginView.getOverlayPanel().setVisible(false);
    }
    
    public void OpenLoading() {
    	 // Hiển thị loading và vô hiệu hóa các nút
        loginView.getLoadingLabel().setVisible(true);
        loginView.getOverlayPanel().setVisible(true);
    }
}