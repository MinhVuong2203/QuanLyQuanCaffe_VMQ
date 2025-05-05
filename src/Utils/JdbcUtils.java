package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private Connection connection;
    private Properties properties;

    public JdbcUtils() throws IOException, ClassNotFoundException, SQLException{
        this.properties = new Properties();
        properties.load(new FileInputStream("resource/database.properties"));
        properties.load(new FileInputStream("resource/message.properties"));
        connect();
    }

    public Connection connect() throws ClassNotFoundException, SQLException {
		if (this.connection != null && !this.connection.isClosed()) {
			return this.connection;
		}

		String url = properties.getProperty("url");
		String username = properties.getProperty("username");
		String password = properties.getProperty("password");
		String driver = properties.getProperty("driver");
		Class.forName(driver);
		this.connection = DriverManager.getConnection(url, username, password);
		return this.connection;
	}

    public void connectDatabase() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
