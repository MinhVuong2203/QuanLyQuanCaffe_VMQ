package Model;

import java.util.HashMap;
import java.util.Map;

public class Order {
    // Phương thức để khởi tạo
    private int orderID;
    private int employeeID;
    private int customerID;
    private int tableID; // Nếu tableID = 0 thì là đơn hàng giao hàng

    private String status; // "Đang chờ", "Đã thanh toán", "Đã hủy"

    // Phương thức để add
    Map<Product, Integer> products; // Sản phẩm và số lượng
    private Payment payments;

    public Order() {
    }

    public Order(int orderID, int employeeID, int customerID, int tableID, String status) {
        this.orderID = orderID;
        this.employeeID = employeeID;
        this.customerID = customerID;

        this.status = status;
        this.products = new HashMap<>();
    }

    // Thêm sản phẩm vào đơn hàng
    public void addOderDeTailProduct(Product product, int quantity) {
        products.put(product, products.getOrDefault(product, 0) + quantity);
    }

    // Tính tổng tiền của đơn hàng
    public void getTotalPrice() {
        double totalPrice = 0;
        for (Map.Entry<Product, Integer> entry : this.products.entrySet()) {
            totalPrice = entry.getKey().getPrice() * entry.getValue();
            System.out.println(entry.getKey().getName() + " " + entry.getKey().getSize() + ": " + totalPrice);
        }
    }

    // Thêm Thanh toán cho đơn hàng
    public void addPayment(Payment payment) {
        this.payments = payment;
    }

    public void cancelOrder() {
        if (!this.status.equals("Đang chờ")) {
            System.out.println("Không thể hủy đơn hàng (đã hoàn tất hoặc đã hủy).");
            return;
        }
        this.status = "Đã hủy";
        System.out.println("Đơn hàng #" + orderID + " đã bị hủy.");
    }

    public void completeOrder() {
        if (!this.status.equals("Đang chờ")) {
            System.out.println("Không thể hoàn tất đơn hàng (đã hủy hoặc đã hoàn tất).");
            return;
        }
        this.status = "Đã thanh toán";
        System.out.println("Đơn hàng #" + orderID + " đã được thanh toán.");
    }

    public double getTotalPaid() {
        return payments.getAmount();
    }

    public int getOrderID() {
        return orderID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getStatus() {
        return status;
    }

    public Payment getPayments() {
        return payments;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public int getTableID() {
        return tableID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }

    public void setPayments(Payment payments) {
        this.payments = payments;
    }
}
