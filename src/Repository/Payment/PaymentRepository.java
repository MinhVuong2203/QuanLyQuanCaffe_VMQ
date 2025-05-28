package Repository.Payment;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Utils.JdbcUtils;

public class PaymentRepository implements IPaymentRepository {
    private final JdbcUtils jdbcUtils;
    
    public PaymentRepository() throws ClassNotFoundException, IOException, SQLException {
        this.jdbcUtils = new JdbcUtils();
    }

    private int getMaxPaymentID() throws SQLException {
        String sql = "SELECT MAX(paymentID) FROM Payment";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
    
    @Override
    public int addPayment(int orderID, String paymentMethod, double amount, String paymentTime) 
            throws SQLException, ClassNotFoundException {
        
        // Lấy paymentID lớn nhất và tăng thêm 1
        int paymentID = getMaxPaymentID() + 1;
        
        String sql = "INSERT INTO Payment (paymentID, orderID, paymentMethod, amount, paymentTime) "
                   + "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, paymentID);
            stmt.setInt(2, orderID);
            stmt.setString(3, paymentMethod);
            stmt.setDouble(4, amount);
            stmt.setString(5, paymentTime);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Đã thêm thanh toán: ID=" + paymentID + ", orderID=" + orderID + 
                               ", paymentMethod=" + paymentMethod + ", amount=" + amount);
                return paymentID;
            } else {
                System.out.println("Không thể thêm thanh toán cho đơn hàng " + orderID);
                return -1;
            }
        }
    }
}
