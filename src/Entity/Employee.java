package Entity;
public class Employee extends Person{
    protected String role;
    protected double hourlyWage;

    public Employee(String name, String phone, String role, double hourlyWage) {
        super(name, phone);
        this.role = role;
        this.hourlyWage = hourlyWage;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", name=" + name + ", phone=" + phone + ", role=" + role + ", hourlyWage=" + hourlyWage + '}';
    }
}
