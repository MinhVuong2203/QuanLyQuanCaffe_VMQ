package Utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private static HikariDataSource dataSource;
    private Properties properties;

    public JdbcUtils() throws IOException, ClassNotFoundException, SQLException {
        this.properties = new Properties();
        properties.load(new FileInputStream("resource/database.properties"));
        properties.load(new FileInputStream("resource/message.properties"));

        if (dataSource == null) { // Chỉ khởi tạo 1 lần duy nhất
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("url"));
            config.setUsername(properties.getProperty("username"));
            config.setPassword(properties.getProperty("password"));
            config.setDriverClassName(properties.getProperty("driver"));
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
            System.out.println("Khởi tạo connection pool thành công");
        }
    }

    public Connection connect() throws SQLException {
        return dataSource.getConnection();
    }

    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("Đã đóng connection pool");
        }
    }
}
