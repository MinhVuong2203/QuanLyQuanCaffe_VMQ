package Repository.Product;

import Model.Product;
import java.sql.SQLException;
import java.util.List;

public interface IProductRespository {
    public List<Product> getArrayListProductFromSQL() throws SQLException;
    public Product getProductByID(int id) throws SQLException;
    public void addProductToOrder(int orderId, int productId, int quantity, double price) throws SQLException;
    public void updateOrder(int orderId, int productId, int quantity, double price) throws SQLException;
    public int getProductIdByNameAndSize(String name, String size) throws SQLException;
    public int getProductIdByName(String name) throws SQLException;
}
