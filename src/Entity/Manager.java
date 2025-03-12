package Entity;
public class Manager extends  Employee{

    public Manager(String name, String phone, String role) {
        super(name, phone, role, 0);
    }

    @Override
    public String toString() {
        return "Manager{" + "id=" + id + ", name=" + name + ", phone=" + phone + ", role=" + role + ", hourlyWage=" + hourlyWage + '}';
    }
}
