package Service.Interface;

import Model.Employee;
import java.sql.SQLException;

public interface IEmployeeService {
    public void addEmployee(Employee employee) throws SQLException, ClassNotFoundException; // Thêm nhân viên vào cơ sở dữ liệu
}
