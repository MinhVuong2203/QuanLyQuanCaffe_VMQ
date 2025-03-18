package Entity;
public class Manager extends  Employee{

    public Manager(int id, String name, String phone, String role, double hourlyWage) {
        super(id, name, phone, role, hourlyWage);
    }

    @Override
    public String toString() {
        return "Manager{" + "id=" + id + ", name=" + name + ", phone=" + phone + ", role=" + role + ", hourlyWage=" + hourlyWage + '}';
    }
}
