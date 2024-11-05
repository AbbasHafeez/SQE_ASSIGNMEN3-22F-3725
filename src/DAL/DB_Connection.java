package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_Connection {
	 public Connection connect() { 
	        String url = "jdbc:mysql://localhost:3306/texteditor";
	        String username = "root";
	        String password = "";

	        try {
	            return DriverManager.getConnection(url, username, password);
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
}