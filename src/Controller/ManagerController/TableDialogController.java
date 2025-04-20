package Controller.ManagerController;

import Model.Table;
import Service.Implements.TableService;
import Utils.ValidationUtils;
import View.ManagerView.ManagerTable.TablePanel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class TableDialogController {

    private final TablePanel tablePanel;
    private final TableService tableService;

    public TableDialogController(List<Table> listTable, TablePanel tablePanel) throws ClassNotFoundException, IOException, SQLException {
        this.tablePanel = tablePanel;
        this.tableService = new TableService(listTable);
    }

    // Xác thực và thêm bàn mới (cho AddTableJDialog)
    public boolean validateAddTableInput(String id, String name, JLabel infoID, JLabel infoName) {
        infoID.setText("");
        infoName.setText("");

        if (!tableService.isValidAddTableInput(id, name)) {
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else if (!ValidationUtils.isNumeric(id)) {
                JOptionPane.showMessageDialog(null, "ID không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else if (ValidationUtils.indexListTableID(tableService.getListTable(), Integer.parseInt(id)) != -1) {
                infoID.setText("ID này đã tồn tại!");
            } else if (ValidationUtils.indexListTableName(tableService.getListTable(), name)) {
                infoName.setText("Tên bàn này đã tồn tại!");
            }
            return false;
        }
        return true;
    }

    public void addTable(String id, String name) throws SQLException, ClassNotFoundException {
        try {
            tableService.addTable(Integer.parseInt(id), name);
            tablePanel.updateTableData(tableService.getListTable());
            JOptionPane.showMessageDialog(null, "Thêm bàn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Xác thực ID bàn (cho MaintenanceDialog và FixTableDialog)
    private boolean validateTableId(String idText, JLabel infoID) {
        infoID.setText("");

        if (!tableService.isValidTableId(idText)) {
            if (idText.isEmpty()) {
                infoID.setText("Vui lòng nhập ID!");
            } else if (!ValidationUtils.isNumeric(idText)) {
                infoID.setText("ID không hợp lệ!");
            } else {
                infoID.setText("ID không tồn tại!");
            }
            return false;
        }
        return true;
    }

    // Xử lý bảo trì/gỡ bảo trì (cho MaintenanceDialog)
    public boolean validateMaintenanceInput(String idText, boolean isMaintenance, JLabel infoID) {
        if (!validateTableId(idText, infoID)) {
            return false;
        }

        try {
            int id = Integer.parseInt(idText);
            int index = ValidationUtils.indexListTableID(tableService.getListTable(), id);
            String currentStatus = tableService.getListTable().get(index).getStatus();

            if (isMaintenance && "Bảo trì".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(null, "Bàn đã ở trạng thái bảo trì!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            if (!isMaintenance && !"Bảo trì".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(null, "Bàn không ở trạng thái bảo trì!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi kiểm tra trạng thái: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void updateTableStatus(String idText, boolean isMaintenance) throws SQLException, NumberFormatException, ClassNotFoundException {
        tableService.updateTableStatus(Integer.parseInt(idText), isMaintenance);
        tablePanel.updateTableData(tableService.getListTable());
        String message = isMaintenance ? "Đã chuyển bàn sang trạng thái Bảo trì." : "Đã chuyển bàn về trạng thái Trống.";
        JOptionPane.showMessageDialog(null, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    // Xác thực và sửa thông tin bàn (cho FixTableDialog)
    public boolean validateFixTableInput(String idText, String nameText, JLabel infoID, JLabel infoName) {
        infoID.setText("");
        infoName.setText("");

        if (!tableService.isValidFixTableInput(idText, nameText)) {
            if (idText.isEmpty() || nameText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else if (!ValidationUtils.isNumeric(idText)) {
                infoID.setText("ID không hợp lệ!");
            } else if (!tableService.isValidTableId(idText)) {
                infoID.setText("ID không tồn tại!");
            } else if (ValidationUtils.indexListTableName(tableService.getListTable(), nameText)) {
                infoName.setText("Tên bàn này đã tồn tại!");
            }
            return false;
        }
        return true;
    }

    public void updateTableName(String idText, String nameText) throws SQLException, ClassNotFoundException {
        try {
            tableService.updateTableName(Integer.parseInt(idText), nameText);
            tablePanel.updateTableData(tableService.getListTable());
            JOptionPane.showMessageDialog(null, "Cập nhật tên bàn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
