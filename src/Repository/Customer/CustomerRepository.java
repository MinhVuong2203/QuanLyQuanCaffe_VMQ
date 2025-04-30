package Repository.Customer;

import Model.Customer;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerRepository implements ICustomerRespository {
    private Connection connection;
    private JdbcUtils jdbcUtils;

    public CustomerRepository() throws IOException, ClassNotFoundException, SQLException{
        jdbcUtils = new JdbcUtils();
    }

    @Override
    public int getIDMaxFromSQL() throws SQLException{
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT MAX(customerID) FROM Customer";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt(1); // Trả về ID lớn nhất
        } catch (ClassNotFoundException | SQLException e){
        } finally {
            connection.close();
        }
        return 0; // Không có ID nào
    }

    @Override
    public boolean checkEqualsPhone(String phone) throws SQLException{
        try{
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT [phone]\r\n" + //
                        "FROM [Customer]\r\n" + //
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

    @Override
    public void addCustomer(String name, String phone) throws SQLException{
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sqlMaxID = "SELECT MAX(customerID) FROM Customer";
            ResultSet rs = stmt.executeQuery(sqlMaxID);
            int id = 10000;
            if (rs.next()) id = rs.getInt(1) + 1;
            String sql = "INSERT INTO Customer (customerID, name, phone, point) VALUES (" + id +", N'" + name + "', '" + phone + "'," + 30 + ")";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    @Override
    public int getCustomerIDByPhone(String phone) throws SQLException{
        try {
            connection = jdbcUtils.connect();
            Statement stmt = connection.createStatement();
            String sql = "SELECT customerID FROM Customer WHERE phone = '" + phone + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt("customerID");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return 0; // Không có ID nào
    }

    @Override
    public double plusPoint(int customerID, double money) throws SQLException{
        try {
            connection = jdbcUtils.connect();
            Statement stmt = connection.createStatement();
            String sql = "UPDATE Customer SET point = point + " + money / 100000 + " WHERE customerID = " + customerID;
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Customer getCustomerByPhone(String phone) throws SQLException{
        try {
            connection = jdbcUtils.connect();
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM Customer WHERE phone = '" + phone + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int id = rs.getInt("customerID");
                String name = rs.getString("name");
                double point = rs.getDouble("point");
                return new Customer(id, name, phone, point);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return null; // Không tìm thấy khách hàng
    }

    @Override
     public void updatePoint(int customerID, double newPoints) throws SQLException, ClassNotFoundException {
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement pstmt = connection.prepareStatement("UPDATE Customer SET point = ? WHERE customerID = ?")) {
            pstmt.setDouble(1, newPoints);
            pstmt.setInt(2, customerID);
            pstmt.executeUpdate();
        }
    }

	

	

	


}
