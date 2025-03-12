package Entity;
public class Customer extends Person {
    private int points;

    public Customer(String name, String phone, int points) {
        super(name, phone);
        this.points = points;
    }

    public Customer(int id, String name, String phone, int points) {
        super(id, name, phone);
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", name=" + name + ", phone=" + phone + ", points=" + points + '}';
    }

    public static Customer getGuestCustomer() {  // Khi có khách vãng lai
        return new Customer(10000, "Guest", "", 0);
    }
}
