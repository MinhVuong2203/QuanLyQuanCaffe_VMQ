package Entity;
import java.util.Date;
public class reservation {
    private int reservationID;
    private int tableID;
    private int customerID;
    private Date reservationDateTime;
    private boolean status;
    public reservation(){}
    public reservation(int reservationID, int tableID, int customerID, Date reservationDateTime, boolean status) {
        this.reservationID = reservationID;
        this.tableID = tableID;
        this.customerID = customerID;
        this.reservationDateTime = reservationDateTime;
        this.status = status;
    }
    public int getReservationID() {
        return reservationID;
    }
    public int getTableID() {
        return tableID;
    }
    public int getCustomerID() {
        return customerID;
    }
    public Date getReservationDateTime() {
        return reservationDateTime;
    }
    public boolean isStatus() {
        return status;
    }
    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }
    public void setTableID(int tableID) {
        this.tableID = tableID;
    }
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    public void setReservationDateTime(Date reservationDateTime) {
        this.reservationDateTime = reservationDateTime;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "reservation{" + "reservationID=" + reservationID + ", tableID=" + tableID + ", customerID=" + customerID + ", reservationDateTime=" + reservationDateTime + ", status=" + status + '}';
    }
}
