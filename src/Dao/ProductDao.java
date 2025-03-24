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
            String sql = "SELECT P.productID, P.name, P.price, P.size, P.image, IR.IngredientID, IR.name, IR.unit, IR.stockQuantity, PIR.quantity\r\n" + //
                                "FROM [dbo].[Product] AS P\r\n" + //
                                "JOIN [dbo].[ProductIngredient] AS PIR ON P.productID = PIR.productID\r\n" + //
                                "JOIN [dbo].[Ingredient] AS IR ON PIR.ingredientID = IR.IngredientID";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt(1));
                product.setName(rs.getString(2));
                product.setPrice(rs.getInt(3));
                product.setSize(rs.getString(4));
                product.setImage(rs.getString(5));
                product.addIngredient(rs.getInt(6), rs.getString(7), rs.getString(8), rs.getInt(9), rs.getInt(10));
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
