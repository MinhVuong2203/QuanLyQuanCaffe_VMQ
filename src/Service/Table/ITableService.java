package Service.Table;

import java.sql.SQLException;

public interface ITableService {
    public void addTable(int id, String name) throws SQLException, ClassNotFoundException;
    public void updateTableStatus(int id, boolean isMaintenance) throws SQLException, ClassNotFoundException;
    public void updateTableName(int id, String name) throws SQLException, ClassNotFoundException;
    public boolean isValidTableId(String idText);
    public boolean isValidAddTableInput(String idText, String name);
    public boolean isValidFixTableInput(String idText, String name);

}
