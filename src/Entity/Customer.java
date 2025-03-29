package Entity;
public class Customer extends User {
    private int points;
    
    public Customer(int id, String name, String phone, String image, String username, String password, String role, int points) {
        super(id, name, phone, image, username, password, "Khách hàng");
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
        return "Customer{" + "id=" + id + ", name=" + name + ", phone=" + phone + ", image=" + image +  ", username=" + username + ", password=" + password + ", role=" + role + ", points=" + points + '}';
    }

   
}
