package Model;
public class Customer extends Person{
    private double points;
    
    public Customer(int id, String name, String phone, double points) {
        super(id, name, phone);
        this.points = points;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double newPoints) {
        this.points = newPoints;
    }

    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", name=" + name + ", phone=" + phone + ", points=" + points + '}';
    }

   
}
