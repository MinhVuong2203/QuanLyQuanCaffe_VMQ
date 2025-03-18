package TEST;

import java.time.LocalDateTime;

import Entity.Order;
import Entity.OrderDetail;
import Entity.Payment;
import Entity.Product;

public class testOrder {

	    public static void main(String[] args) {
	        // Tạo đơn hàng 200.000 VND
	        Order order = new Order(1001, 201, 101, 200000);

	        // Thanh toán lần 1: 100.000 VND bằng tiền mặt
	        Payment payment1 = new Payment(1, order.getOrderID(), "Cash", 100000, LocalDateTime.now(), "Success", null);
	        order.addPayment(payment1);

	        // Thanh toán lần 2: 100.000 VND qua thẻ
	        Payment payment2 = new Payment(2, order.getOrderID(), "Credit Card", 100000, LocalDateTime.now(), "Success", "TXN12345");
	        order.addPayment(payment2);

	        // Kiểm tra trạng thái đơn hàng
	        System.out.println("Tổng số tiền đã thanh toán: " + order.getTotalPaid());
	        System.out.println("Trạng thái đơn hàng: " + order.getStatus());
	    }
}


