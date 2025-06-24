package Repository.Order;

import Model.Order;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IOrderRepository {
    List<Order> getAllOrders() throws SQLException;

    public String getTimeByTableID(int tableID) throws SQLException;

     public List<Map<Order,Integer>>  getOrdersBetweenDates(LocalDate fromDate, LocalDate toDate)
            throws SQLException, IOException, ClassNotFoundException;

    public void updateOrderDiscount(int orderId, double discountAmount)
            throws SQLException, ClassNotFoundException, IOException;
    
    public Order getOrderByOrderID(int orderId)
            throws SQLException, ClassNotFoundException, IOException;
    
//    public Map<Product, Integer> getProductsByOrderID(int orderID) throws SQLException;
}
