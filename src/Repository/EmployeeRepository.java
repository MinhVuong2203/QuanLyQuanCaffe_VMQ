package Repository;

import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Employee;
import Model.EmployeeShift;

public class EmployeeRepository {
    private Connection connection;
    private JdbcUtils jdbcUtils;
    public EmployeeRepository() throws IOException, ClassNotFoundException, SQLException{
        jdbcUtils = new JdbcUtils();
    }

    public boolean checkEqualsPhone(String phone) throws SQLException{
        try{
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT [phone]\r\n" + //
                        "FROM [Employee]\r\n" + //
                        "WHERE [phone] = '" + phone + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return true; // Có tồn tại
        } catch (Exception e){
            e.printStackTrace();
        } finally {
			connection.close();
		}
        return false; // Không tồn tại
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT image, name FROM Employee \r\n" ;
                    // " FROM Employee \r\n";

        try (Connection connection = jdbcUtils.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setImage(rs.getString("image"));
                employee.setName(rs.getString("name"));
                // employee.setHourlyWage(rs.getDouble("hourlyWage"));
                employees.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employees;
    }

    public static void main(String[] args) {
        try {
            EmployeeRepository employeeRepository = new EmployeeRepository();
            List<Employee> employees = employeeRepository.getAllEmployees();
            for (Employee employee : employees) {
                System.out.println(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

