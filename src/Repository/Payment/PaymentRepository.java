package Repository.Payment;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Customer;
import Model.Order;
import Model.Payment;
import Model.Product;
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
    

    @Override
    public List<Payment> getPaymentInDate(String startDate, String endDate) 
            throws SQLException, ClassNotFoundException {
        List<Payment> payments = new ArrayList<>();
        
        String sql = "SELECT paymentID, orderID, paymentMethod, amount, paymentTime " +
                     "FROM Payment " +
                     "WHERE paymentTime BETWEEN ? AND ?";

        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            // Chuyển đổi startDate và endDate từ String sang LocalDateTime
            LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00"); // Bắt đầu ngày
            LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");   // Kết thúc ngày
            stmt.setObject(1, start);
            stmt.setObject(2, end);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Payment payment = new Payment(
                            rs.getInt("paymentID"),
                            rs.getInt("orderID"),
                            rs.getString("paymentMethod"),
                            rs.getDouble("amount"),
                            rs.getString("paymentTime") // Lấy paymentTime dưới dạng String
                    );
                    payments.add(payment);
                }
            }
        }
        return payments;
    }
    
    public List<Order> getAllOrderInDate(String startDate, String endDate) throws SQLException, ClassNotFoundException {
        List<Order> orders = new ArrayList<>();
        Map<Integer, Order> orderMap = new HashMap<>(); // Sử dụng Map để nhóm theo orderID

        String sql = "SELECT P.paymentID, P.paymentMethod, P.amount, P.paymentTime, O.orderID, O.tableID, O.employeeID, O.orderTime, " +
                     "PD.productID, PD.name, PD.size, OD.price, PD.image, C.customerID, C.name as [customerName], C.phone, C.point " +
                     "FROM Payment P " +
                     "JOIN Orders O ON O.orderID = P.orderID " +
                     "JOIN OrderDetail OD ON OD.orderID = O.orderID " +
                     "JOIN Product PD ON PD.productID = OD.productID " +
                     "JOIN Customer C ON C.customerID = O.customerID " +
                     "WHERE P.paymentTime >= ? AND P.paymentTime <= ?";

        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            // Chuyển đổi startDate và endDate từ String sang LocalDateTime
            LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00"); // Bắt đầu ngày
            LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");   // Kết thúc ngày
            stmt.setObject(1, start);
            stmt.setObject(2, end);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("orderID");

                    // Nếu order chưa tồn tại trong map, tạo mới
                    Order order = orderMap.computeIfAbsent(orderId, k -> {
                        Order newOrder = new Order();
                        newOrder.setOrderID(orderId);
                        try {
							newOrder.setEmployeeID(rs.getInt("employeeID"));
							newOrder.setOrderTime(rs.getString("orderTime"));
							newOrder.setTableID(rs.getInt("tableID"));
							newOrder.setCustomerID(rs.getInt("customerID"));
							
							// Thêm thông tin Customer vào Order
	                        Customer customer = new Customer();
	                        customer.setId(rs.getInt("customerID"));
	                        customer.setName(rs.getString("customerName"));
	                        customer.setPhone(rs.getString("phone"));
	                        customer.setPoints(rs.getInt("point"));
	                        newOrder.setCustomer(customer); // Gán Customer vào Order
							
						} catch (SQLException e) {						
							e.printStackTrace();
						}
                        newOrder.setProducts(new HashMap<>());
                        newOrder.setPayments(new Payment());
                        
                        return newOrder;
                    });

                    // Cập nhật Payment
                    Payment payment = order.getPayments();
                    payment.setPaymentID(rs.getInt("paymentID"));
                    payment.setOrderID(orderId);
                    payment.setPaymentMethod(rs.getString("paymentMethod"));
                    payment.setAmount(rs.getDouble("amount"));
                    payment.setPaymentTime(rs.getString("paymentTime"));
                    
                    // Cập nhật Product và số lượng (giả định price đại diện cho sản phẩm)
                    int productId = rs.getInt("productID");
                    String productName = rs.getString("name");
                    double price = rs.getDouble("price");
                    String size = rs.getString("size");
                    String image = rs.getString("image");
                    Product product = new Product(productId, productName, price, size, image);
                    order.getProducts().merge(product, 1, Integer::sum); // Cộng dồn số lượng
                }
            }
        }

        // Chuyển từ Map sang List
        orders.addAll(orderMap.values());
        return orders;
    }
    
}
