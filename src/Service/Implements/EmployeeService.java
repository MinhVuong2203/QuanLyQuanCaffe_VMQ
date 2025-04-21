package Service.Implements;

import Model.Employee;
import Repository.Employee.EmployeeRespository;
import Repository.Employee.IEmployeeRespository;
import Service.Interface.IEmployeeService;
import java.io.IOException;
import java.sql.SQLException;

public class EmployeeService implements IEmployeeService {
    private IEmployeeRespository employeeRepository;

    public EmployeeService() throws ClassNotFoundException, IOException, SQLException {
        this.employeeRepository = new EmployeeRespository();

    }

    @Override
    public void addEmployee(Employee employee) throws SQLException, ClassNotFoundException {
        if (this.employeeRepository.checkEqualsPhone(employee.getPhone())) {
            throw new RuntimeException("Số điện thoại đã tồn tại!");
        } else if (this.employeeRepository.checkEqualsCCCD(employee.getCCCD())) {
            throw new RuntimeException("CCCD đã tồn tại!");
        }
        else if (this.employeeRepository.checkEqualsUsername(employee.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        else {
            this.employeeRepository.addEmployee(employee); // Thêm nhân viên vào cơ sở dữ liệu
            System.out.println("Adding employee: " + employee.getName());
        }
    }

}
