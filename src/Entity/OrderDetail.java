package Entity;

public class OrderDetail {
    private Product product;
    private int quantity;
    private double price; // Giá của từng sản phẩm

    public OrderDetail(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.price = product.getPrice(); // Lấy giá từ sản phẩm
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    // Getter
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}

