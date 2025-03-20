package TEST;

import java.time.LocalDateTime;

import Entity.Reservation;

public class testReservation {
	    public static void main(String[] args) {
	        // Khách hàng 101 đặt bàn 5 lúc 19:00, không cần nhân viên xác nhận
	        Reservation res = new Reservation(1, 101, 5, LocalDateTime.now(), 4, "Bàn ngoài trời");

	        // Kiểm tra trạng thái mặc định
	        System.out.println("Trạng thái đặt bàn: " + res.getStatus()); // Sẽ là "Confirmed"

	        // Hoàn tất đặt bàn
	        res.completeReservation();
	        System.out.println("Trạng thái đặt bàn: " + res.getStatus()); // Sẽ là "Completed"
	    }

}
