package Repository;

import Model.Employee;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EmployeeRepository {
    private Locale VN = new Locale("vi", "VN");

    private Connection connection;
    private JdbcUtils jdbcUtils;

    public EmployeeRepository() throws IOException, ClassNotFoundException, SQLException {
        jdbcUtils = new JdbcUtils();
    }

    public boolean checkEqualsPhone(String phone) throws SQLException {
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT [phone]\r\n" + //
                    "FROM [Employee]\r\n" + //
                    "WHERE [phone] = '" + phone + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
                return true; // Có tồn tại
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return false; // Không tồn tại
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.image, e.name, es.shiftID, es.startTime, es.endTime, ua.role\r\n" +
                " FROM Employee e\r\n" + //
                " JOIN EmployeeShift es ON es.employeeID = e.employeeID\r\n" +
                " JOIN UserAccount ua ON e.employeeID = ua.ID\r\n";
        // " FROM Employee \r\n";

        try (Connection connection = jdbcUtils.connect();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setImage(rs.getString("image"));
                employee.setName(rs.getString("name"));
                employee.getEmployeeShift().setShiftID(rs.getInt("shiftID"));
                employee.getEmployeeShift().setStartTime(rs.getTimestamp("startTime").toLocalDateTime());
                employee.getEmployeeShift().setEndTime(rs.getTimestamp("endTime").toLocalDateTime());
                employee.setRole(rs.getString("role"));
                // employee.setHourlyWage(rs.getDouble("hourlyWage"));
                employees.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employees;
    }

    public List<Employee> getAllEmployeesToManager() throws SQLException {
        Map<Integer, Employee> mapEmployee = new java.util.HashMap<>();
        String sql = "SELECT e.employeeID , e.image, e.name, ua.role "
                     + "FROM Employee e "
                     + "JOIN UserAccount ua ON e.employeeID = ua.ID";
        // " FROM Employee \r\n";

        try (Connection connection = jdbcUtils.connect();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                if (rs.getString("role").equals("Quản lí")) continue;// bỏ qua quản lý
                    Employee employee = new Employee();
                    employee.setId(rs.getInt("employeeID"));
                    employee.setImage(rs.getString("image"));
                    employee.setName(rs.getString("name"));
                    employee.setRole(rs.getString("role"));
                    mapEmployee.put(employee.getId(), employee);
            }
            List<Employee> listEmployee = new ArrayList<>(mapEmployee.values());
            // Sắp xếp theo role
            listEmployee.sort(Comparator.comparing(Employee::getRole));
            return listEmployee;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        // try {
        //     EmployeeRepository employeeRepository = new EmployeeRepository();
        //     List<Employee> employees = employeeRepository.getAllEmployees();
        //     for (Employee employee : employees) {
        //         System.out.println(employee.getImage() + " " + employee.getName() + " "
        //                 + employee.getEmployeeShift().getShiftID() + " Thoi gian bat dau: "
        //                 + employee.getEmployeeShift().getStartTime() + " Thoi gian ket thuc: "
        //                 + employee.getEmployeeShift().getEndTime());
        //     }
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        try{
            EmployeeRepository employeeRepository = new EmployeeRepository();
            List<Employee> employees = employeeRepository.getAllEmployeesToManager();
            for (Employee employee : employees) {
                System.out.println(employee.getImage() + " " + employee.getName() + " "
                        + employee.getRole() + " ID: " + employee.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
