package Repository.Order;

import Model.Order;
import Model.Product;
import Repository.Product.ProductRespository;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderRepository implements IOrderRepository {

    private JdbcUtils jdbcUtils;
    private Connection connection;

    public OrderRepository() throws IOException, ClassNotFoundException, SQLException {
        jdbcUtils = new JdbcUtils();
    }

    @Override
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orderList = new ArrayList<>();
        ProductRespository productRepository;
        try {
            connection = jdbcUtils.connect();
            productRepository = new ProductRespository(); // tạo 1 lần

            String sql = "SELECT * FROM Orders";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int orderID = resultSet.getInt("orderID");
                int employeeID = resultSet.getInt("employeeID");
                int customerID = resultSet.getInt("customerID");
                int tableID = resultSet.getInt("tableID");
                String status = resultSet.getString("status");
                Map<Product, Integer> products = productRepository.getProductsByOrderID(orderID); // Lấy danh sách sản
                                                                                                  // phẩm theo orderID

                // int productID = resultSet.getInt("productID");
                // int quantity = resultSet.getInt("quantity");

                // Map<Product, Integer> products = new HashMap<>();
                // Product product = productRepository.getProductByID(productID);
                // if (product != null) {
                // products.put(product, quantity);
                // }

                Order order = new Order(orderID, employeeID, customerID, tableID, status, products);
                orderList.add(order);
            }

            statement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.close();
        }
        return orderList;

    }

    @Override
    public String getTimeByTableID(int tableID) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String sql = "SELECT orderTime FROM Orders WHERE tableID = " + tableID + " AND status = N'Đang chuẩn bị'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getString("orderTime");
            }
            statement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> getOrdersBetweenDates(LocalDate fromDate, LocalDate toDate)
            throws SQLException, IOException, ClassNotFoundException {

        List<Order> orderList = new ArrayList<>();
        ProductRespository productRepository = new ProductRespository();

        String sql = "SELECT * FROM Orders WHERE orderTime BETWEEN ? AND ?";

        try (Connection conn = jdbcUtils.connect();
                PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setDate(1, java.sql.Date.valueOf(fromDate));
            statement.setDate(2, java.sql.Date.valueOf(toDate));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int orderID = resultSet.getInt("orderID");
                    int employeeID = resultSet.getInt("employeeID");
                    int customerID = resultSet.getInt("customerID");
                    int tableID = resultSet.getInt("tableID");
                    String status = resultSet.getString("status");

                    Map<Product, Integer> products = productRepository.getProductsByOrderID(orderID);

                    Order order = new Order(orderID, employeeID, customerID, tableID, status, products);
                    orderList.add(order);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw e; // Bắn lại cho nơi gọi xử lý
        } catch (Exception e) {
            e.printStackTrace(); // Log các lỗi runtime bất ngờ
        }

        return orderList;
    }

    public void updateOrderDiscount(int orderId, double discountAmount) throws SQLException, ClassNotFoundException, IOException {
        try {
            connection = jdbcUtils.connect();
            String sql = "UPDATE Orders SET Discount = ? WHERE orderID = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDouble(1, discountAmount);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

}