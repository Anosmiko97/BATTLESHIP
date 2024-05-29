package controlers.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionDB {

    public static Connection createConnection() {
        Connection conn = null;
        Properties properties = getProperties();
		String url = properties.getProperty("db.url");
		String user = properties.getProperty("db.usuario");
		String password = properties.getProperty("db.contrasena");
		
		try {
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Se conectó exitosamente a la base de datos.");
		}catch(SQLException ex) {
			System.out.println("Error al conectarse a la base de datos: " + ex.getMessage());
		}
		
		return conn;
	}

    public static Properties getProperties() {
		Properties propierties = new Properties();
		
		try(FileInputStream filePropierties = new FileInputStream("src/controlers/db/configuration.properties")){
			propierties.load(filePropierties);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return propierties;
	}
}
