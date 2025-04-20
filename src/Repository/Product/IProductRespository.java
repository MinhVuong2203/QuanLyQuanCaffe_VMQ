package Repository.Product;

import Model.Product;
import java.sql.SQLException;
import java.util.List;

public interface IProductRespository {
    public List<Product> getArrayListProductFromSQL() throws SQLException;
    public Product getProductByID(int id) throws SQLException;
    public void addProductToOrderDetail(int orderId, int productId, int quantity, double price, int tableID) throws SQLException;
    public void updateOrderDetail(int orderId, int productId, int quantity, double price) throws SQLException;
    public int getProductIdByNameAndSize(String name, String size) throws SQLException;
    public int getProductIdByName(String name) throws SQLException;
    public void deleteProductFromOrderDetail(int orderId, int productId) throws SQLException;
    public void addToOrder(int orderID, int tableID, int employeeID, int customerID, String orderTime) throws SQLException;
    public void delToOrder(int orderID, int tableID) throws SQLException;
}
