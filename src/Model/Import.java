package Model;
import java.util.Date;

public class Import {
    private int importID;
    private Ingredient ingredient;
    private int quantity;
    private double unitPrice;
    private Date importDate;

    public Import(int importID, Ingredient ingredient, int quantity, double unitPrice, Date importDate) {
        this.importID = importID;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.importDate = importDate;

        // Khi nhập hàng, tự động cập nhật tồn kho nguyên liệu
        this.ingredient.addStock(quantity);
    }

    // Getter
    public Ingredient getIngredient() { return ingredient; }
    public int getQuantity() { return quantity; }
}


