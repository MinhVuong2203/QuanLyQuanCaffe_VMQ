package Repository.Table;

import Model.Table;
import java.sql.SQLException;
import java.util.List;

public interface ITableRespository {
    public List<Table> getTableFromSQL() throws SQLException, ClassNotFoundException;
    public void insertTable(Table table) throws SQLException, ClassNotFoundException;
    public void updateTable(int id, String name) throws SQLException, ClassNotFoundException;
    public void updateTableStatus(int tableId, String status) throws SQLException, ClassNotFoundException;
}
