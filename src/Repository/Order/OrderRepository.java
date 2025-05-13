package Repository.Order;

import Model.Order;
import Model.Product;
import Repository.Product.ProductRespository;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderRepository implements IOrderRepository {
    private final JdbcUtils jdbcUtils;
    private final ProductRespository productRepository;

    public OrderRepository() throws IOException, ClassNotFoundException, SQLException {
        this.jdbcUtils = new JdbcUtils();
        this.productRepository = new ProductRespository();
    }

    @Override
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT orderID, employeeID, customerID, tableID, status FROM Orders";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int orderID = rs.getInt("orderID");
                int employeeID = rs.getInt("employeeID");
                int customerID = rs.getInt("customerID");
                int tableID = rs.getInt("tableID");
                String status = rs.getString("status");
                Map<Product, Integer> products;
				try {
					products = productRepository.getProductsByOrderID(orderID);
					Order order = new Order(orderID, employeeID, customerID, tableID, status, products);
					orderList.add(order);
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
        }
        return orderList;
    }

    @Override
    public String getTimeByTableID(int tableID) throws SQLException {
        if (tableID <= 0) {
            throw new IllegalArgumentException("ID bàn không hợp lệ");
        }
        String sql = "SELECT orderTime FROM Orders WHERE tableID = ? AND status = N'Đang chuẩn bị'";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, tableID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("orderTime");
                }
            }
        }
        return null;
    }

    @Override
    public List<Order> getOrdersBetweenDates(LocalDate fromDate, LocalDate toDate) throws SQLException {
        if (fromDate == null || toDate == null || fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("Ngày không hợp lệ");
        }
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT orderID, employeeID, customerID, tableID, status FROM Orders WHERE orderTime BETWEEN ? AND ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(fromDate));
            stmt.setDate(2, Date.valueOf(toDate.plusDays(1))); // Bao gồm cả ngày cuối
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int orderID = rs.getInt("orderID");
                    int employeeID = rs.getInt("employeeID");
                    int customerID = rs.getInt("customerID");
                    int tableID = rs.getInt("tableID");
                    String status = rs.getString("status");
                    Map<Product, Integer> products = productRepository.getProductsByOrderID(orderID);
                    Order order = new Order(orderID, employeeID, customerID, tableID, status, products);
                    orderList.add(order);
                }
            }
        }
        return orderList;
    }

    @Override
    public void updateOrderDiscount(int orderId, double discountAmount) throws SQLException {
        if (orderId <= 0 || discountAmount < 0) {
            throw new IllegalArgumentException("ID đơn hàng hoặc số tiền giảm giá không hợp lệ");
        }
        String sql = "UPDATE Orders SET Discount = ? WHERE orderID = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, discountAmount);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        }
    }
}