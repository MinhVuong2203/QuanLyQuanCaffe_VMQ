package Dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class CustomerDao {
    private Connection conn;
    public CustomerDao(){
        try { 
            Properties properties = new Properties();

            try (FileInputStream fis = new FileInputStream("resource/database.properties")) {
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

    public boolean checkEqualsPhone(String phone){
        try{
            Statement stmt = conn.createStatement();
            String sql = "SELECT [phone]\r\n" + //
                        "FROM [Customer]\r\n" + //
                        "WHERE [phone] = '" + phone + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return true; // Có tồn tại
        } catch (Exception e){
            e.printStackTrace();
        }
        return false; // Không tồn tại
    }

    public void closeConnection(){
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
