package Model;

import java.util.Objects;

public class Product {
    private int productID;
    private String name;
    private double price;
    private String size;
    private String image;

    
    public Product() {
    }

    public Product(int productID, String name, double price, String size, String image) {
        this.productID = productID;
        this.name = name;
        this.size = size;
        this.price = price;
        this.image = image;
    }

    // Getter
    public int getProductID() { return productID; }
    public String getName() { return name; }
    public String getSize() { return size; }
    public double getPrice() { return price; }
    public String getImage() { return image; }


    // Setter
    public void setProductID(int productID) { this.productID = productID; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setSize(String size) { this.size = size; }
    public void setImage(String image) { this.image = image; }


    @Override
    public String toString() {
        return "Product{" +
                "productID=" + productID +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", size='" + size + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return Objects.equals(name, product.name);
    }

    // Chỉ tạo hashCode dựa trên name để đảm bảo Set nhận diện trùng lặp theo tên
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }



}

