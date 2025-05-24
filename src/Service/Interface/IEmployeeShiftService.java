package Service.Interface;

import Model.Employee;
import java.sql.SQLException;

import com.toedter.calendar.JDateChooser;
import java.util.List;

public interface IEmployeeShiftService {
    
    // Lấy danh sách tất cả nhân viên cho quản lý
    List<Employee>getAllEmployeesToManager() throws SQLException;

    // Lấy ca làm việc của một nhân viên trong khoảng thời gian
    String[] getEachEmployeeShift(int id, JDateChooser startDay, JDateChooser endDay) throws SQLException;

    // Thêm ca làm việc
    void addShift(int id, String dateString, String timeRange, int hourWage) throws SQLException;

    // Cập nhật ca làm việc
    void updateShift(int id, String dateString, String timeRange, String lastTimeRange) throws SQLException;

    // Xóa ca làm việc
    void deleteShift(int id, String dateString) throws SQLException;

}
