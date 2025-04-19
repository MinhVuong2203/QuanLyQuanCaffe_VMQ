package Service.Table;

import Model.Table;
import Repository.Table.ITableRespository;
import Repository.Table.TableRepository;
import Utils.ValidationUtils;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TableService implements ITableService {

    private final ITableRespository tableRepository;
    private final List<Table> listTable;

    public TableService(List<Table> listTable) throws ClassNotFoundException, IOException, SQLException {
        this.listTable = listTable;
        this.tableRepository = new TableRepository();
    }

    // Thêm bàn mới
    @Override
    public void addTable(int id, String name) throws SQLException, ClassNotFoundException{
        if (ValidationUtils.indexListTableID(listTable, id) != -1) {
            throw new IllegalArgumentException("ID này đã tồn tại!");
        }
        if (ValidationUtils.indexListTableName(listTable, name)) {
            throw new IllegalArgumentException("Tên bàn này đã tồn tại!");
        }

        Table table = new Table(id, name, "Trống");
        tableRepository.insertTable(table);
        listTable.add(table);
    }

    @Override
    // Cập nhật trạng thái bàn (bảo trì/gỡ bảo trì)
    public void updateTableStatus(int id, boolean isMaintenance) throws SQLException, ClassNotFoundException {
        int index = ValidationUtils.indexListTableID(listTable, id);
        if (index == -1) {
            throw new IllegalArgumentException("ID không tồn tại!");
        }

        Table table = listTable.get(index);
        if (isMaintenance && "Bảo trì".equalsIgnoreCase(table.getStatus())) {
            throw new IllegalStateException("Bàn đã ở trạng thái bảo trì!");
        }
        if (!isMaintenance && !"Bảo trì".equalsIgnoreCase(table.getStatus())) {
            throw new IllegalStateException("Bàn không ở trạng thái bảo trì!");
        }

        String newStatus = isMaintenance ? "Bảo trì" : "Trống";
        tableRepository.updateTableStatus(id, newStatus);
        table.setStatus(newStatus);
    }

    @Override
    // Cập nhật tên bàn
    public void updateTableName(int id, String name) throws SQLException, ClassNotFoundException {
        int index = ValidationUtils.indexListTableID(listTable, id);
        if (index == -1) {
            throw new IllegalArgumentException("ID không tồn tại!");
        }
        if (ValidationUtils.indexListTableName(listTable, name)) {
            throw new IllegalArgumentException("Tên bàn này đã tồn tại!");
        }

        tableRepository.updateTable(id, name);
        listTable.get(index).setTableName(name);
    }

    @Override
    // Xác thực ID bàn
    public boolean isValidTableId(String idText) {
        if (idText.isEmpty() || !ValidationUtils.isNumeric(idText)) {
            return false;
        }
        return ValidationUtils.indexListTableID(listTable, Integer.parseInt(idText)) != -1;
    }

    @Override
    // Xác thực đầu vào cho thêm bàn
    public boolean isValidAddTableInput(String idText, String name) {
        if (idText.isEmpty() || name.isEmpty() || !ValidationUtils.isNumeric(idText)) {
            return false;
        }
        int id = Integer.parseInt(idText);
        return ValidationUtils.indexListTableID(listTable, id) == -1 &&
               !ValidationUtils.indexListTableName(listTable, name);
    }

    @Override
    // Xác thực đầu vào cho sửa bàn
    public boolean isValidFixTableInput(String idText, String name) {
        if (idText.isEmpty() || name.isEmpty() || !ValidationUtils.isNumeric(idText)) {
            return false;
        }
        return isValidTableId(idText) && !ValidationUtils.indexListTableName(listTable, name);
    }

    public List<Table> getListTable() {
        return listTable;
    }
}
