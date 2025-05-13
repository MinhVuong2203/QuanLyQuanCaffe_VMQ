package Repository.Table;

import Model.Table;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableRepository implements ITableRespository {
    private final JdbcUtils jdbcUtils;

    public TableRepository() throws IOException, ClassNotFoundException, SQLException {
        this.jdbcUtils = new JdbcUtils();
    }

    @Override
    public List<Table> getTableFromSQL() throws SQLException {
        List<Table> listTables = new ArrayList<>();
        String sql = "SELECT tableID, tableName, status FROM TableCaffe WHERE tableID != 0";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                listTables.add(new Table(rs.getInt("tableID"), rs.getString("tableName"), rs.getString("status")));
            }
        }
        return listTables;
    }

    @Override
    public void insertTable(Table table) throws SQLException {
        if (table == null || table.getTableID() <= 0 || table.getTableName() == null || table.getStatus() == null) {
            throw new IllegalArgumentException("Thông tin bàn không hợp lệ");
        }
        String sql = "INSERT INTO TableCaffe (tableID, tableName, status) VALUES (?, ?, ?)";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, table.getTableID());
            stmt.setString(2, table.getTableName());
            stmt.setString(3, table.getStatus());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateTable(int id, String name) throws SQLException {
        if (id <= 0 || name == null || name.isEmpty()) {
            throw new IllegalArgumentException("ID hoặc tên bàn không hợp lệ");
        }
        String sql = "UPDATE TableCaffe SET tableName = ? WHERE tableID = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateTableStatus(int tableId, String status) throws SQLException {
        if (tableId <= 0 || status == null || status.isEmpty()) {
            throw new IllegalArgumentException("ID bàn hoặc trạng thái không hợp lệ");
        }
        String sql = "UPDATE TableCaffe SET status = ? WHERE tableID = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, tableId);
            stmt.executeUpdate();
        }
    }

    @Override
    public int getTableIDByName(String name) throws SQLException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Tên bàn không hợp lệ");
        }
        String sql = "SELECT tableID FROM TableCaffe WHERE tableName = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("tableID");
                }
            }
        }
        return -1; // Không tìm thấy bàn
    }

    @Override
    public String getTableStatus(int tableId) throws SQLException {
        if (tableId <= 0) {
            throw new IllegalArgumentException("ID bàn không hợp lệ");
        }
        String sql = "SELECT status FROM TableCaffe WHERE tableID = ?";
        try (Connection connection = jdbcUtils.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, tableId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("status");
                }
            }
        }
        return null; // Không tìm thấy bàn
    }
}