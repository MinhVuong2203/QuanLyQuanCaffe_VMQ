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

public class UserAccountRepository implements IUserAccountRepository {
    private final JdbcUtils jdbcUtils;

    public UserAccountRepository() throws IOException, ClassNotFoundException, SQLException {
        this.jdbcUtils = new JdbcUtils();
    }

    @Override
    public User login(String userName, String passWord) throws SQLException {
        if (userName == null || userName.isEmpty() || passWord == null || passWord.isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không hợp lệ");
        }
        String sql = "SELECT ID, username, password, role FROM UserAccount WHERE username = ? AND password = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.setString(2, ConvertInto.hashPassword(passWord));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("ID"),
                        "", "", "", // Các trường không sử dụng
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public int getIDMaxFromSQL() throws SQLException {
        String sql = "SELECT MAX(ID) FROM UserAccount";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public void signUp(String name, String sdt) throws SQLException {
        if (name == null || name.isEmpty() || sdt == null || sdt.isEmpty()) {
            throw new IllegalArgumentException("Tên hoặc số điện thoại không hợp lệ");
        }
        String sql = "INSERT INTO Customer (customerID, name, phone, point) VALUES (?, ?, ?, ?)";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int idMax = getIDMaxFromSQL() + 1;
            pstmt.setInt(1, idMax);
            pstmt.setString(2, name);
            pstmt.setString(3, sdt);
            pstmt.setDouble(4, 0);
            pstmt.executeUpdate();
        }
    }

   
    public String getRoleFromID(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID không hợp lệ");
        }
        String sql = "SELECT role FROM UserAccount WHERE ID = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        }
        return "";
    }

    @Override
    public Employee getEmployeeFromID(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID không hợp lệ");
        }
        String sql = """
                SELECT UA.username, UA.password, UA.role, E.name, E.phone, E.hourWage, E.CCCD, E.birthDate, E.gender, E.image
                FROM UserAccount UA
                JOIN Employee E ON UA.ID = E.employeeID
                WHERE UA.ID = ?
                """;
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                        id,
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("image"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("CCCD"),
                        rs.getString("birthDate"),
                        rs.getString("gender"),
                        rs.getDouble("hourWage")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public Manager getManagerFromID(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID không hợp lệ");
        }
        String sql = """
                SELECT UA.username, UA.password, E.name, E.phone, E.hourWage, E.CCCD, E.birthDate, E.gender, E.image
                FROM UserAccount UA
                JOIN Employee E ON UA.ID = E.employeeID
                WHERE UA.ID = ?
                """;
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Manager(
                        id,
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("image"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("CCCD"),
                        rs.getString("birthDate"),
                        rs.getString("gender"),
                        rs.getDouble("hourWage")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public boolean checkEqualsUserName(String username) throws SQLException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không hợp lệ");
        }
        String sql = "SELECT username FROM UserAccount WHERE username = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    @Override
    public boolean checkEqualCCCD(int id, String cccd) throws SQLException{
        String sql = "SELECT CCCD FROM employee WHERE employeeID = ? AND CCCD = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, cccd);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    
    @Override
    public void fixPassword(int id, String passwordNew) throws SQLException{
    	passwordNew = ConvertInto.hashPassword(passwordNew);
    	String sql = "UPDATE UserAccount SET password = ? WHERE ID = ?";
    	try(
    		Connection connection = jdbcUtils.connect();
    		PreparedStatement stmt = connection.prepareStatement(sql)
    		){
    			stmt.setString(1, passwordNew);
    			stmt.setInt(2, id);
    			stmt.executeUpdate();
	
    		}
    		
    }
    
    @Override
    public int getIDFromUsername(String username) throws SQLException{
    	String sql = "SELECT ID FROM UserAccount WHERE username = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                	return rs.getInt("ID");
                }
            }
        }
        return -1;
    }
    

    @Override
    public Customer getCustomerFromID(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID không hợp lệ");
        }
        String sql = """
                SELECT U.ID, C.name, C.phone, C.point
                FROM UserAccount U
                JOIN Customer C ON U.ID = C.customerID
                WHERE U.ID = ?
                """;
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                        id,
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getDouble("point")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void updatePoint(int id, double pointNew) throws SQLException {
        if (id <= 0 || pointNew < 0) {
            throw new IllegalArgumentException("ID hoặc điểm không hợp lệ");
        }
        String sql = "UPDATE Customer SET point = ? WHERE customerID = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, pointNew);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }
}