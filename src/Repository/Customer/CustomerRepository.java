package Repository.Customer;

import Model.Customer;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRepository implements ICustomerRespository {
    private final JdbcUtils jdbcUtils;

    public CustomerRepository() throws IOException, ClassNotFoundException, SQLException {
        this.jdbcUtils = new JdbcUtils();
    }

    @Override
    public int getIDMaxFromSQL() throws SQLException {
        String sql = "SELECT MAX(customerID) FROM Customer";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0; // Không có ID nào
    }

    @Override
    public boolean checkEqualsPhone(String phone) throws SQLException {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        String sql = "SELECT phone FROM Customer WHERE phone = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Có tồn tại nếu rs.next() trả về true
            }
        }
    }

    @Override
    public void addCustomer(String name, String phone) throws SQLException {
        if (name == null || name.isEmpty() || phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Tên hoặc số điện thoại không hợp lệ");
        }
        String sqlMaxID = "SELECT MAX(customerID) FROM Customer";
        String sqlInsert = "INSERT INTO Customer (customerID, name, phone, point) VALUES (?, ?, ?, ?)";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmtMaxID = connection.prepareStatement(sqlMaxID);
             ResultSet rs = stmtMaxID.executeQuery()) {
            int id = 10000;
            if (rs.next()) {
                id = rs.getInt(1) + 1;
            }
            try (PreparedStatement stmtInsert = connection.prepareStatement(sqlInsert)) {
                stmtInsert.setInt(1, id);
                stmtInsert.setString(2, name);
                stmtInsert.setString(3, phone);
                stmtInsert.setDouble(4, 30.0);
                stmtInsert.executeUpdate();
            }
        }
    }

    @Override
    public int getCustomerIDByPhone(String phone) throws SQLException {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        String sql = "SELECT customerID FROM Customer WHERE phone = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("customerID");
                }
            }
        }
        return 0; // Không có ID nào
    }

    @Override
    public double plusPoint(int customerID, double money) throws SQLException {
        if (money < 0) {
            throw new IllegalArgumentException("Số tiền không hợp lệ");
        }
        String sql = "UPDATE Customer SET point = point + ? WHERE customerID = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, money / 100000);
            stmt.setInt(2, customerID);
            stmt.executeUpdate();
            // Trả về số điểm đã cộng
            return money / 100000;
        }
    }

    @Override
    public Customer getCustomerByPhone(String phone) throws SQLException {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        String sql = "SELECT customerID, name, phone, point FROM Customer WHERE phone = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("customerID");
                    String name = rs.getString("name");
                    double point = rs.getDouble("point");
                    return new Customer(id, name, phone, point);
                }
            }
        }
        return null; // Không tìm thấy khách hàng
    }

    @Override
    public void updatePoint(int customerID, double newPoints) throws SQLException {
        if (newPoints < 0) {
            throw new IllegalArgumentException("Điểm không hợp lệ");
        }
        String sql = "UPDATE Customer SET point = ? WHERE customerID = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, newPoints);
            stmt.setInt(2, customerID);
            stmt.executeUpdate();
        }
    }
}