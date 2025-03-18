package Entity;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderID;
    private int employeeID;
    private int customerID;
    private double totalPrice;
    private String status; // "Pending", "Paid", "Cancelled"
    private List<Payment> payments;

    public Order(int orderID, int employeeID, int customerID, double totalPrice) {
        this.orderID = orderID;
        this.employeeID = employeeID;
        this.customerID = customerID;
        this.totalPrice = totalPrice;
        this.status = "Pending";
        this.payments = new ArrayList<>();
    }

    public boolean addPayment(Payment payment) {
        if (getTotalPaid() + payment.getAmount() > totalPrice) {
            System.out.println("⚠ Số tiền thanh toán vượt quá tổng đơn hàng!");
            return false;
        }
        payments.add(payment);

        if (getTotalPaid() == totalPrice) {
            this.status = "Paid"; // Đơn hàng được thanh toán đủ
        }

        return true;
    }

    public double getTotalPaid() {
        return payments.stream().mapToDouble(Payment::getAmount).sum();
    }

    public boolean isFullyPaid() {
        return getTotalPaid() >= totalPrice;
    }

    // Getter
    public int getOrderID() { return orderID; }
    public String getStatus() { return status; }
    public List<Payment> getPayments() { return payments; }
}


