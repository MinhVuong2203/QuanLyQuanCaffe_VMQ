package Entity;

public class Product {
    private int productID;
    private String name;
    private double price;

    public Product(int productID, String name, double price) {
        this.productID = productID;
        this.name = name;
        this.price = price;
    }

    // Getter
    public int getProductID() { return productID; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}
