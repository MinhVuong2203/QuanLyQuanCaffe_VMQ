package Repository.Table;

import Model.Table;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TableRepository implements ITableRespository {
    private Connection connection;
    private JdbcUtils jdbcUtils;

    public TableRepository() throws ClassNotFoundException, IOException, SQLException {
        this.jdbcUtils = new JdbcUtils();
    }

    @Override
    public List<Table> getTableFromSQL() throws SQLException, ClassNotFoundException {
        List<Table> listTables = new ArrayList<>();
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM TableCaffe";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (rs.getInt(1) == 0)
                    continue; // Bỏ qua tableID = 0
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

    @Override
    public void insertTable(Table table) throws SQLException, ClassNotFoundException {
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO TableCaffe (tableID, tableName, status) VALUES (" + table.getTableID() + ", '"
                    + table.getTableName() + "', N'" + table.getStatus() + "')";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    @Override
    public void updateTable(int id, String name) throws SQLException, ClassNotFoundException {
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "UPDATE TableCaffe SET tableName = '" + name + "' WHERE tableID = " + id;
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close(); // Đóng kết nối
        }
    }

    @Override
    public void updateTableStatus(int tableId, String status) throws SQLException, ClassNotFoundException {
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "UPDATE TableCaffe SET status = N'" + status + "' WHERE tableID = " + tableId;
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close(); // Đóng kết nối
        }
    }

    @Override
    public int getTableIDByName(String name) throws SQLException, ClassNotFoundException {
        try {
            connection = jdbcUtils.connect();
            Statement stmt = connection.createStatement();
            String sql = "SELECT tableID FROM TableCaffe WHERE tableName = N'" + name + "'";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                int tableId = rs.getInt("tableID");
                rs.close();
                stmt.close();
                return tableId;
            }

            rs.close();
            stmt.close();
            return -1; // Return -1 if table not found
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public String getTableStatus(int tableId) throws SQLException, ClassNotFoundException {
        try {
            connection = jdbcUtils.connect();
            Statement stmt = connection.createStatement();
            String sql = "SELECT status FROM TableCaffe WHERE tableID = " + tableId;
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String status = rs.getString("status");
                rs.close();
                stmt.close();
                return status;
            }

            rs.close();
            stmt.close();
            return null; // Return null if table not found
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
