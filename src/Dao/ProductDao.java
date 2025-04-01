package Dao;

import Entity.Product;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class ProductDao {
    private Connection conn;

    public ProductDao() {
        try { 
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream("src/resource/database.properties")) {
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("Không thể đọc file database.properties");
                e.printStackTrace();
                return;
            }
            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String driver = properties.getProperty("Driver");
            Class.forName(driver);
            this.conn = DriverManager.getConnection(url , username, password);
            System.out.println("Kết nối thành công");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Product> getArrayListProductFromSQL() {
        // code here
        // Set<Product> list = new LinkedHashSet<>();
        List<Product> list = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
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
        }
        return null;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
