package Entity;
import java.util.Date;

public class ImportEntity {
    private int ingredientID;
    private String name;
    private float unit;
    private int quantity;
    private double unitPrice;
    private Date imporDate;
    double totalCost;

    public ImportEntity(){}

    public ImportEntity(int ingredientID, String name, float unit, int quantity, double unitPrice, Date imporDate) {
        this.ingredientID = ingredientID;
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.imporDate = imporDate;
        this.totalCost = unitPrice * quantity;
    }

    public int getIngredientID() {
        return ingredientID;
    }

    public String getName() {
        return name;
    }

    public float getUnit() {
        return unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
    public Date getImporDate() {
        return imporDate;
    }
    public double getTotalCost() {
        return totalCost;
    }

    public void setIngredient(int ingredientID) {
        this.ingredientID = ingredientID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(float unit) {
        this.unit = unit;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "Import [IngredientID = " + ingredientID + ", Name = " + name + ", Unit = " + unit + ", Quantity = " + quantity + ", UnitPrice = " + unitPrice + ", ImportDate = " + imporDate + ", TotalCost = " + totalCost + "]";

    }
}


