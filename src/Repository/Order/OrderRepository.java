package Repository.Order;
import Model.Order;
import Repository.Product.ProductRespository;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class OrderRepository implements IOrderRepository {

   
    private JdbcUtils jdbcUtils;
    private Connection connection;
    public OrderRepository () throws IOException, ClassNotFoundException, SQLException {
        jdbcUtils = new JdbcUtils();
    }
    @Override
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orderList = new ArrayList<>();
        ProductRespository productRepository;
        try {
            connection = jdbcUtils.connect();
            productRepository = new ProductRespository(); // tạo 1 lần

            String sql = "SELECT * FROM OrderDetails";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int orderID = resultSet.getInt("orderID");
                int employeeID = resultSet.getInt("employeeID");
                int customerID = resultSet.getInt("customerID");
                int tableID = resultSet.getInt("tableID");
                String status = resultSet.getString("status");

                // int productID = resultSet.getInt("productID");
                // int quantity = resultSet.getInt("quantity");

                // Map<Product, Integer> products = new HashMap<>();
                // Product product = productRepository.getProductByID(productID);
                // if (product != null) {
                //     products.put(product, quantity);
                // }
                
                Order order = new Order(orderID, employeeID, customerID, tableID, status);
                orderList.add(order);
            }

            statement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.close();
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
   

}
