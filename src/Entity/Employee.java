package Entity;
public class Employee extends User{
    protected String role;
    protected double hourlyWage;

    public Employee(int id, String name, String phone, String image, String username, String password, double hourlyWage) {
        super(id, name, phone, image, username, password, "Nhân viên");
        this.hourlyWage = hourlyWage;
    }

    public double getHourlyWage() {
        return hourlyWage;
    }


    public void setHourlyWage(double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", name=" + name + ", phone=" + phone + ", image=" + image + ", username=" + username + ", password=" + password + ", role=" + role + ", hourlyWage=" + hourlyWage + '}';
    }
}
