package Entity;
public class UserSystem extends Person{
    private String username;
    private String password;
    private String role;

    public UserSystem(String name, String phone, String username, String password, String role) {
        super(name, phone);
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

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", phone=" + phone + ", username=" + username + ", password=" + password + ", role=" + role + '}';
    }
}
