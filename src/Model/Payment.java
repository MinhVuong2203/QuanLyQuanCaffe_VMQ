package Model;

import java.time.LocalDateTime;

public class Payment {
    private int paymentID;
    private int orderID;
    private String paymentMethod; // Tiền mặt, thẻ, ví điện tử
    private double amount;
    private LocalDateTime paymentTime;
   

    public Payment(int paymentID, int orderID, String paymentMethod, double amount, LocalDateTime paymentTime) {
        this.paymentID = paymentID;
        this.orderID = orderID;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentTime = paymentTime;
      
    }

    // Getter
    public int getPaymentID() { return paymentID; }
    public int getOrderID() { return orderID; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getAmount() { return amount; }
    public LocalDateTime getPaymentTime() { return paymentTime; }

    public Payment() {
        // Constructor rỗng
    }

}


    

