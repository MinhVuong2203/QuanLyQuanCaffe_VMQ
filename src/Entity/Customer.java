package Entity;
public class Customer extends Person {
    private int points;
    private String username;
    private String password;
   
    public Customer(int id, String name, String phone, int points, String username, String password) {
        super(id, name, phone);
        this.points = points;
        this.username = username;
        this.password = password;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", name=" + name + ", phone=" + phone + ", points=" + points + ", username=" + username + ", password=" + password + '}';
    }

    
}
