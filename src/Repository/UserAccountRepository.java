package Repository;

import Model.Customer;
import Model.Employee;
import Utils.ConvertInto;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;


public class UserAccountRepository {
    private Connection connection;
    private JdbcUtils jdbcUtils;

    public UserAccountRepository() throws IOException, ClassNotFoundException, SQLException {
        this.jdbcUtils = new JdbcUtils();
    }

    public int login(String userName, String passWord) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM UserAccount";  // Lấy tất cả bản nhân viên
            ResultSet rs = stmt.executeQuery(sql);
            int getID = -1; // Lấy ID đúng để biết là ai đăng nhập
            boolean check = false;  
            while (rs.next()) {
                if (userName.equals(rs.getString(2).trim()) && ConvertInto.verifyPassword(passWord, rs.getString(3).trim())){
                    getID = rs.getInt(1);
                    rs.close();
                    stmt.close();
                    return getID;
                }
            }
            // Nếu không đúng giá trị thì thông báo lỗi
            // JOptionPane.showMessageDialog(null, "Sai tên đăng nhập hoặc mật khẩu", "Thông báo", JOptionPane.ERROR_MESSAGE);
            rs.close();
            stmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
			connection.close();
		}
        return -1;  // Nếu không đúng thì trả về -1
    }

    public int getIDMaxFromSQL() throws SQLException{
        int id = 0;
        try{
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT MAX(ID) FROM UserAccount";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                id = rs.getInt(1);
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			connection.close();
		}
        return id;
    }

    public void signUp(String name, String sdt, String userName, String passWord) throws SQLException {
        try{
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
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
        } finally {
			connection.close();
		}
    }

    public String getRoleFromID(int id) throws SQLException{
        String role = "";
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM UserAccount WHERE ID = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                role = rs.getString(4);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			connection.close();
		}
        return role;
    }

    public Employee getEmployeeFromID(int id) throws SQLException{
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT UA.username, UA.password, UA.role,"+// 
               "E.name, E.phone, E.hourWage, E.CCCD, E.birthDate, E.gender, E.image" +//  
                " FROM UserAccount UA" + //
                " JOIN Employee E ON UA.ID = E.employeeID" +//
                " WHERE UA.ID = " + id;

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");
                System.out.println("Hihi " + role);
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                double hourlyWage = rs.getDouble("hourWage");
                String CCCD = rs.getString("CCCD");
                String birthDate = rs.getString("birthDate");
                String gender = rs.getString("gender");
                String image = rs.getString("image");
                return new Employee(id, name, phone, image, username, password, role, CCCD, birthDate, gender, hourlyWage);
            }
            rs.close();
            stmt.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			connection.close();
		}
        return null;
    }

    public boolean checkEqualsUserName(String username) throws SQLException{
        try{
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT [username]\r\n" + //
                        "FROM [UserAccount]\r\n" + //
                        "WHERE [username] = '" + username + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return true;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
			connection.close();
		}
        return false; // Không tồn tại
    }

    public Customer getCustomerFromID(int id) throws SQLException {
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT U.ID, C.name, C.phone, C.image, U.username, U.password, U.role, C.point \r\n" + //
                                "FROM UserAccount AS U\r\n" + //
                                "JOIN Customer AS C ON U.ID = C.customerID\r\n" + //
                                "WHERE U.ID = " + id;
            
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String image = rs.getString("image");
                double cost = rs.getDouble("point");
                String username = rs.getString("username");
                String password = rs.getString("password");
            return new Customer(id, name, phone, image, username, password, cost);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			connection.close();
		}
        return null; // Không tồn tại
    }

    public void updatePoint(int id, double pointNew) throws SQLException {
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "UPDATE Customer SET point = " + pointNew + " WHERE customerID = " + id;
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

}
