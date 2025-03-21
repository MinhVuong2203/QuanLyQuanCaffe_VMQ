package Backend;

import Fontend.SignUp_Window;
import Fontend.Staff_Sign;
import Fontend.WelcomeScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
            System.out.println("Đăng nhập");
            // Đăng nhập
             try { 
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=CaffeVMQ;encrypt=false";
            // String url = "jdbc:sqlserver://192.168.155.223:1433;databaseName=CaffeVMQ;user=sa;password=123456789;encrypt=false;trustServerCertificate=true;";

            String username = "sa";
            String password = "123456789";
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement();


            String sql = "SELECT * FROM UserAcount";
            // Muốn thêm nhân viên, thêm khách hàng đều phải truy xuất id max
            ResultSet rs = stmt.executeQuery(sql);
            String userName = action.getTextField().getText();  // Lấy tên đăng nhập từ giao diện
            String passWord = new String(action.getPasswordField().getPassword());  // Lấy mật khẩu từ giao diện
            boolean check = false;
            while (rs.next()) {
                if (userName.equals(rs.getString(2).trim()) && passWord.equals(rs.getString(3).trim())) {
                    System.out.println("Đăng nhập thành công");
                    check = true;
                    break;
                }
            }
            if (check == false) {
                // JOptionPane.showMessageDialog(Staff_Sign, "Sai tên đăng nhập hoặc mật khẩu");
            }


           
             
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e1) {
            e1.printStackTrace();
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
