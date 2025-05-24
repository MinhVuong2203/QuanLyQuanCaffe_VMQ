package Model;
public class Manager extends Employee{
	
    public Manager(int id, String name, String phone, String image, String username, String password, String CCCD, String birthDate, String gender, int hourlyWage) {
		super(id, name, phone, image, username, password,"Quản lí" ,CCCD, birthDate, gender, hourlyWage);
	}

	@Override
    public String toString() {
        return "Manager{" + "id=" + id + ", name=" + name + ", phone=" + phone + ", role=" + role + ", hourlyWage=" + hourlyWage + '}';
    }
}
