package Model;

public class Ingredient {
    private int ingredientID;
    private String name;
    private String unit;  // Ví dụ: gram, ml, cái
    private int stockQuantity; // Số lượng tồn kho

    public Ingredient(int ingredientID, String name, String unit, int stockQuantity) {
        this.ingredientID = ingredientID;
        this.name = name;
        this.unit = unit;
        this.stockQuantity = stockQuantity;
    }

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public boolean useStock(int quantity) {
        if (stockQuantity >= quantity) {
            stockQuantity -= quantity;
            return true;
        }
        return false;
    }

    // Getter
    public int getIngredientID() { return ingredientID; }
    public String getName() { return name; }
    public int getStockQuantity() { return stockQuantity; }

    // Setter
    public void setIngredientID(int ingredientID) { this.ingredientID = ingredientID; }
    public void setName(String name) { this.name = name; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "ingredientID=" + ingredientID +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
    
}

