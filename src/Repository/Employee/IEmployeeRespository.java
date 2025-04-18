package Repository.Employee;

import Model.Employee;
import java.sql.SQLException;
import java.util.List;

import com.toedter.calendar.JDateChooser;

public interface IEmployeeRespository {
    public int getMaxShiftID();
    public boolean checkEqualsPhone(String phone) throws SQLException;
    public List<Employee> getAllEmployees() throws SQLException;
    public List<Employee> getAllEmployeesToManager() throws SQLException;
    public void setStatusFromSQL(int id, String status);
    public String getStatusFromSQL(int id);
    public String[] getEachEmployeeShift(int id, JDateChooser startDay, JDateChooser endDay);
    public void addShiftToSQL(int id, String dateString, String timeRange) ;
    public void deleteShiftFromSQL(int id, String dateString) ;
    public void updateShiftToSQL(int id, String dateString, String timeRange, String lastTimeRange);
    public List<Employee> getAllEmployeesAllAttributes();
}
