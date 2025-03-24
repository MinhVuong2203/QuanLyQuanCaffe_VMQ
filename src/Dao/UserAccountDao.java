package Dao;


import Fontend.Staff_Sign;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class UserAccountDao {
    private Connection conn;
    private Staff_Sign action;
    public UserAccountDao(Staff_Sign action) {
        this.action = action;
         try { 
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=CaffeVMQ;encrypt=false";
            // String url = "jdbc:sqlserver://192.168.155.223:1433;databaseName=CaffeVMQ;user=sa;password=123456789;encrypt=false;trustServerCertificate=true;";

            String username = "sa";
            String password = "123456789";
            this.conn = DriverManager.getConnection(url, username, password);
            System.out.println("Kết nối thành công");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String login(){
        // if (conn == null) {
        //     System.out.println("Kết nối thất bại");
        //     return;
        // }
        try {
            Statement stmt = conn.createStatement();

            String sql = "SELECT * FROM UserAccount";  // Lấy tất cả bản nhân viên
            ResultSet rs = stmt.executeQuery(sql);
            String userName = action.getTextField().getText();  // Lấy tên đăng nhập từ giao diện
            String passWord = new String(action.getPasswordField().getPassword());  // Lấy mật khẩu từ giao diện

            String getID = ""; // Lấy ID đúng để biết là ai đăng nhập
            boolean check = false;  
            while (rs.next()) {
                if (userName.equals(rs.getString(2).trim()) && passWord.equals(rs.getString(3).trim())) {
                    JOptionPane.showMessageDialog(null, "Đăng nhập thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    getID = rs.getString(1);
                    rs.close();
                    stmt.close();
                    return getID;
                }
            }
            // Nếu không đúng giá trị thì thông báo lỗi
            JOptionPane.showMessageDialog(null, "Sai tên đăng nhập hoặc mật khẩu", "Thông báo", JOptionPane.ERROR_MESSAGE);
            rs.close();
            stmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;  // Nếu không đúng thì trả về null
    }

    public void signUp(){ // Chưa đúng
        try {
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO UserAccount VALUES ('" + action.getTextField().getText() + "', '" + new String(action.getPasswordField().getPassword()) + "')";
            stmt.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Đăng ký thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getRoleFromID(String id){
        String role = "";
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM UserAccount WHERE ID = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                role = rs.getString(4);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return role;
    }

    // Hàm đóng database mỗi khi thực hiện xong tất cả các tác vụ - Quan trọng phải có và phải gọi sau khi dùng xong
    public void closeConnection(){
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
