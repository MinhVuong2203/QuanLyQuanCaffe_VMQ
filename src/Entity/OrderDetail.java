package Entity;

public class OrderDetail {
    private Product product;
    private double price; // Giá của từng sản phẩm

    public OrderDetail(Product product) {
        this.product = product;
        this.price = product.getPrice(); // Lấy giá từ sản phẩm
    }
    // Getter
    public Product getProduct() { return product; }
    public double getPrice() { return price; }
}

