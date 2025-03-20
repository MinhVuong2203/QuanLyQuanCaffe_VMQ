package TEST;

import Entity.Order;
import Entity.Payment;
import Entity.Product;
import java.time.LocalDateTime;

public class testOrder {
	    public static void main(String[] args) {
			

			// 1. Tạo Product 	
			Product product_CaPheSua_S = new Product(101, "Cà phê sữa", "S", 15000);
			Product product_CaPheSua_X = new Product(102, "Cà phê sữa", "X", 20000);
			Product product_CaPheSua_XL = new Product(103, "Cà phê sữa", "XL", 25000);
			
			Product product_CaPheDen_S = new Product(104, "Cà phê đen", "S", 15000);
			Product product_CaPheDen_X = new Product(105, "Cà phê đen", "X", 15000);
			Product product_CaPheDen_XL = new Product(106, "Cà phê đen", "XL", 15000);
			// 2. Tạo Order
	        Order order = new Order(1001, 201, 101, 4,"Đang chờ");
			// 3. Tạo Payment (Phương thức thanh toán sau), thì Payment phải có OrderID
	        Payment payment1 = new Payment(1, order.getOrderID(), "Cash", 20000, LocalDateTime.now());
			// 4. Add OrderDetail vào Order
			order.addOderDeTailProduct(product_CaPheSua_S, 2);
			order.addOderDeTailProduct(product_CaPheDen_X, 10);
			order.addOderDeTailProduct(product_CaPheDen_XL, 1); // Thêm trùng sản phẩm thì sản phẩm đó sẽ cộng dồn số lượng
			order.addOderDeTailProduct(product_CaPheDen_XL, 1); 
			order.addOderDeTailProduct(product_CaPheDen_XL, 1); 
			order.addOderDeTailProduct(product_CaPheDen_XL, 1); 
			// 6. Add Payment vào Order
			order.addPayment(payment1); 
	        // Hoàn tất đơn hàng
			order.completeOrder();
	       


			
			order.getTotalPrice();

	        // Kiểm tra trạng thái đơn hàng
	        System.out.println("Trạng thái đơn hàng: " + order.getStatus());
	        System.out.println("Tổng số tiền đã thanh toán: " + order.getTotalPaid());
	    }
}


