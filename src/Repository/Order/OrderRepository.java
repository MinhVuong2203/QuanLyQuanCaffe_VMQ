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
import java.util.HashMap;
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
       public List<Map<Order,Integer>>  getOrdersBetweenDates(LocalDate fromDate, LocalDate toDate) throws SQLException , IOException, ClassNotFoundException {
        if (fromDate == null || toDate == null || fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("Ngày không hợp lệ");
        }
        List<Map<Order, Integer>> orderList = new ArrayList<>();
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
                    int discount = rs.getInt("Discount");
                    Map<Product, Integer> products = productRepository.getProductsByOrderID(orderID);
                    Order order = new Order(orderID, employeeID, customerID, tableID, status, products);
                    Map<Order, Integer> orderDiscountMap = new java.util.HashMap<>();
                    orderDiscountMap.put(order, discount);
                    orderList.add(orderDiscountMap);
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
    
    @Override
    public Order getOrderByOrderID(int orderID)
            throws SQLException, ClassNotFoundException, IOException{
    	 String sql = "SELECT orderID, employeeID, customerID, tableID, status FROM Orders WHERE orderID = " + orderID;
         try (Connection connection = jdbcUtils.connect();
              PreparedStatement stmt = connection.prepareStatement(sql);
              ResultSet rs = stmt.executeQuery()) {
             if (rs.next()) {
                 int employeeID = rs.getInt("employeeID");
                 int customerID = rs.getInt("customerID");
                 int tableID = rs.getInt("tableID");
                 String status = rs.getString("status");
                 Map<Product, Integer> products;
 				try {
 					products = productRepository.getProductsByOrderID(orderID);
 					Order order = new Order(orderID, employeeID, customerID, tableID, status, products);		
 					return order;
 				} catch (SQLException e) {
 					e.printStackTrace();
 				}
             }
         }   	
         return null;
    }
    
//    @Override
//    public Map<Product, Integer> getProductsByOrderID(int orderID) throws SQLException{
//    	Map<Product, Integer> products = new HashMap<>();
//    	 String sql = """
//                 SELECT p.productID, p.name, p.price, p.size, p.image, od.quantity
//                 FROM OrderDetail od
//                 JOIN Product p ON od.productID = p.productID
//                 WHERE od.orderID = ?
//                 """;
//         try (Connection connection = jdbcUtils.connect();
//                 PreparedStatement stmt = connection.prepareStatement(sql)) {
//             stmt.setInt(1, orderID);
//             try (ResultSet rs = stmt.executeQuery()) {
//                 while (rs.next()) {
//                     Product product = new Product();
//                     product.setProductID(rs.getInt("productID"));
//                     product.setName(rs.getString("name"));
//                     product.setPrice(rs.getDouble("price"));
//                     product.setSize(rs.getString("size"));
//                     product.setImage(rs.getString("image"));
//                     int quantity = rs.getInt("quantity");
//                     products.put(product, quantity);
//                 }
//             }
//         }
//         return products; 	    	
//    }
}