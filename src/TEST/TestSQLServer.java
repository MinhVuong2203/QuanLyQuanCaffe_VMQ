package TEST;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestSQLServer {
    public static void main(String[] args) {
        try { 
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=CaffeVMQ;encrypt=false";
            // String url = "jdbc:sqlserver://192.168.155.223:1433;databaseName=CaffeVMQ;user=sa;password=123456789;encrypt=false;trustServerCertificate=true;";

            String username = "sa";
            String password = "123456789";
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement();


            String sql = "SELECT MAX(id) FROM UserSystem";
            // Muốn thêm nhân viên, thêm khách hàng đều phải truy xuất id max
            ResultSet rs = stmt.executeQuery(sql);
            int id_Max = 0;
            while (rs.next()) {
                id_Max = rs.getInt(1) + 1;
            }
             

    


            // while (rs.next()) {
            //     // System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + rs.getString(5) + " " + rs.getString(6));
            // }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
