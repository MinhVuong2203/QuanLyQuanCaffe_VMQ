package Repository;

import Model.Table;
import View.Login;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TableRepository {
    private Connection conn;
    private Login staff_Sign;

    public TableRepository() {
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

    public List<Table> getTableFromSQL(){
        List<Table> listTables = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM TableCaffe";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                listTables.add(new Table(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            rs.close();
            stmt.close();
            return listTables;

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    } 


    // Hàm đóng database mỗi khi thực hiện xong tất cả các tác vụ - Quan trọng phải có và phải gọi sau khi dùng xong
    public void closeConnection(){
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

