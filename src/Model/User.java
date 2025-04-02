package Model;
public class User extends Person{
    protected  String username;
    protected String password;
    protected String role;

    public User(int id, String name, String phone, String image, String username, String password, String role) {
        super(id, name, phone, image);
        this.username = username;
        this.password = password;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", phone=" + phone + ", username=" + username + ", password=" + password + ", role=" + role + '}';
    }
    

}
