package Repository.EmployeeShift;


import Model.EmployeeShift;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class EmployeeShiftRepository implements IEmployeeShiftRepository {
    private Connection connection;
    private JdbcUtils jdbcUtils;

    public EmployeeShiftRepository() throws IOException, ClassNotFoundException, SQLException {
        jdbcUtils = new JdbcUtils();
    }


    @Override
    public List<EmployeeShift> getAllEmployeeShift() throws SQLException, IOException, ClassNotFoundException {
        List<EmployeeShift> EmployeeList = new ArrayList<>();
        try {
            connection = jdbcUtils.connect();
            String sql = "SELECT * FROM EmployeeShift";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int shiftID = resultSet.getInt("shiftID");
                int employeeID = resultSet.getInt("employeeID");
                LocalDateTime startTime = resultSet.getTimestamp("startTime").toLocalDateTime();
                LocalDateTime endTime = resultSet.getTimestamp("endTime").toLocalDateTime();
                double hourlyWage = resultSet.getDouble("hourWage");

                EmployeeShift employeeShift = new EmployeeShift(shiftID, employeeID, startTime, endTime, hourlyWage);
                EmployeeList.add(employeeShift);
            }

            statement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return EmployeeList;
    
    }
    @Override
    public double getHourlyWageByEmployeeID(int employeeID) throws SQLException, IOException, ClassNotFoundException {
        double hourlyWage = 0.0;
        try {
            connection = jdbcUtils.connect();
            String sql = "SELECT hourlyWage FROM EmployeeShift WHERE employeeID = " + employeeID;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                hourlyWage = resultSet.getDouble("hourlyWage");
            }

            statement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return hourlyWage;
    }
    public List<EmployeeShift> getShiftsByEmployeeIDBetweenDates(int employeeID, LocalDate fromDate, LocalDate toDate) throws SQLException, ClassNotFoundException {
    List<EmployeeShift> shiftList = new ArrayList<>();
    String sql = "SELECT * FROM EmployeeShift WHERE employeeID = ? AND startTime BETWEEN ? AND ?";

    try (Connection connection = jdbcUtils.connect();
         PreparedStatement stmt = connection.prepareStatement(sql)) {

        stmt.setInt(1, employeeID);
        stmt.setDate(2, Date.valueOf(fromDate));
        stmt.setDate(3, Date.valueOf(toDate));

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                
                LocalDateTime startTime = rs.getTimestamp("startTime").toLocalDateTime();
                LocalDateTime endTime = rs.getTimestamp("endTime").toLocalDateTime();

                EmployeeShift shift = new EmployeeShift(employeeID, startTime, endTime);
                shiftList.add(shift);
            }
        }
    }
    return shiftList;
}

}