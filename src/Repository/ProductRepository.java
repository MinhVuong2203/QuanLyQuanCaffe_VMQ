package Repository;

import Model.Product;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ProductRepository {
    private Connection connection;
    private JdbcUtils jdbcUtils;
    public ProductRepository() throws IOException, ClassNotFoundException, SQLException {
        jdbcUtils = new JdbcUtils();
    }

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
}