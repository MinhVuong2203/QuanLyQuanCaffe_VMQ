package Dao;

import Entity.Product;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    private Connection conn;

    public ProductDao() {
         try { 
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=CaffeVMQ;encrypt=false";
            // String url = "jdbc:sqlserver://192.168.155.223:1433;databaseName=CaffeVMQ;user=sa;password=123456789;encrypt=false;trustServerCertificate=true;";

            String username = "sa";
            String password = "123456789";
            this.conn = DriverManager.getConnection(url, username, password);
            System.out.println("Kết nối thành công");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Product> getArrayListProductFromSQL() {
        // code here
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

            // String sql2 = "SELECT * FROM ProductIngredient";
            // ResultSet rs2 = stmt.executeQuery(sql2);
            // while (rs2.next()) {
            //     for (Product product : list) {
            //         if (product.getProductID() == rs2.getInt(1)) {
                        
            //         }
            //     }
            // }

            rs.close();
            stmt.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
