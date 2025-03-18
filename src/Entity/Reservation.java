package Entity;

import java.time.LocalDateTime;

public class Reservation {
    private int reservationID;
    private int customerID;
    private int tableID;
    private LocalDateTime reservationTime;
    private String status; // "Confirmed", "Cancelled", "Completed"
    private int numGuests; // Số lượng khách
    private String note; // Ghi chú đặc biệt

    public Reservation(int reservationID, int customerID, int tableID, LocalDateTime reservationTime, int numGuests, String note) {
        this.reservationID = reservationID;
        this.customerID = customerID;
        this.tableID = tableID;
        this.reservationTime = reservationTime;
        this.status = "Confirmed"; // Mặc định đặt bàn là "Confirmed"
        this.numGuests = numGuests;
        this.note = note;
    }

    // Hủy đặt bàn
    public void cancelReservation() {
        if (!this.status.equals("Confirmed")) {
            System.out.println("⚠ Không thể hủy đặt bàn (đã hoàn tất hoặc đã hủy).");
            return;
        }
        this.status = "Cancelled";
        System.out.println("❌ Đặt bàn #" + reservationID + " đã bị hủy.");
    }

    // Đánh dấu đã sử dụng
    public void completeReservation() {
        if (!this.status.equals("Confirmed")) {
            System.out.println("⚠ Không thể hoàn tất đặt bàn (đã hủy hoặc chưa đặt).");
            return;
        }
        this.status = "Completed";
        System.out.println("✅ Đặt bàn #" + reservationID + " đã được sử dụng.");
    }

    // Getter
    public int getReservationID() { return reservationID; }
    public String getStatus() { return status; }
    public int getNumGuests() { return numGuests; }
    public String getNote() { return note; }
}

