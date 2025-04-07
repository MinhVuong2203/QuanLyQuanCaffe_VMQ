package Repository;

import Model.Table;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TableRepository {
    private Connection connection;
    private JdbcUtils jdbcUtils;

    public TableRepository() throws ClassNotFoundException, IOException, SQLException {
        this.jdbcUtils = new JdbcUtils();
    }

    public List<Table> getTableFromSQL() throws SQLException, ClassNotFoundException{
        List<Table> listTables = new ArrayList<>();
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM TableCaffe";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                listTables.add(new Table(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            rs.close();
            stmt.close();
            return listTables;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
			connection.close();
		}
        return null;
    }
    
    public void insertTable(Table table) throws SQLException, ClassNotFoundException{
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO TableCaffe (tableID, tableName, status) VALUES (" + table.getTableID() + ", '" + table.getTableName() + "', N'" + table.getStatus() + "')";
            stmt.executeUpdate(sql);
            stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connection.close(); 
        }
    }
    
}

