package Repository.Order;
import Model.Order;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class OrderRepository implements IOrderRepository {
    private JdbcUtils jdbcUtils;
    private Connection connection;
    public OrderRepository () throws IOException, ClassNotFoundException, SQLException {
        jdbcUtils = new JdbcUtils();
    }


    @Override
    public void addOrder(Order order) throws SQLException {
        try {
            connection = jdbcUtils.getConnection();
            String sql = "INSERT INTO Orders (orderId, tableId, employeeId, customerId, price) VALUES (" + order.getOrderId() + ", " + order.getTableId() + ", " + order.getEmployeeId() + ", " + order.getCustomerId() + ", '" + order.getTotalPaid() + "')";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbcUtils.closeConnection(connection);
        }
    }

    @Override
    public void deleteOrder(int orderId, int tableId) throws SQLException {
        try {
            connection = jdbcUtils.getConnection();
            String sql = "DELETE FROM Orders WHERE orderId = " + orderId + " AND tableId = " + tableId;
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbcUtils.closeConnection(connection);
        }
    }

    @Override
    public int getOrderIdByTableId(int tableId) throws SQLException {
        int orderId = 0;
        try {
            connection = jdbcUtils.getConnection();
            String sql = "SELECT orderId FROM Orders WHERE tableId = " + tableId;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                orderId = resultSet.getInt("orderId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbcUtils.closeConnection(connection);
        }
        return orderId; // Placeholder return value
    }

    @Override
    public void updateOrder(Order order) throws SQLException {
        try {
            connection = jdbcUtils.getConnection();
            String sql = "UPDATE Orders SET tableId = " + order.getTableId() + ", employeeId = " + order.getEmployeeId() + ", customerId = " + order.getCustomerId() + ", price = '" + order.getTotalPaid() + "' WHERE orderId = " + order.getOrderId();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbcUtils.closeConnection(connection);
        }
    }

    @Override
    public void getAllOrders() throws SQLException {
        ArrayList<Order> orders = new ArrayList<>();
        try {
            connection = jdbcUtils.getConnection();
            String sql = "SELECT * FROM Orders";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int orderId = resultSet.getInt("orderId");
                int tableId = resultSet.getInt("tableId");
                int employeeId = resultSet.getInt("employeeId");
                int customerId = resultSet.getInt("customerId");
                double price = resultSet.getDouble("price");
                Order order = new Order(orderId, tableId, employeeId, customerId, price);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbcUtils.closeConnection(connection);
        }
    }
    
}
