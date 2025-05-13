package Repository.Employee;

import Model.Employee;
import Model.EmployeeShift;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.toedter.calendar.JDateChooser;

public interface IEmployeeRespository {
    public int getIdMaxFromSQL() throws SQLException;
    public int getMaxShiftID() throws SQLException;
    public boolean checkEqualsPhone(String phone) throws SQLException;
    public boolean checkEqualsCCCD(String cccd) throws SQLException;
    public boolean checkEqualsUsername(String username) throws SQLException;
    public boolean checkEqualsPhone(String phone, int excludeId) throws SQLException, ClassNotFoundException;
    public boolean checkEqualsCCCD(String cccd, int excludeId) throws SQLException, ClassNotFoundException;
    public boolean checkEqualsUsername(String username, int excludeId) throws SQLException, ClassNotFoundException;
    public void updateEmployee(Employee employee) throws SQLException, ClassNotFoundException;
    public void addEmployee(Employee employee) throws SQLException, ClassNotFoundException;
    public void quitJob(int id) throws SQLException, ClassNotFoundException;
    public List<Employee> getAllEmployees() throws SQLException;
    public List<Employee> getAllEmployeesToManager() throws SQLException;
    public String getImgByID(int id) throws SQLException;
    public void setStatusFromSQL(int id, String status, int shiftID) throws SQLException;
    public String getStatusFromSQL(int id, int shiftID) throws SQLException;
    public String[] getEachEmployeeShift(int id, JDateChooser startDay, JDateChooser endDay) throws SQLException;
    public void addShiftToSQL(int id, String dateString, String timeRange) throws SQLException ;
    public void deleteShiftFromSQL(int id, String dateString) throws SQLException ;
    public void updateShiftToSQL(int id, String dateString, String timeRange, String lastTimeRange) throws SQLException;
    public List<Employee> getAllEmployeesAllAttributes() throws SQLException;
    public EmployeeShift getEmployeeShiftByEmployeeID(int employeeID) throws SQLException, ClassNotFoundException;
    public List<Employee> getShiftsBetweenDates(LocalDate fromDate, LocalDate toDate) throws SQLException,ClassNotFoundException;
}