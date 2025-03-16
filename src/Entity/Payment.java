package Entity;

public class Payment {
    private int paymentID;
    private int orderID;
    private String paymentMethod;
    private double amount;
    private double PaymentTime;
    public Payment(){}
    public Payment(int paymentID, int orderID, String paymentMethod, double amount, double PaymentTime) {
        this.paymentID = paymentID;
        this.orderID = orderID;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.PaymentTime = PaymentTime;
    }
    public int getPaymentID() {
        return paymentID;
    }
    public int getOrderID() {
        return orderID;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public double getAmount() {
        return amount;
    }
    public double getPaymentTime() {
        return PaymentTime;
    }
    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setPaymentTime(double PaymentTime) {
        this.PaymentTime = PaymentTime;
    }
    @Override
    public String toString() {
        return "Payment{" + "paymentID=" + paymentID + ", orderID=" + orderID + ", paymentMethod=" + paymentMethod + ", amount=" + amount + ", PaymentTime=" + PaymentTime + '}';
    }
}
