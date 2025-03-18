package Entity;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderID;
    private int employeeID; // Người tạo đơn
    private int customerID; // Khách hàng (hoặc null nếu là khách vãng lai)
    private double totalPrice;
    private String status; // "Pending", "Paid", "Cancelled"
    private List<OrderDetail> orderDetails;

    public Order(int orderID, int employeeID, int customerID) {
        this.orderID = orderID;
        this.employeeID = employeeID;
        this.customerID = customerID;
        this.totalPrice = 0.0;
        this.status = "Pending";
        this.orderDetails = new ArrayList<>();
    }

    public void addOrderDetail(OrderDetail detail) {
        orderDetails.add(detail);
        totalPrice += detail.getTotalPrice(); // Cập nhật tổng tiền
    }

    public void removeOrderDetail(int productID) {
        orderDetails.removeIf(detail -> detail.getProduct().getProductID() == productID);
        recalculateTotalPrice();
    }

    private void recalculateTotalPrice() {
        totalPrice = 0.0;
        for (OrderDetail detail : orderDetails) {
            totalPrice += detail.getTotalPrice();
        }
    }

    public void completeOrder() {
        this.status = "Paid";
    }

    public void cancelOrder() {
        this.status = "Cancelled";
    }

    // Getter
    public int getOrderID() { return orderID; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public List<OrderDetail> getOrderDetails() { return orderDetails; }
}

