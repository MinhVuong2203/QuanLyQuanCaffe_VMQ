package Repository;

import Model.Employee;
import Utils.JdbcUtils;
import Utils.ValidationUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.toedter.calendar.JDateChooser;
import java.util.Date;

public class EmployeeRepository {
    private Locale VN = new Locale("vi", "VN");

    private Connection connection;
    private JdbcUtils jdbcUtils;

    public EmployeeRepository() throws IOException, ClassNotFoundException, SQLException {
        jdbcUtils = new JdbcUtils();
    }

    public int getMaxShiftID() {
        String sql = "SELECT MAX(shiftID) FROM EmployeeShift";
        try (Connection connection = jdbcUtils.connect();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
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
        String sql = "SELECT e.image, e.employeeID, e.name, es.shiftID, es.startTime, es.endTime, ua.role, es.status\r\n"
                +
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
                employee.setId(rs.getInt("employeeID"));
                employee.getEmployeeShift().setShiftID(rs.getInt("shiftID"));
                employee.getEmployeeShift().setStartTime(rs.getTimestamp("startTime").toLocalDateTime());
                employee.getEmployeeShift().setEndTime(rs.getTimestamp("endTime").toLocalDateTime());
                employee.setRole(rs.getString("role"));
                employee.getEmployeeShift().setStatus(rs.getString("status"));
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
                if (rs.getString("role").equals("Quản lí"))
                    continue;// bỏ qua quản lý
                Employee employee = new Employee();
                employee.setId(rs.getInt("employeeID"));
                employee.setImage(rs.getString("image"));
                employee.setName(rs.getString("name"));
                employee.setRole(rs.getString("role"));
                mapEmployee.put(employee.getId(), employee);
            }
            List<Employee> listEmployee = new ArrayList<>(mapEmployee.values());
            // Sắp xếp theo role
            listEmployee.sort(Comparator.comparing(Employee::getRole).thenComparing(Employee::getOnlyName));
            return listEmployee;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setStatusFromSQL(int id, String status) {
        try (Connection connection = jdbcUtils.connect();
                Statement stmt = connection.createStatement()) {
            String sql = "UPDATE EmployeeShift SET status = N'" + status + "' WHERE employeeID = " + id;
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStatusFromSQL(int id) {
        String sql = "SELECT status FROM EmployeeShift WHERE employeeID = " + id;
        try (Connection connection = jdbcUtils.connect();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] getEachEmployeeShift(int id, JDateChooser startDay, JDateChooser endDay) {
        int n = ValidationUtils.CalculateDate(startDay, endDay) + 1;
        String[] x = new String[n];
        for (int i = 0; i < n; i++) {
            x[i] = "";
        }
        // Chuyển JDateChooser thành LocalDate
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = startDay.getDate();
        Date endDate = new Date(endDay.getDate().getTime() + 86400000); // Cộng thêm 1 ngày để lấy đến ngày
        String startDayString = sdf.format(startDate);
        String endDayString = sdf.format(endDate);
        String sql = "SELECT [employeeID], [startTime],[endTime]\r\n" +
                "FROM [dbo].[EmployeeShift]\r\n" + //
                "WHERE [employeeID] = " + id + " AND [startTime] >= '" + startDayString + "' AND [endTime] < '"
                + endDayString + "'";
        try (Connection connection = jdbcUtils.connect();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int row = ValidationUtils.CalculateDate(startDayString, rs.getString("startTime")); // Lấy tọa độ
                String a = rs.getString("startTime").split(" ")[1];
                String b = rs.getString("endTime").split(" ")[1];
                a = a.substring(0, 5);
                b = b.substring(0, 5);
                x[row] = a + " - " + b; // Lưu vào mảng
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return x;
    }

    public void addShiftToSQL(int id, String dateString, String timeRange) {
        String sql = "INSERT INTO EmployeeShift (shiftID, employeeID, startTime, endTime) " +
                "VALUES (" + (getMaxShiftID() + 1) + ", " + id + ", '" + dateString + " " + timeRange.split("-")[0]
                + ":00', '" + dateString + " " + timeRange.split("-")[1] + ":00')";
        try (Connection connection = jdbcUtils.connect();
                Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteShiftFromSQL(int id, String dateString) {
        String sql = "DELETE FROM EmployeeShift WHERE employeeID = " + id + " AND CAST(startTime AS DATE) = '"
                + dateString + "'";
        try (Connection connection = jdbcUtils.connect();
                Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateShiftToSQL(int id, String dateString, String timeRange, String lastTimeRange) {
        String sql = "UPDATE EmployeeShift SET startTime = '" + dateString + " " + timeRange.split("-")[0]
                + ":00', endTime = '" + dateString + " " + timeRange.split("-")[1] + ":00' "
                + "WHERE employeeID = " + id + " AND startTime = '" + dateString + " " + lastTimeRange.split("-")[0]
                + ":00' AND endTime = '" + dateString + " " + lastTimeRange.split("-")[1] + ":00'";
        try (Connection connection = jdbcUtils.connect();
                Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            EmployeeRepository employeeRepository = new EmployeeRepository();
            List<Employee> employees = employeeRepository.getAllEmployees();
            for (Employee employee : employees) {
                System.out.println(employee.getImage() + " " + employee.getName() + " "
                        + employee.getEmployeeShift().getShiftID() + " Thoi gian bat dau: "
                        + employee.getEmployeeShift().getStartTime() + " Thoi gian ket thuc: "
                        + employee.getEmployeeShift().getEndTime()
                        + " " + employee.getRole() + " ID: " + employee.getId() + " Status: "
                        + employee.getEmployeeShift().getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // try {
        // EmployeeRepository employeeRepository = new EmployeeRepository();
        // List<Employee> employees = employeeRepository.getAllEmployeesToManager();
        // for (Employee employee : employees) {
        // System.out.println(employee.getImage() + " " + employee.getName() + " "
        // + employee.getRole() + " ID: " + employee.getId());
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

}
