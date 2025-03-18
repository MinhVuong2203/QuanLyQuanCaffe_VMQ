package Entity;

import java.util.HashMap;
import java.util.Map;

public class Product {
    private int productID;
    private String name;
    private double price;
    private Map<Ingredient, Integer> ingredientList; // Nguyên liệu & số lượng cần cho 1 sản phẩm

    public Product(int productID, String name, double price) {
        this.productID = productID;
        this.name = name;
        this.price = price;
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
    public double getPrice() { return price; }
}

