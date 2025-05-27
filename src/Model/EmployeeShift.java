package Model;
import java.time.Duration;
import java.time.LocalDateTime;
public class EmployeeShift {
    private int shiftID;
    private int employeeID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double hourWorked;
    private int hourWage;
    private double salary;
    private String status; // trạng thái ca làm việc (Đã điểm danh, chưa điểm danh, chờ duyệt)
    public EmployeeShift(){}
   

    public EmployeeShift(int shiftID, int employeeID, LocalDateTime startTime, LocalDateTime endTime, double hourWorked,
			 int hourWage, double salary, String status) {
		this.shiftID = shiftID;
		this.employeeID = employeeID;
		this.startTime = startTime;
		this.endTime = endTime;
		this.hourWorked = hourWorked;
		this.salary = salary;
		this.hourWage = hourWage;
		this.status = status;
	}
    
	public int getShiftID() {
		return shiftID;
	}

	public void setShiftID(int shiftID) {
		this.shiftID = shiftID;
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public double getHourWorked() {
		return hourWorked;
	}

	public void setHourWorked(double hourWorked) {
		this.hourWorked = hourWorked;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public int getHourWage() {
		return hourWage;
	}

	public void setHourWage(int hourWage) {
		this.hourWage = hourWage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "EmployeeShift [shiftID=" + shiftID + ", employeeID=" + employeeID + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", hourWorked=" + hourWorked + ", hourWage=" + hourWage + ", salary="
				+ salary + ", status=" + status + "]";
	}

	public EmployeeShift(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public EmployeeShift(int employeeID, LocalDateTime startTime, LocalDateTime endTime) {
    this.employeeID = employeeID;
    this.startTime = startTime;
    this.endTime = endTime;
}

    
}
