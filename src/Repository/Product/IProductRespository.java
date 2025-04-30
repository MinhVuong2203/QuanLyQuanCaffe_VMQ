package Repository.Product;

import Model.Product;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IProductRespository {
    public List<Product> getArrayListProductFromSQL() throws SQLException;
    public Product getProductByID(int id) throws SQLException;
    public void addProductToOrderDetail(int orderId, int productId, int quantity, double price, int tableID) throws SQLException;
    public void updateOrderDetail(int orderId, int productId, int quantity, double price) throws SQLException;
    public int getProductIdByNameAndSize(String name, String size) throws SQLException;
    public int getProductIdByName(String name) throws SQLException;
    public void deleteProductFromOrderDetail(int orderId, int productId) throws SQLException;
    public void addToOrder(int orderID, int tableID, int employeeID, int customerID, String orderTime) throws SQLException;
    public void delOrder(int orderID, int tableID) throws SQLException;
    public int initTempOrderId() throws SQLException;
    public Product getProductByName(String name) throws SQLException;
    public int getOrderIDByTableID(int TableID) throws SQLException;
    public void delProductByID(int productID) throws SQLException;
    public Map<String, Object> getBillInfoByTableID(int tableID) throws SQLException;
    public void addProduct(Product product) throws SQLException;
    public void updateProduct(Product product) throws SQLException;
    public void updateTableStatus(int tableID, String status) throws SQLException;
    public void updateOrder(int orderID, int tableID, int employeeID, int customerID, String orderTime) throws SQLException;
    public void updateOrderStatus(int orderID, String status) throws SQLException;
}
