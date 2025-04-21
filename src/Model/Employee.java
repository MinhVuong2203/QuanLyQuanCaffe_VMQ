package Model;
public class Employee extends User{
    protected double hourlyWage;
    protected String CCCD;
    protected String birthDate;
    protected String gender;
	protected EmployeeShift employeeShift = new EmployeeShift(); // Khởi tạo đối tượng EmployeeShift
    public Employee() {
		super();
	}

	public Employee(int id, String name, String phone, String image, String username, String password, String role, String CCCD, String birthDate, String gender, double hourlyWage) {
        super(id, name, phone, image, username, password, role);
        this.hourlyWage = hourlyWage;
        this.CCCD = CCCD;
        this.birthDate = birthDate;
        this.gender = gender;
    }
    

	public double getHourlyWage() {
		return hourlyWage;
	}

	public void setHourlyWage(double hourlyWage) {
		this.hourlyWage = hourlyWage;
	}

	public String getCCCD() {
		return CCCD;
	}

	public void setCCCD(String cCCD) {
		CCCD = cCCD;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Override
	public String toString() {
		return "Employee [hourlyWage=" + hourlyWage + ", CCCD=" + CCCD + ", birthDate=" + birthDate + ", gender="
				+ gender + ", employeeShift=" + employeeShift + ", username=" + username + ", password=" + password
				+ ", role=" + role + ", image=" + image + ", id=" + id + ", name=" + name + ", phone=" + phone + "]";
	}

	public EmployeeShift getEmployeeShift() {
		return employeeShift;
	}

	public void setEmployeeShift(EmployeeShift employeeShift) {
		this.employeeShift = employeeShift;
	}
}
