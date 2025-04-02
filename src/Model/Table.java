package Model;

public class Table {
    private int tableID;  // Nếu tableID = 0 thì là đơn hàng giao hàng
    private String tableName;
    private String status; // Nếu Bàn trống thì status = "Trống", Bàn đã có người thì status = "Đã có người"

    public Table(int tableID, String tableName, String status) {
        this.tableID = tableID;
        this.tableName = tableName;
        this.status = status;
    }

    public int getTableID() {
        return tableID;
    }

    public String getTableName() {
        return tableName;
    }

    public String getStatus() {
        return status;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Table [tableID = " + tableID + ", tableName = " + tableName + ", status = " + status + "]";
    }
}
