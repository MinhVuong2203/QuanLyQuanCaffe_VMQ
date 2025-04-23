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

    @Override
    public void updateEmployee(Employee employee) throws SQLException, ClassNotFoundException {
        // Kiểm tra số điện thoại và CCCD, bỏ qua nếu trùng với chính nhân viên hiện tại
        if (this.employeeRepository.checkEqualsPhone(employee.getPhone(), employee.getId())) {
            throw new RuntimeException("Số điện thoại đã được sử dụng bởi người khác!");
        } else if (this.employeeRepository.checkEqualsCCCD(employee.getCCCD(), employee.getId())) {
            throw new RuntimeException("CCCD đã được sử dụng bởi người khác!");
        } else if (this.employeeRepository.checkEqualsUsername(employee.getUsername(), employee.getId())) {
            throw new RuntimeException("Tên đăng nhập đã được sử dụng bởi người khác!");
        } else {
            this.employeeRepository.updateEmployee(employee);
            System.out.println("Updating employee: " + employee.getName());
        }
    }

    @Override
    public boolean quitJob(int id){
        try {
            this.employeeRepository.quitJob(id);
            return true;
        } catch (ClassNotFoundException | SQLException e) {
           System.out.println("Error: " + e.getMessage());
        }
        return false;
    }
}
