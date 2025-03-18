package Entity;

import java.time.LocalDateTime;

public class Payment {
    private int paymentID;
    private int orderID;
    private String paymentMethod; // Tiền mặt, thẻ, ví điện tử
    private double amount;
    private LocalDateTime paymentTime;
    private String status; // "Success", "Failed", "Pending"
    private String transactionID; // Chỉ dùng cho thanh toán online

    public Payment(int paymentID, int orderID, String paymentMethod, double amount, LocalDateTime paymentTime, String status, String transactionID) {
        this.paymentID = paymentID;
        this.orderID = orderID;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentTime = paymentTime;
        this.status = status;
        this.transactionID = transactionID;
    }

    // Getter
    public int getPaymentID() { return paymentID; }
    public int getOrderID() { return orderID; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getAmount() { return amount; }
    public LocalDateTime getPaymentTime() { return paymentTime; }
    public String getStatus() { return status; }
    public String getTransactionID() { return transactionID; }
}

