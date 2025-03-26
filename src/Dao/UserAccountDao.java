package Dao;


import Fontend.Staff_Sign;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import javax.swing.JOptionPane;

public class UserAccountDao {
    private Connection conn;
    private Staff_Sign staff_Sign;


    public UserAccountDao() {
        try { 
            Properties properties = new Properties();

            try (FileInputStream fis = new FileInputStream("src/resource/database.properties")) {
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("Không thể đọc file database.properties");
                e.printStackTrace();
                return;
            }

            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String driver = properties.getProperty("Driver");

            Class.forName(driver);
            this.conn = DriverManager.getConnection(url , username, password);
            System.out.println("Kết nối thành công");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String login(String userName, String passWord) {
        // if (conn == null) {
        //     System.out.println("Kết nối thất bại");
        //     return;
        // }
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM UserAccount";  // Lấy tất cả bản nhân viên
            ResultSet rs = stmt.executeQuery(sql);
            String getID = ""; // Lấy ID đúng để biết là ai đăng nhập
            boolean check = false;  
            while (rs.next()) {
                if (userName.equals(rs.getString(2).trim()) && passWord.equals(rs.getString(3).trim())) {
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

    public int getIDMaxFromSQL(){
        int id = 0;
        try{
            Statement stmt = conn.createStatement();
            String sql = "SELECT MAX(ID) FROM UserAccount";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                id = rs.getInt(1);
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public void signUp(String name, String sdt, String userName, String passWord) {
        try{
            Statement stmt = conn.createStatement();
            int IDMax = this.getIDMaxFromSQL() + 1; // Lấy ID lớn nhất trong database và cộng thêm 1 để tạo một id mới
            String sql= "INSERT INTO UserAccount VALUES (" + IDMax + ", '" + userName + "', '" + passWord + "', '" + "Khách" + "')";
            String sql2 = "INSERT INTO Customer VALUES (" + IDMax + ", N'" + name + "', '" + sdt + "', " + 0 + ")";
            stmt.executeUpdate(sql);
            stmt.executeUpdate(sql2);
            JOptionPane.showMessageDialog(null, "Đăng ký thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // public void signUp(){ // Chưa đúng
    //     try {
    //         Statement stmt = conn.createStatement();
    //         String sql = "INSERT INTO UserAccount VALUES ('" + action.getTextField().getText() + "', '" + new String(action.getPasswordField().getPassword()) + "')";
    //         stmt.executeUpdate(sql);
    //         JOptionPane.showMessageDialog(null, "Đăng ký thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    //         stmt.close();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }


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
