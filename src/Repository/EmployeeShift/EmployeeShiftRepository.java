package Repository.EmployeeShift;

import Model.EmployeeShift;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmployeeShiftRepository implements IEmployeeShiftRepository {
    private final JdbcUtils jdbcUtils;

    public EmployeeShiftRepository() throws IOException, ClassNotFoundException, SQLException {
        this.jdbcUtils = new JdbcUtils();
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
    public List<EmployeeShift> getAllEmployeeShift() throws SQLException {
        List<EmployeeShift> shiftList = new ArrayList<>();
        String sql = "SELECT shiftID, employeeID, startTime, endTime, hourWage FROM EmployeeShift";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int shiftID = rs.getInt("shiftID");
                int employeeID = rs.getInt("employeeID");
                LocalDateTime startTime = rs.getTimestamp("startTime") != null ? rs.getTimestamp("startTime").toLocalDateTime() : null;
                LocalDateTime endTime = rs.getTimestamp("endTime") != null ? rs.getTimestamp("endTime").toLocalDateTime() : null;
                double hourlyWage = rs.getDouble("hourWage");
                EmployeeShift employeeShift = new EmployeeShift(shiftID, employeeID, startTime, endTime, hourlyWage);
                shiftList.add(employeeShift);
            }
        }
        return shiftList;
    }

    @Override
    public double getHourlyWageByEmployeeID(int employeeID) throws SQLException {
        if (employeeID <= 0) {
            throw new IllegalArgumentException("ID nhân viên không hợp lệ");
        }
        String sql = "SELECT hourWage FROM EmployeeShift WHERE employeeID = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("hourWage");
                }
            }
        }
        return 0.0;
    }

    @Override
    public List<EmployeeShift> getShiftsByEmployeeIDBetweenDates(int employeeID, LocalDate fromDate, LocalDate toDate) throws SQLException {
        if (employeeID <= 0 || fromDate == null || toDate == null || fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("Thông tin không hợp lệ");
        }
        List<EmployeeShift> shiftList = new ArrayList<>();
        String sql = "SELECT shiftID, employeeID, startTime, endTime, hourWage FROM EmployeeShift WHERE employeeID = ? AND startTime BETWEEN ? AND ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeID);
            stmt.setDate(2, Date.valueOf(fromDate));
            stmt.setDate(3, Date.valueOf(toDate.plusDays(1))); // Bao gồm cả ngày cuối
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int shiftID = rs.getInt("shiftID");
                    LocalDateTime startTime = rs.getTimestamp("startTime") != null ? rs.getTimestamp("startTime").toLocalDateTime() : null;
                    LocalDateTime endTime = rs.getTimestamp("endTime") != null ? rs.getTimestamp("endTime").toLocalDateTime() : null;
                    double hourlyWage = rs.getDouble("hourWage");
                    EmployeeShift shift = new EmployeeShift(shiftID, employeeID, startTime, endTime, hourlyWage);
                    shiftList.add(shift);
                }
            }
        }
        return shiftList;
    }
    
    @Override
    public void addRegister(int employeeId, String startTime, String endTime, int hourWage, String status) throws SQLException {
    	if (employeeId <=0 || startTime == null || endTime == null || hourWage<0 || status == null) {
    		 throw new IllegalArgumentException("Thông tin không hợp lệ");
    	}
    	String sql = "INSERT INTO EmployeeShift(shiftID,employeeID, startTime, endTime, hourWage, status) "
    				+ "VALUES (?, ?, ?, ?, ?, ?)";
    	try (Connection connection = jdbcUtils.connect();
    		PreparedStatement stmt = connection.prepareStatement(sql)){
    		stmt.setInt(1, this.getMaxShiftID()+1);
    		stmt.setInt(2, employeeId);
    		stmt.setString(3, startTime);
    		stmt.setString(4, endTime);
    		stmt.setInt(5, hourWage);
    		stmt.setString(6, status);
    		stmt.executeUpdate();
    	}
		
    }
}