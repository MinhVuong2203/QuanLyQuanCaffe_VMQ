package Repository.Employee;

import Model.Employee;
import Utils.JdbcUtils;
import Utils.ValidationUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.toedter.calendar.JDateChooser;
import java.util.Date;

public class EmployeeRespository implements IEmployeeRespository {
    private Locale VN = new Locale("vi", "VN");
    private Connection connection;
    private JdbcUtils jdbcUtils;

    public EmployeeRespository() throws IOException, ClassNotFoundException, SQLException {
        jdbcUtils = new JdbcUtils();
    }

    @Override
    public int getIdMaxFromSQL() throws SQLException {
        String sql = "SELECT MAX(employeeID) FROM Employee";
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

    @Override
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

    @Override
    public boolean checkEqualsPhone(String phone) throws SQLException {
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT [phone] FROM [Employee] WHERE [phone] = '" + phone + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return true; // Có tồn tại
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } 
        return false; // Không tồn tại
    }
    @Override
    public boolean checkEqualsCCCD(String cccd) throws SQLException{
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT [CCCD] FROM [Employee] WHERE [CCCD] = '" + cccd + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return true; // Có tồn tại
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } 
        return false; // Không tồn tại
    }

    @Override
    public boolean checkEqualsUsername(String username) throws SQLException{
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT [username] FROM [UserAccount] WHERE [username] = '" + username + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return true; // Có tồn tại
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } 
        return false; // Không tồn tại
    }

    // Cập nhật các phương thức kiểm tra để hỗ trợ excludeId
    public boolean checkEqualsPhone(String phone, int excludeId) throws SQLException, ClassNotFoundException {
        connection = jdbcUtils.connect();
        String sql = "SELECT COUNT(*) FROM Employee WHERE phone = ? AND employeeID != ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, phone);
        stmt.setInt(2, excludeId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    public boolean checkEqualsCCCD(String cccd, int excludeId) throws SQLException, ClassNotFoundException {
        connection = jdbcUtils.connect();
        String sql = "SELECT COUNT(*) FROM Employee WHERE CCCD = ? AND employeeID != ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, cccd);
        stmt.setInt(2, excludeId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    public boolean checkEqualsUsername(String username, int excludeId) throws SQLException, ClassNotFoundException {
        connection = jdbcUtils.connect();
        String sql = "SELECT COUNT(*) FROM UserAccount WHERE username = ? AND ID != ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setInt(2, excludeId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    @Override
    public void addEmployee(Employee employee) throws SQLException, ClassNotFoundException {
        try{
            connection = jdbcUtils.connect(); // Phải có để có connection
            String sqlUserAccount = "INSERT INTO UserAccount (ID, username, [password], role) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtUserAccount = connection.prepareStatement(sqlUserAccount);
            stmtUserAccount.setInt(1, employee.getId()); // ID của UserAccount
            stmtUserAccount.setString(2, employee.getUsername());
            stmtUserAccount.setString(3, employee.getPassword());
            stmtUserAccount.setString(4, employee.getRole());
            stmtUserAccount.executeUpdate();
            String sqlEmployee = "INSERT INTO Employee (employeeID, [name], phone, hourWage, CCCD, birthDate, gender, [image]) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtEmployee = connection.prepareStatement(sqlEmployee);
            stmtEmployee.setInt(1, employee.getId());
            stmtEmployee.setString(2, employee.getName());
            stmtEmployee.setString(3, employee.getPhone());
            stmtEmployee.setDouble(4, employee.getHourlyWage());
            stmtEmployee.setString(5, employee.getCCCD());
            stmtEmployee.setString(6, employee.getBirthDate());
            stmtEmployee.setString(7, employee.getGender());
            stmtEmployee.setString(8, employee.getImage());
            stmtEmployee.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } 
    }

    @Override
    public void updateEmployee(Employee employee) throws SQLException, ClassNotFoundException {
        try {
            connection = jdbcUtils.connect();
            // Cập nhật UserAccount
            String sqlUserAccount = "UPDATE UserAccount SET username = ?, [password] = ?, role = ? WHERE ID = ?";
            PreparedStatement stmtUserAccount = connection.prepareStatement(sqlUserAccount);
            stmtUserAccount.setString(1, employee.getUsername());
            stmtUserAccount.setString(2, employee.getPassword());
            stmtUserAccount.setString(3, employee.getRole());
            stmtUserAccount.setInt(4, employee.getId());
            stmtUserAccount.executeUpdate();

            // Cập nhật Employee
            String sqlEmployee = "UPDATE Employee SET [name] = ?, phone = ?, hourWage = ?, CCCD = ?, birthDate = ?, gender = ?, [image] = ? WHERE employeeID = ?";
            PreparedStatement stmtEmployee = connection.prepareStatement(sqlEmployee);
            stmtEmployee.setString(1, employee.getName());
            stmtEmployee.setString(2, employee.getPhone());
            stmtEmployee.setDouble(3, employee.getHourlyWage());
            stmtEmployee.setString(4, employee.getCCCD());
            stmtEmployee.setString(5, employee.getBirthDate());
            stmtEmployee.setString(6, employee.getGender());
            stmtEmployee.setString(7, employee.getImage());
            stmtEmployee.setInt(8, employee.getId());
            stmtEmployee.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    @Override
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

    @Override
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
                if (rs.getString("role").equalsIgnoreCase("Quản lí") || rs.getString("role").equalsIgnoreCase("Nghỉ việc"))
                    continue;// bỏ qua quản lý và nhân viên nghỉ việc
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

    @Override
    public String getImgByID(int id) throws SQLException {
        String sql = "SELECT image FROM Employee WHERE employeeID = " + id;
        try (Connection connection = jdbcUtils.connect();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("image");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setStatusFromSQL(int id, String status) {
        try (Connection connection = jdbcUtils.connect();
                Statement stmt = connection.createStatement()) {
            String sql = "UPDATE EmployeeShift SET status = N'" + status + "' WHERE employeeID = " + id;
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
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

    @Override
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

    @Override
    public void addShiftToSQL(int id, String dateString, String timeRange) {
        String sql = "INSERT INTO EmployeeShift (shiftID, employeeID, startTime, endTime) " +
                "VALUES (" + (getMaxShiftID() + 1) + ", " + id + ", '" + dateString + " " + timeRange.split("-")[0]
                + ":00', '" + dateString + " " + timeRange.split("-")[1] + ":00')";
                
        try {
            Connection connection = jdbcUtils.connect();
           
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
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

    @Override
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

    @Override
    public void quitJob(int id) throws SQLException, ClassNotFoundException{
        String sql = "UPDATE UserAccount SET role = N'Nghỉ việc' WHERE ID = " + id;
        try (Connection connection = jdbcUtils.connect();
                Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Employee> getAllEmployeesAllAttributes(){
        String sql = "SELECT e.employeeID, e.name, e.phone, e.image, u.username, u.password, u.role, e.CCCD, e.birthDate, e.gender, e.hourWage " +
                     "FROM Employee as e " + 
                     "JOIN UserAccount as u ON e.employeeID = u.ID " +
                     "WHERE u.role != N'Quản lí' AND u.role != N'Nghỉ việc'"; // Lọc luôn ở SQL
        try (Connection connection = jdbcUtils.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            List<Employee> employees = new ArrayList<>();
            while(rs.next()){
                int id = rs.getInt("employeeID");
                String name = rs.getString("name").trim();
                String phone = rs.getString("phone") != null ? rs.getString("phone").trim() : "";
                String image = rs.getString("image") != null ? rs.getString("image").trim() : "";
                String username = rs.getString("username").trim();
                String password = rs.getString("password").trim();
                String role = rs.getString("role").trim();
                String CCCD = rs.getString("CCCD").trim();
                String birthDate = rs.getString("birthDate").trim();
                String gender = rs.getString("gender").trim();
                double hourWage = rs.getDouble("hourWage");
        
                employees.add(new Employee(id, name, phone, image, username, password, role, CCCD, birthDate, gender, hourWage));
            }   
            return employees; 
        } 
        catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>(); // Trả về danh sách rỗng nếu có lỗi
    }
    

    public static void main(String[] args) {
        try {
            EmployeeRespository employeeRepository = new EmployeeRespository();
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
