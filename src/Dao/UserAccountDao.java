package Dao;

import Backend.PasswordHasherSHA256;
import Entity.Employee;
import Fontend.Login;
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
    private Login staff_Sign;


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

    public int login(String userName, String passWord) {
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM UserAccount";  // Lấy tất cả bản nhân viên
            ResultSet rs = stmt.executeQuery(sql);
            int getID = -1; // Lấy ID đúng để biết là ai đăng nhập
            boolean check = false;  
            while (rs.next()) {
                if (userName.equals(rs.getString(2).trim()) && PasswordHasherSHA256.verifyPassword(passWord, rs.getString(3).trim())){
                    getID = rs.getInt(1);
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
        return -1;  // Nếu không đúng thì trả về -1
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
            String image = "src\\image\\Customer_Image\\Customer_default.png";
            String sql= "INSERT INTO UserAccount VALUES (" + IDMax + ", '" + userName + "', '" + passWord + "', '" + "Khách" + "')";
            String sql2 = "INSERT INTO Customer VALUES (" + IDMax + ", N'" + name + "', '" + sdt + "', " + 0 + "N'" + image + "'" + ")";
            stmt.executeUpdate(sql);
            stmt.executeUpdate(sql2);
            JOptionPane.showMessageDialog(null, "Đăng ký thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRoleFromID(int id){
        String role = "";
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM UserAccount WHERE ID = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                role = rs.getString(4);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return role;
    }

    public Employee getEmployeeFromID(int id){
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT UA.Username, UA.Password, UA.Role,"+// 
               "E.Name, E.Phone, E.hourWage, E.CCCD, E.BirthDate, E.Gender, E.Image" +//  
                
                " FROM UserAccount UA" + //
                " JOIN Employee E ON UA.ID = E.employeeID" +//
                " WHERE UA.ID = " + id;

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String username = rs.getString("Username");
                String password = rs.getString("Password");
                String role = rs.getString("Role");
                String name = rs.getString("Name");
                String phone = rs.getString("Phone");
                double hourlyWage = rs.getDouble("hourWage");
                String CCCD = rs.getString("CCCD");
                String birthDate = rs.getString("BirthDate");
                String gender = rs.getString("Gender");
                String image = rs.getString("Image");
                return new Employee(id, name, phone, image, username, password, role, CCCD, birthDate, gender, hourlyWage);
            }
            rs.close();
            stmt.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkEqualsUserName(String username){
        try{
            Statement stmt = conn.createStatement();
            String sql = "SELECT [username]\r\n" + //
                        "FROM [UserAccount]\r\n" + //
                        "WHERE [username] = '" + username + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false; // Không tồn tại
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
