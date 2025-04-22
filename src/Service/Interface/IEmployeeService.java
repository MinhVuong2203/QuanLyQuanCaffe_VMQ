package Service.Interface;

import Model.Employee;
import java.sql.SQLException;

public interface IEmployeeService {
    public void addEmployee(Employee employee) throws SQLException, ClassNotFoundException; // Thêm nhân viên vào cơ sở dữ liệu
    void updateEmployee(Employee employee) throws SQLException, ClassNotFoundException; // Cập nhật thông tin nhân viên trong cơ sở dữ liệu
}
