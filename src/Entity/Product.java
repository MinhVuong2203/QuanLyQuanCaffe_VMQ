package Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Product {
    private int productID;
    private String name;
    private double price;
    private String size;
    private String image;

    private Map<Ingredient, Integer> ingredientList; // Nguyên liệu & số lượng cần cho 1 sản phẩm

    public Product() {
        this.ingredientList = new HashMap<>();
    }

    public Product(int productID, String name, double price, String size, String image) {
        this.productID = productID;
        this.name = name;
        this.size = size;
        this.price = price;
        this.image = image;
        this.ingredientList = new HashMap<>();
    }

    public void addIngredient(Ingredient ingredient, int quantity) {
        ingredientList.put(ingredient, quantity);
    }

    public boolean canMakeProduct() {
        for (Map.Entry<Ingredient, Integer> entry : ingredientList.entrySet()) {
            if (entry.getKey().getStockQuantity() < entry.getValue()) {
                return false; // Không đủ nguyên liệu
            }
        }
        return true;
    }

    public boolean makeProduct() {
        if (!canMakeProduct()) return false;

        // Trừ nguyên liệu sau khi làm sản phẩm
        for (Map.Entry<Ingredient, Integer> entry : ingredientList.entrySet()) {
            entry.getKey().useStock(entry.getValue());
        }
        return true;
    }

    // Getter
    public int getProductID() { return productID; }
    public String getName() { return name; }
    public String getSize() { return size; }
    public double getPrice() { return price; }
    public String getImage() { return image; }
    public Map<Ingredient, Integer> getIngredientList() { return ingredientList; }

    // Setter
    public void setProductID(int productID) { this.productID = productID; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setSize(String size) { this.size = size; }
    public void setImage(String image) { this.image = image; }
    public void setIngredientList(Map<Ingredient, Integer> ingredientList) { this.ingredientList = ingredientList; }

    @Override
    public String toString() {
        return "Product{" +
                "productID=" + productID +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", size='" + size + '\'' +
                ", image='" + image + '\'' +
                ", ingredientList=" + ingredientList +
                '}';
    }

    public void addIngredient(int aInt, String string, String string0, int aInt0, int aInt1) {
        Ingredient ingredient = new Ingredient(aInt, string, string0, aInt0);
        this.ingredientList.put(ingredient, aInt1); // Add ingredient to
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

