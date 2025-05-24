package Service.Implements;

import Model.Employee;
import Repository.Employee.IEmployeeRespository;
import Service.Interface.IEmployeeShiftService;
import Repository.Employee.EmployeeRespository;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class EmployeeShiftService implements IEmployeeShiftService {

    private final IEmployeeRespository employeeRepository;

    public EmployeeShiftService() throws ClassNotFoundException, IOException, SQLException {
        this.employeeRepository = new EmployeeRespository();
    }

    // Lấy danh sách tất cả nhân viên cho quản lý
    @Override
    public List<Employee> getAllEmployeesToManager() throws SQLException {
        return employeeRepository.getAllEmployeesToManager();
    }

    // Lấy ca làm việc của một nhân viên trong khoảng thời gian
    @Override
    public String[] getEachEmployeeShift(int id, JDateChooser startDay, JDateChooser endDay) throws SQLException {
        return employeeRepository.getEachEmployeeShift(id, startDay, endDay);
    }

    // Thêm ca làm việc
    @Override
    public void addShift(int id, String dateString, String timeRange, int hourWage) throws SQLException {
        if (timeRange == null || timeRange.isEmpty()) {
            throw new IllegalArgumentException("Thời gian ca làm việc không được để trống!");
        }
        employeeRepository.addShiftToSQL(id, dateString, timeRange, hourWage);
    }

    // Cập nhật ca làm việc
    @Override
    public void updateShift(int id, String dateString, String timeRange, String lastTimeRange) throws SQLException {
        if (timeRange == null || timeRange.isEmpty() || lastTimeRange == null || lastTimeRange.isEmpty()) {
            throw new IllegalArgumentException("Thời gian ca làm việc không được để trống!");
        }
        employeeRepository.updateShiftToSQL(id, dateString, timeRange, lastTimeRange);
    }

    // Xóa ca làm việc
    @Override
    public void deleteShift(int id, String dateString) throws SQLException {
        employeeRepository.deleteShiftFromSQL(id, dateString);
    }
}