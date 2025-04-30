package Repository.Order;

import Model.Order;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IOrderRepository {
    void addOrder(Order order) throws SQLException;
    void deleteOrder(int orderId, int tableId) throws SQLException;
    int getOrderIdByTableId(int tableId) throws SQLException;
    void updateOrder(Order order) throws SQLException;
    List<Order> getAllOrders() throws SQLException;
}
