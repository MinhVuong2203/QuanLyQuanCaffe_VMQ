package Model;
public class Customer extends User {
    private double points;
    
    public Customer(int id, String name, String phone, String image, String username, String password, double points) {
        super(id, name, phone, image, username, password, "Khách");
        this.points = points;
    }

    public double getPoints() {
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
