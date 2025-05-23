package Repository.EmployeeShift;


import Model.EmployeeShift;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public interface IEmployeeShiftRepository {
    public List<EmployeeShift> getAllEmployeeShift() throws SQLException, IOException, ClassNotFoundException;
    public double getHourlyWageByEmployeeID(int employeeID) throws SQLException, IOException, ClassNotFoundException;
       public List<EmployeeShift> getShiftsByEmployeeIDBetweenDates(int employeeID, LocalDate fromDate, LocalDate toDate) throws SQLException,ClassNotFoundException;
    }
