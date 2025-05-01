package Repository.Order;

import Model.Order;
import java.sql.SQLException;
import java.util.List;

public interface IOrderRepository {
    List<Order> getAllOrders() throws SQLException;
    public String getTimeByTableID(int tableID) throws SQLException;
}
