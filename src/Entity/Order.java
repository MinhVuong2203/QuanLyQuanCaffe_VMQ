package Entity;

import java.util.Date;

public class Order {
    private int orderID;
    private int userID;
    private int customerID;
    private double totalPrice;
    private Date orderDate;

    public Order() {

    }

    public Order(int orderID, int userID, int customerID, double totalPrice, Date orderDate) {
        this.orderID = orderID;
        this.userID = userID;
        this.customerID = customerID;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getUserID() {
        return userID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order [orderID = " + orderID + ", userID = " + userID + ", customerID = " + customerID
                + ", totalPrice = "
                + totalPrice + ", orderDate = " + orderDate + "]";
    }
}
