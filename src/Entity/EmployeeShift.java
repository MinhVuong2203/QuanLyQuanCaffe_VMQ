package Entity;
import java.util.Date;
public class EmployeeShift {
    private int shiftID;
    private int employeeID;
    private Date startTime;
    private Date endTime;
    private double hourWorked;
    private double salary;
    public EmployeeShift(){}
    public EmployeeShift(int shiftID, int employeeID, Date startTime, Date endTime, double salary) {
        this.shiftID = shiftID;
        this.employeeID = employeeID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hourWorked = dateDiff(startTime, endTime);
        this.salary = salary;
    }

    private double dateDiff(Date startTime, Date endTime) {
        long diffInMillies = endTime.getTime() - startTime.getTime();
        return (double) diffInMillies / (1000 * 60 * 60);
    }
    public int getShiftID() {
        return shiftID;
    }

    public int getEmployeeID() {
        return employeeID;
    }
    public Date getStartTime() {
        return startTime;
    }
    public Date getEndTime() {
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
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(Date endTime) {
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
