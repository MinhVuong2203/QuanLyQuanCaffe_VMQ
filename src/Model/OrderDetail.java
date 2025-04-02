package Model;

public class OrderDetail {
    private Product product;
    private int quanlity; // Số lượng sản phẩm
    private double price; // Giá của từng sản phẩm

    public OrderDetail(Product product, int quanlity) {
        this.product = product;
        this.quanlity = quanlity; // Lấy số lượng từ sản phẩm
        this.price = product.getPrice(); // Lấy giá từ sản phẩm
    }
    // Getter
    public Product getProduct() { return product; }
    public int getQuanlity() { return quanlity; }
    public double getPrice() { return price; }
}

