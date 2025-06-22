package Repository.Payment;

import java.sql.SQLException;
import java.util.List;

import Model.Order;
import Model.Payment;

public interface IPaymentRepository {
    public int addPayment(int orderID, String paymentMethod, double amount, String paymentTime) 
            throws SQLException, ClassNotFoundException;
    public List<Payment> getPaymentInDate(String startDate, String endDate) 
            throws SQLException, ClassNotFoundException;
    
    public List<Order> getAllOrderInDate(String startDate, String endDate)  throws SQLException, ClassNotFoundException;
    
}
