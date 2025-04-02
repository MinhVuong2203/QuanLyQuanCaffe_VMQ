package Repository;

import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}

