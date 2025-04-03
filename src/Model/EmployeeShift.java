package Model;
import java.time.Duration;
import java.time.LocalDateTime;
public class EmployeeShift {
    private int shiftID;
    private int employeeID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double hourWorked;
    private double salary;
    public EmployeeShift(){}
   
    public EmployeeShift(int shiftID, int employeeID, LocalDateTime startTime, LocalDateTime endTime, double hourlyWage){  // hourlyWage là lương theo giờ của nhân viên
        this.shiftID = shiftID;
        this.employeeID = employeeID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hourWorked = Duration.between(startTime, endTime).toHours();
        this.salary = hourlyWage * hourWorked;
    }

    public int getShiftID() {
        return shiftID;
    }

    public int getEmployeeID() {
        return employeeID;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }
    public double getHourWorked() {
        return hourWorked;
    }
    public double getSalary() {
        return salary;
    }
    public void setShiftID(int shiftID) {
        this.shiftID = shiftID;
    }
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    public void setHourWorked(double hourWorked) {
        this.hourWorked = hourWorked;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }
    @Override
    public String toString() {
        return "EmplyeeShift{" + "shiftID=" + shiftID + ", employeeID=" + employeeID + ", startTime=" + startTime + ", endTime=" + endTime + ", hourWorked=" + hourWorked + ", salary=" + salary + '}';
    }
}
