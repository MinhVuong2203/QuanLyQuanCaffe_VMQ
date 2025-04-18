package Repository.Product;

import Model.Product;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductRespository implements IProductRespository{
    private Connection connection;
    private JdbcUtils jdbcUtils;
    public ProductRespository() throws IOException, ClassNotFoundException, SQLException {
        jdbcUtils = new JdbcUtils();
    }
    
    @Override
    public List<Product> getArrayListProductFromSQL() throws SQLException {
        
        List<Product> list = new ArrayList<>();
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM Product";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt(1));
                product.setName(rs.getString(2));
                product.setPrice(rs.getInt(3));
                product.setSize(rs.getString(4));
                product.setImage(rs.getString(5));
                list.add(product);
            }
            rs.close();
            stmt.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			connection.close();
		}
        return null;
    }

    @Override
    public Product getProductByID(int id) throws SQLException {
        Product product = null;
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM Product WHERE ProductID = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                product = new Product();
                product.setProductID(rs.getInt(1));
                product.setName(rs.getString(2));
                product.setPrice(rs.getInt(3));
                product.setSize(rs.getString(4));
                product.setImage(rs.getString(5));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();}
        return product;

    }
    
    @Override
    public void addProductToOrder(int orderId, int productId, int quantity, double price) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String sql = "INSERT INTO OrderDetail (orderID, productID, quantity, price) VALUES (?, ?, ?, ?)";
            var stmt = connection.prepareStatement(sql);
            stmt.setInt(1, orderId);
            stmt.setInt(2, productId); 
            stmt.setInt(3, quantity);
            stmt.setDouble(4, price);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    @Override
    public void updateOrder(int orderId, int productId, int quantity, double price) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String sql = "UPDATE OrderDetail SET quantity = ?, price = ? WHERE orderID = ? AND productID = ?";
            var stmt = connection.prepareStatement(sql);
            stmt.setInt(1, quantity);
            stmt.setDouble(2, price);
            stmt.setInt(3, orderId);
            stmt.setInt(4, productId);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
    
    @Override
    public int getProductIdByNameAndSize(String name, String size) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String sql = "SELECT ProductID FROM Product WHERE name = ? AND size = ?";
            var stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, size);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ProductID");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return -1; // Return -1 if no product found
    }
    
    @Override
    public int getProductIdByName(String name) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String sql = "SELECT ProductID FROM Product WHERE name = ?";  // Default to M size for products
            var stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ProductID");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return -1; // Return -1 if no product found
    }
}