package Repository.Employee;

import Model.Employee;
import Model.EmployeeShift;
import Utils.JdbcUtils;
import Utils.ValidationUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeRespository implements IEmployeeRespository {
    private final JdbcUtils jdbcUtils;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public EmployeeRespository() throws IOException, ClassNotFoundException, SQLException {
        this.jdbcUtils = new JdbcUtils();
    }

    @Override
    public int getIdMaxFromSQL() throws SQLException {
        String sql = "SELECT MAX(employeeID) FROM Employee";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public int getMaxShiftID() throws SQLException {
        String sql = "SELECT MAX(shiftID) FROM EmployeeShift";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public String getNameFromID(int id) throws SQLException {
        String sql = "SELECT name FROM Employee WHERE employeeID = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getString("name");
                return null;
            }
        }
    }

    @Override
    public boolean checkEqualsPhone(String phone) throws SQLException {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        String sql = "SELECT 1 FROM Employee WHERE phone = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean checkEqualsCCCD(String cccd) throws SQLException {
        if (cccd == null || cccd.isEmpty()) {
            throw new IllegalArgumentException("CCCD không hợp lệ");
        }
        String sql = "SELECT 1 FROM Employee WHERE CCCD = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cccd);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean checkEqualsUsername(String username) throws SQLException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không hợp lệ");
        }
        String sql = "SELECT 1 FROM UserAccount WHERE username = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean checkEqualsPhone(String phone, int excludeId) throws SQLException {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        String sql = "SELECT 1 FROM Employee WHERE phone = ? AND employeeID != ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, phone);
            stmt.setInt(2, excludeId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean checkEqualsCCCD(String cccd, int excludeId) throws SQLException {
        if (cccd == null || cccd.isEmpty()) {
            throw new IllegalArgumentException("CCCD không hợp lệ");
        }
        String sql = "SELECT 1 FROM Employee WHERE CCCD = ? AND employeeID != ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cccd);
            stmt.setInt(2, excludeId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean checkEqualsUsername(String username, int excludeId) throws SQLException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không hợp lệ");
        }
        String sql = "SELECT 1 FROM UserAccount WHERE username = ? AND ID != ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setInt(2, excludeId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public void addEmployee(Employee employee) throws SQLException {
        if (employee == null || employee.getId() <= 0 || employee.getUsername() == null
                || employee.getPassword() == null) {
            throw new IllegalArgumentException("Thông tin nhân viên không hợp lệ");
        }
        try (Connection connection = jdbcUtils.connect()) {
            String sqlUserAccount = "INSERT INTO UserAccount (ID, username, [password], role) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmtUserAccount = connection.prepareStatement(sqlUserAccount)) {
                stmtUserAccount.setInt(1, employee.getId());
                stmtUserAccount.setString(2, employee.getUsername());
                stmtUserAccount.setString(3, employee.getPassword());
                stmtUserAccount.setString(4, employee.getRole());
                stmtUserAccount.executeUpdate();
            }
            String sqlEmployee = "INSERT INTO Employee (employeeID, [name], phone, hourWage, CCCD, birthDate, gender, [image]) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmtEmployee = connection.prepareStatement(sqlEmployee)) {
                stmtEmployee.setInt(1, employee.getId());
                stmtEmployee.setString(2, employee.getName());
                stmtEmployee.setString(3, employee.getPhone());
                stmtEmployee.setDouble(4, employee.getHourlyWage());
                stmtEmployee.setString(5, employee.getCCCD());
                stmtEmployee.setString(6, employee.getBirthDate());
                stmtEmployee.setString(7, employee.getGender());
                stmtEmployee.setString(8, employee.getImage());
                stmtEmployee.executeUpdate();
            }
        }
    }

    @Override
    public void updateEmployee(Employee employee) throws SQLException {
        if (employee == null || employee.getId() <= 0 || employee.getUsername() == null
                || employee.getPassword() == null) {
            throw new IllegalArgumentException("Thông tin nhân viên không hợp lệ");
        }
        try (Connection connection = jdbcUtils.connect()) {
            String sqlUserAccount = "UPDATE UserAccount SET username = ?, [password] = ?, role = ? WHERE ID = ?";
            try (PreparedStatement stmtUserAccount = connection.prepareStatement(sqlUserAccount)) {
                stmtUserAccount.setString(1, employee.getUsername());
                stmtUserAccount.setString(2, employee.getPassword());
                stmtUserAccount.setString(3, employee.getRole());
                stmtUserAccount.setInt(4, employee.getId());
                stmtUserAccount.executeUpdate();
            }
            String sqlEmployee = "UPDATE Employee SET [name] = ?, phone = ?, hourWage = ?, CCCD = ?, birthDate = ?, gender = ?, [image] = ? WHERE employeeID = ?";
            try (PreparedStatement stmtEmployee = connection.prepareStatement(sqlEmployee)) {
                stmtEmployee.setString(1, employee.getName());
                stmtEmployee.setString(2, employee.getPhone());
                stmtEmployee.setDouble(3, employee.getHourlyWage());
                stmtEmployee.setString(4, employee.getCCCD());
                stmtEmployee.setString(5, employee.getBirthDate());
                stmtEmployee.setString(6, employee.getGender());
                stmtEmployee.setString(7, employee.getImage());
                stmtEmployee.setInt(8, employee.getId());
                stmtEmployee.executeUpdate();
            }
        }
    }

    @Override
    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.image, e.employeeID, e.name, es.shiftID, es.startTime, es.endTime, ua.role, es.status " +
                "FROM Employee e " +
                "JOIN EmployeeShift es ON es.employeeID = e.employeeID " +
                "JOIN UserAccount ua ON e.employeeID = ua.ID";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setImage(rs.getString("image"));
                employee.setName(rs.getString("name"));
                employee.setId(rs.getInt("employeeID"));
                EmployeeShift shift = new EmployeeShift();
                shift.setShiftID(rs.getInt("shiftID"));
                Timestamp startTime = rs.getTimestamp("startTime");
                Timestamp endTime = rs.getTimestamp("endTime");
                if (startTime != null && endTime != null) {
                    shift.setStartTime(startTime.toLocalDateTime());
                    shift.setEndTime(endTime.toLocalDateTime());
                }
                shift.setStatus(rs.getString("status"));
                employee.setEmployeeShift(shift);
                employee.setRole(rs.getString("role"));
                employees.add(employee);
            }
        }
        return employees;
    }

    @Override
    public List<Employee> getAllEmployeesToManager() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.employeeID, e.image, e.name, ua.role " +
                "FROM Employee e " +
                "JOIN UserAccount ua ON e.employeeID = ua.ID " +
                "WHERE ua.role NOT IN (N'Quản lí', N'Nghỉ việc')";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getInt("employeeID"));
                employee.setImage(rs.getString("image"));
                employee.setName(rs.getString("name"));
                employee.setRole(rs.getString("role"));
                employees.add(employee);
            }
            employees.sort(Comparator.comparing(Employee::getRole).thenComparing(Employee::getOnlyName));
        }
        return employees;
    }

    @Override
    public String getImgByID(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID nhân viên không hợp lệ");
        }
        String sql = "SELECT image FROM Employee WHERE employeeID = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("image");
                }
            }
        }
        return null;
    }

    @Override
    public void setStatusFromSQL(int id, String status, int shiftID) throws SQLException {
        if (id <= 0 || shiftID <= 0 || status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Thông tin không hợp lệ");
        }
        String sql = "UPDATE EmployeeShift SET status = ? WHERE employeeID = ? AND shiftID = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.setInt(3, shiftID);
            stmt.executeUpdate();
        }
    }

    @Override
    public String getStatusFromSQL(int id, int shiftID) throws SQLException {
        if (id <= 0 || shiftID <= 0) {
            throw new IllegalArgumentException("ID nhân viên hoặc ca làm không hợp lệ");
        }
        String sql = "SELECT status FROM EmployeeShift WHERE employeeID = ? AND shiftID = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, shiftID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("status");
                }
            }
        }
        return null;
    }

    @Override
    public String[] getEachEmployeeShift(int id, JDateChooser startDay, JDateChooser endDay) throws SQLException {
        if (id <= 0 || startDay == null || endDay == null || startDay.getDate() == null || endDay.getDate() == null) {
            throw new IllegalArgumentException("Thông tin không hợp lệ");
        }
        int n = ValidationUtils.CalculateDate(startDay, endDay) + 1;
        String[] shifts = new String[n];
        for (int i = 0; i < n; i++) {
            shifts[i] = "";
        }
        String startDayString = sdf.format(startDay.getDate());
        String endDayString = sdf.format(new Date(endDay.getDate().getTime() + 86400000));
        String sql = "SELECT startTime, endTime FROM EmployeeShift WHERE employeeID = ? AND startTime >= ? AND endTime < ? AND status <> N'Chờ duyệt'";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, startDayString);
            stmt.setString(3, endDayString);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String startTime = rs.getString("startTime").split(" ")[1].substring(0, 5);
                    String endTime = rs.getString("endTime").split(" ")[1].substring(0, 5);
                    int row = ValidationUtils.CalculateDate(startDayString, rs.getString("startTime"));
                    shifts[row] = startTime + " - " + endTime;
                }
            }
        }
        return shifts;
    }

    @Override // Chuỗi regester phần tử có dạng 12:00 - 18:00, trả về mảng 2 chiều
    public String[][] getOnlyRegesterEmployeeShift(int id, JDateChooser startDay, JDateChooser endDay,
            String[] regester) throws SQLException {
        if (id <= 0 || startDay == null || endDay == null || startDay.getDate() == null || endDay.getDate() == null) {
            throw new IllegalArgumentException("Thông tin không hợp lệ");
        }
        int n = regester.length;
        int m = ValidationUtils.CalculateDate(startDay, endDay) + 1;

        String[][] shifts = new String[n][m];

        for (int i = 0; i < n; i++) {
            String startTime = regester[i].split(" ")[0] + ":00";
            String endTime = regester[i].split(" ")[2] + ":00";
            System.out.println(startTime + " " + endTime);

            String startDayString = sdf.format(startDay.getDate());
            String endDayString = sdf.format(new Date(endDay.getDate().getTime() + 86400000));
            String sql = "SELECT endTime, status "
                    + "FROM EmployeeShift "
                    + "WHERE employeeID = ? AND startTime >= ? AND endTime < ? AND FORMAT(startTime, 'HH:mm:ss')= ? AND FORMAT(endTime, 'HH:mm:ss')=?";

            try (Connection connection = jdbcUtils.connect();
                    PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.setString(2, startDayString);
                stmt.setString(3, endDayString);
                stmt.setString(4, startTime);
                stmt.setString(5, endTime);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int row = ValidationUtils.CalculateDate(startDayString, rs.getString("endTime").split(" ")[0]);
                        String status = rs.getString("status");
                        if (status.equalsIgnoreCase("Đã điểm danh"))
                            shifts[i][row] = "Đã điểm danh";
                        if (status.equalsIgnoreCase("Chưa điểm danh")) {
                            // Lấy datetime hiện tại
                            LocalDateTime now = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            String currentDateTime = now.format(formatter);
                            if (ValidationUtils.CompareDateTime(currentDateTime, rs.getString("endTime")) < 0)
                                shifts[i][row] = "Đã duyệt";
                            else
                                shifts[i][row] = "Vắng";
                        }
                        if (status.equalsIgnoreCase("Chờ duyệt"))
                            shifts[i][row] = "Chờ duyệt";
                    }
                }
            }

        }

        return shifts;
    }

    @Override
    public void addShiftToSQL(int id, String dateString, String timeRange, int hourWage) throws SQLException {
        if (id <= 0 || dateString == null || timeRange == null
                || !timeRange.matches("\\d{2}:\\d{2}\\s*-\\s*\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Thông tin ca làm không hợp lệ");
        }
        String[] times = timeRange.split("-");
        String sql = "INSERT INTO EmployeeShift (shiftID, employeeID, startTime, endTime, hourWage) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, getMaxShiftID() + 1);
            stmt.setInt(2, id);
            stmt.setString(3, dateString + " " + times[0].trim() + ":00");
            stmt.setString(4, dateString + " " + times[1].trim() + ":00");
            stmt.setInt(5, hourWage);
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteShiftFromSQL(int id, String dateString) throws SQLException {
        if (id <= 0 || dateString == null || dateString.isEmpty()) {
            throw new IllegalArgumentException("Thông tin không hợp lệ");
        }
        String sql = "DELETE FROM EmployeeShift WHERE employeeID = ? AND CAST(startTime AS DATE) = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, dateString);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateShiftToSQL(int id, String dateString, String timeRange, String lastTimeRange)
            throws SQLException {
        if (id <= 0 || dateString == null || timeRange == null || lastTimeRange == null ||
                !timeRange.matches("\\d{2}:\\d{2}\\s*-\\s*\\d{2}:\\d{2}")
                || !lastTimeRange.matches("\\d{2}:\\d{2}\\s*-\\s*\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Thông tin ca làm không hợp lệ");
        }
        String[] times = timeRange.split("-");
        String[] lastTimes = lastTimeRange.split("-");
        String sql = "UPDATE EmployeeShift SET startTime = ?, endTime = ? WHERE employeeID = ? AND startTime = ? AND endTime = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dateString + " " + times[0].trim() + ":00");
            stmt.setString(2, dateString + " " + times[1].trim() + ":00");
            stmt.setInt(3, id);
            stmt.setString(4, dateString + " " + lastTimes[0].trim() + ":00");
            stmt.setString(5, dateString + " " + lastTimes[1].trim() + ":00");
            stmt.executeUpdate();
        }
    }

    @Override
    public void quitJob(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID nhân viên không hợp lệ");
        }
        String sql = "UPDATE UserAccount SET role = N'Nghỉ việc' WHERE ID = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Employee> getAllEmployeesAllAttributes() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.employeeID, e.name, e.phone, e.image, u.username, u.password, u.role, e.CCCD, e.birthDate, e.gender, e.hourWage "
                +
                "FROM Employee e " +
                "JOIN UserAccount u ON e.employeeID = u.ID " +
                "WHERE u.role NOT IN (N'Quản lí', N'Nghỉ việc')";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("employeeID"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("image"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("CCCD"),
                        rs.getString("birthDate"),
                        rs.getString("gender"),
                        rs.getInt("hourWage")));
            }
        }
        return employees;
    }

    @Override
    public EmployeeShift getEmployeeShiftByEmployeeID(int employeeID) throws SQLException {
        if (employeeID <= 0) {
            throw new IllegalArgumentException("ID nhân viên không hợp lệ");
        }
        String sql = "SELECT startTime, endTime FROM EmployeeShift WHERE employeeID = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp start = rs.getTimestamp("startTime");
                    Timestamp end = rs.getTimestamp("endTime");
                    if (start != null && end != null) {
                        return new EmployeeShift(start.toLocalDateTime(), end.toLocalDateTime());
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<Employee> getShiftsBetweenDates(LocalDate fromDate, LocalDate toDate) throws SQLException {
        if (fromDate == null || toDate == null || fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("Ngày không hợp lệ");
        }
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT DISTINCT e.employeeID, e.name, e.phone, e.image, u.username, u.password, u.role, e.CCCD, e.birthDate, e.gender, e.hourWage "
                +
                "FROM EmployeeShift es " +
                "JOIN Employee e ON es.employeeID = e.employeeID " +
                "JOIN UserAccount u ON e.employeeID = u.ID " +
                "WHERE es.startTime BETWEEN ? AND ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(fromDate.atStartOfDay()));
            stmt.setTimestamp(2, Timestamp.valueOf(toDate.plusDays(1).atStartOfDay()));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(new Employee(
                            rs.getInt("employeeID"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("image"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("CCCD"),
                            rs.getString("birthDate"),
                            rs.getString("gender"),
                            rs.getInt("hourWage")));
                }
            }
        }
        return employees;
    }

    @Override
    public void requestUpdateInforEmployee(int empID, String phone, String username, String password, String birthday,
            String image) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Kết nối đến database
            connection = jdbcUtils.connect();

            // Lấy changeID lớn nhất hiện tại và tăng thêm 1
            String maxIdQuery = "SELECT MAX(changeID) FROM ChangeInfoEmployee";
            stmt = connection.prepareStatement(maxIdQuery);
            rs = stmt.executeQuery();

            int changeID = 1; // Mặc định là 1 nếu bảng trống
            if (rs.next() && rs.getInt(1) > 0) {
                changeID = rs.getInt(1) + 1;
            }

            // Đóng ResultSet và PreparedStatement để tái sử dụng
            rs.close();
            stmt.close();

            // Chuẩn bị câu lệnh SQL để thêm dữ liệu
            String insertQuery = "INSERT INTO ChangeInfoEmployee (changeID, employeeID, phone, username, password, birthday, [image], status) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, N'Chờ duyệt')";

            stmt = connection.prepareStatement(insertQuery);
            stmt.setInt(1, changeID);
            stmt.setInt(2, empID);
            stmt.setString(3, phone);

            // Kiểm tra các giá trị có thể null và xử lý tương ứng
            if (username == null || username.isEmpty()) {
                stmt.setNull(4, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(4, username);
            }

            if (password == null || password.isEmpty()) {
                stmt.setNull(5, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(5, password);
            }

            if (birthday == null || birthday.isEmpty()) {
                stmt.setNull(6, java.sql.Types.DATE);
            } else {
                stmt.setString(6, birthday);
            }

            if (image == null || image.isEmpty()) {
                stmt.setNull(7, java.sql.Types.NVARCHAR);
            } else {
                stmt.setString(7, image);
            }

            // Thực thi câu lệnh SQL
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Đã thêm yêu cầu cập nhật thông tin nhân viên với ID: " + changeID);
            } else {
                System.out.println("Không thể thêm yêu cầu cập nhật thông tin nhân viên!");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm yêu cầu cập nhật thông tin nhân viên: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Không thể thêm yêu cầu cập nhật: " + e.getMessage());
        }
    }

    @Override
    public Employee getEmployeeInfor(int empID) throws SQLException {
        String sql = "SELECT e.employeeID, e.name, e.phone, e.image, u.username, u.password, e.CCCD, e.birthDate, e.gender, u.role "
                +
                "FROM Employee e " +
                "JOIN UserAccount u ON e.employeeID = u.ID " +
                "WHERE e.employeeID = ?";

        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, empID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getInt("employeeID"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("image"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("CCCD"),
                            rs.getString("birthDate"),
                            rs.getString("gender"),
                            0);
                }
            }
        }
        // Nếu không tìm thấy thông tin, trả về null
        return null;
    }

    @Override
    public void acceptUpdateInforEmployee(int changeID, String request) throws SQLException {
        Connection connection = null;
        PreparedStatement stmtSelect = null;
        PreparedStatement stmtUpdateEmployee = null;
        PreparedStatement stmtUpdateUserAccount = null;
        PreparedStatement stmtUpdateStatus = null;
        ResultSet rs = null;

        try {
            // Kết nối đến database
            connection = jdbcUtils.connect();
            // Bắt đầu transaction
            connection.setAutoCommit(false);

            // Kiểm tra action là chấp nhận hay từ chối
            if (request.equalsIgnoreCase("Từ chối")) {
                // Nếu từ chối, chỉ cập nhật trạng thái thành "Đã từ chối"
                String updateStatusQuery = "UPDATE ChangeInfoEmployee SET status = N'Đã từ chối' WHERE changeID = ?";
                stmtUpdateStatus = connection.prepareStatement(updateStatusQuery);
                stmtUpdateStatus.setInt(1, changeID);
                stmtUpdateStatus.executeUpdate();

                // Commit transaction
                connection.commit();
                System.out.println("Đã từ chối yêu cầu cập nhật thông tin có ID: " + changeID);
                return;
            }

            // Lấy thông tin từ bảng ChangeInfoEmployee
            String selectQuery = "SELECT employeeID, phone, username, password, birthday, [image] " +
                    "FROM ChangeInfoEmployee WHERE changeID = ? AND status = N'Chờ duyệt'";
            stmtSelect = connection.prepareStatement(selectQuery);
            stmtSelect.setInt(1, changeID);
            rs = stmtSelect.executeQuery();

            if (rs.next()) {
                int employeeID = rs.getInt("employeeID");
                String phone = rs.getString("phone");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String birthday = rs.getString("birthday");
                String image = rs.getString("image");

                // Cập nhật thông tin nhân viên trong bảng Employee
                if (phone != null || birthday != null || image != null) {
                    StringBuilder sqlEmployee = new StringBuilder("UPDATE Employee SET ");
                    boolean needComma = false;

                    if (phone != null) {
                        sqlEmployee.append("phone = ?");
                        needComma = true;
                    }
                    if (birthday != null) {
                        if (needComma)
                            sqlEmployee.append(", ");
                        sqlEmployee.append("birthDate = ?");
                        needComma = true;
                    }
                    if (image != null) {
                        if (needComma)
                            sqlEmployee.append(", ");
                        sqlEmployee.append("[image] = ?");
                    }
                    sqlEmployee.append(" WHERE employeeID = ?");

                    stmtUpdateEmployee = connection.prepareStatement(sqlEmployee.toString());
                    int paramIndex = 1;

                    if (phone != null) {
                        stmtUpdateEmployee.setString(paramIndex++, phone);
                    }
                    if (birthday != null) {
                        stmtUpdateEmployee.setString(paramIndex++, birthday);
                    }
                    if (image != null) {
                        stmtUpdateEmployee.setString(paramIndex++, image);
                    }
                    stmtUpdateEmployee.setInt(paramIndex, employeeID);
                    stmtUpdateEmployee.executeUpdate();
                }

                // Cập nhật thông tin tài khoản trong bảng UserAccount nếu có
                if (username != null || password != null) {
                    StringBuilder sqlUserAccount = new StringBuilder("UPDATE UserAccount SET ");
                    boolean needComma = false;

                    if (username != null) {
                        sqlUserAccount.append("username = ?");
                        needComma = true;
                    }
                    if (password != null) {
                        if (needComma)
                            sqlUserAccount.append(", ");
                        sqlUserAccount.append("[password] = ?");
                    }
                    sqlUserAccount.append(" WHERE ID = ?");

                    stmtUpdateUserAccount = connection.prepareStatement(sqlUserAccount.toString());
                    int paramIndex = 1;

                    if (username != null) {
                        stmtUpdateUserAccount.setString(paramIndex++, username);
                    }
                    if (password != null) {
                        stmtUpdateUserAccount.setString(paramIndex++, password);
                    }
                    stmtUpdateUserAccount.setInt(paramIndex, employeeID);
                    stmtUpdateUserAccount.executeUpdate();
                }

                // Cập nhật trạng thái của yêu cầu thành "Đã duyệt"
                String updateStatusQuery = "UPDATE ChangeInfoEmployee SET status = N'Đã duyệt' WHERE changeID = ?";
                stmtUpdateStatus = connection.prepareStatement(updateStatusQuery);
                stmtUpdateStatus.setInt(1, changeID);
                stmtUpdateStatus.executeUpdate();

                // Commit transaction nếu tất cả các thao tác đều thành công
                connection.commit();
                System.out.println("Đã duyệt và cập nhật thông tin nhân viên với changeID: " + changeID);
            } else {
                System.out.println("Không tìm thấy yêu cầu cập nhật thông tin có ID: " + changeID
                        + " hoặc yêu cầu đã được duyệt/từ chối");
            }
        } catch (SQLException e) {
            // Rollback transaction nếu có lỗi
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("Lỗi khi duyệt yêu cầu cập nhật thông tin: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Không thể duyệt yêu cầu cập nhật: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            EmployeeRespository employeeRepository = new EmployeeRespository();
            List<Employee> employees = employeeRepository.getAllEmployees();
            for (Employee employee : employees) {
                System.out.println(employee.getImage() + " " + employee.getName() + " " +
                        employee.getEmployeeShift().getShiftID() + " Thời gian bắt đầu: " +
                        employee.getEmployeeShift().getStartTime() + " Thời gian kết thúc: " +
                        employee.getEmployeeShift().getEndTime() + " " + employee.getRole() +
                        " ID: " + employee.getId() + " Status: " + employee.getEmployeeShift().getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}