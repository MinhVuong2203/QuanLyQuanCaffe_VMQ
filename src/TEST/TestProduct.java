package TEST;

import java.util.Date;

import Entity.Import;
import Entity.Ingredient;
import Entity.Product;

public class TestProduct {
	    public static void main(String[] args) {
	        // Tạo nguyên liệu
	        Ingredient coffeeBeans = new Ingredient(1, "Cà phê hạt", "gram", 500);
	        Ingredient milk = new Ingredient(2, "Sữa tươi", "ml", 1000);

	        // Nhập thêm nguyên liệu
	        Import importCoffee = new Import(1, coffeeBeans, 200, 50000, new Date());
	        Import importMilk = new Import(2, milk, 500, 20000, new Date());

	        // Tạo sản phẩm
	        Product cappuccino = new Product(1, "Cappuccino", 45000);
	        cappuccino.addIngredient(coffeeBeans, 50); // Cần 50g cà phê
	        cappuccino.addIngredient(milk, 200);      // Cần 200ml sữa

	        // Kiểm tra làm sản phẩm
	        System.out.println("Có thể làm Cappuccino? " + cappuccino.canMakeProduct()); // true
	        cappuccino.makeProduct();
	        System.out.println("Số lượng cà phê hạt còn lại: " + coffeeBeans.getStockQuantity());
	        System.out.println("Số lượng sữa còn lại: " + milk.getStockQuantity());
	    }

}
