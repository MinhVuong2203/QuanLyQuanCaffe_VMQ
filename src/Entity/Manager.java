package Entity;
public class Manager extends Employee{

    public Manager(int id, String name, String phone, String username, String password) {
        super(id, name, phone, username, password, 0);
        this.role = "Quản lý";
    }

    @Override
    public String toString() {
        return "Manager{" + "id=" + id + ", name=" + name + ", phone=" + phone + ", role=" + role + ", hourlyWage=" + hourlyWage + '}';
    }
}
