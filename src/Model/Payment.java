package Model;

import java.time.LocalDateTime;

public class Payment {
    private int paymentID;
    private int orderID;
    private String paymentMethod; // Tiền mặt, chuyển khoản
    private double amount;
    private String paymentTime;
   

    public Payment(int paymentID, int orderID, String paymentMethod, double amount, String paymentTime) {
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
    public String getPaymentTime() { return paymentTime; }

    
    
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

	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}

	public Payment() {
        // Constructor rỗng
    }

	@Override
	public String toString() {
		return "Payment [paymentID=" + paymentID + ", orderID=" + orderID + ", paymentMethod=" + paymentMethod
				+ ", amount=" + amount + ", paymentTime=" + paymentTime + "]";
	}
    
    

}


    

