package Repository.Payment;

import java.sql.SQLException;

public interface IPaymentRepository {
    public int addPayment(int orderID, String paymentMethod, double amount, String paymentTime) 
            throws SQLException, ClassNotFoundException;
}
