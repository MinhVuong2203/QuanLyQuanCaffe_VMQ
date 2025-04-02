package TEST;

import java.time.LocalDateTime;

import Model.Order;
import Model.Payment;
import Model.Product;

public class testOrder {
	    public static void main(String[] args) {
			

			// 1. Tạo Product (Đọc product từ database)
			Product product_CaPheSua_S = new Product(101, "Cà phê sữa",15000, "S",  "src\\image");
			// 2. Tạo Order
	        Order order = new Order(1001, 201, 101, 4,"Đang chờ");
			// 3. Tạo Payment (Phương thức thanh toán sau), thì Payment phải có OrderID
	        Payment payment1 = new Payment(1, order.getOrderID(), "Cash", 20000, LocalDateTime.now());
			// 4. Add OrderDetail vào Order
			order.addOderDeTailProduct(product_CaPheSua_S, 2);
		
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


