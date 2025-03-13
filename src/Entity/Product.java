package Entity;

public class Product {
    private int productID;
    private String name;
    private double price;
    private int categoryID;
    private String image;

    public Product() {

    }

    public Product(int productID, String name, double price, int categoryID, String image) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.categoryID = categoryID;
        this.image = image;
    }

    public int getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getImage() {
        return image;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product [productID = " + productID + ", name = " + name + ", price = " + price + ", categoryID = "
                + categoryID
                + ", image = " + image + "]";
    }
}
