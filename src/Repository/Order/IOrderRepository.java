package Repository.Order;

import Model.Order;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface IOrderRepository {
    List<Order> getAllOrders() throws SQLException;
    public String getTimeByTableID(int tableID) throws SQLException;
    public List<Order> getOrdersBetweenDates(LocalDate fromDate, LocalDate toDate)
        throws SQLException, IOException, ClassNotFoundException;
}
