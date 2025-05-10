package Repository.UserAccount;

import Model.Customer;
import Model.Employee;
import Model.Manager;
import Model.User;
import Utils.ConvertInto;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class UserAccountRepository implements IUserAccountRepository {
    private Connection connection;
    private JdbcUtils jdbcUtils;

    public UserAccountRepository() throws IOException, ClassNotFoundException, SQLException {
        this.jdbcUtils = new JdbcUtils();
        
    }

    public User login(String userName, String passWord) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            Statement stmt = connection.createStatement();
            passWord = ConvertInto.hashPassword(passWord);
            String sql = "SELECT * FROM UserAccount WHERE username = '" + userName + "' AND password = '" + passWord + "'";  // Lấy tất cả bản nhân viên
            ResultSet rs = stmt.executeQuery(sql);
        
            if (rs.next()) {
//                if (userName.equals(rs.getString(2).trim()) && ConvertInto.verifyPassword(passWord, rs.getString(3).trim())){
                    int getID = rs.getInt(1);
                    String getRole = rs.getString(4);
                    rs.close();
                    stmt.close();
                    return new User(getID,"", "", "", userName,  passWord, getRole);
                }
            
            rs.close();
            stmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
			connection.close();
		}
        return null;  // Nếu không đúng thì trả về null
    }

    public int getIDMaxFromSQL() throws SQLException{ 
        int id = 0;
        try{
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT MAX(ID) FROM UserAccount"; // Lấy ID lớn nhất trong bảng
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

    
    public void signUp(String name, String sdt) throws SQLException {  // Đây
        try{
            connection = jdbcUtils.connect(); // Phải có để có connection
            String sql = "INSERT INTO Customer VALUES (?, ?, ?, ?)"; 
            PreparedStatement pstmt = connection.prepareStatement(sql);
            int IDMax = this.getIDMaxFromSQL() + 1; // Lấy ID lớn nhất trong database và cộng thêm 1 để tạo một id mới
            pstmt.setInt(1, IDMax);         
            pstmt.setString(2, name);       
            pstmt.setString(3, sdt);      
            pstmt.setInt(4, 0); 
            pstmt.executeUpdate(); // Thực hiện câu lệnh SQL
            pstmt.close();

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
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                double hourlyWage = rs.getDouble("hourWage");
                String CCCD = rs.getString("CCCD");
                String birthDate = rs.getString("birthDate");
                String gender = rs.getString("gender");
                String image = rs.getString("image");
                System.out.println(image);
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

    public Manager getManagerFromID(int id) throws SQLException{
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT UA.username, UA.password,"+// 
               "E.name, E.phone, E.hourWage, E.CCCD, E.birthDate, E.gender, E.image" +//  
                " FROM UserAccount UA" + //
                " JOIN Employee E ON UA.ID = E.employeeID" +//
                " WHERE UA.ID = " + id;

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                double hourlyWage = rs.getDouble("hourWage");
                String CCCD = rs.getString("CCCD");
                String birthDate = rs.getString("birthDate");
                String gender = rs.getString("gender");
                String image = rs.getString("image");
                return new Manager(id, name, phone, image, username, password, CCCD, birthDate, gender, hourlyWage);
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
                double point = rs.getDouble("point");
            return new Customer(id, name, phone, point);
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
