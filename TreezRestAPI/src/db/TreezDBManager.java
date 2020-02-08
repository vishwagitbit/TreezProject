package db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;



public class TreezDBManager {
	
	private static Connection con;
	
	
	//Hard coded jdbc connection and mysql credential values for local testing
	//private static final String connUrl = "jdbc:mysql://localhost:3307/treezdatabase?useSSL=false";
	//private static final String username = "root";
	//private static final String password = "root";
	//private static final String driver = "com.mysql.jdbc.Driver";
		
	public static Connection getDBConnection() throws FileNotFoundException, IOException
	{

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("dbConnection.properties");
		   
		Properties properties = new Properties();
		properties.load(input);
				
	    String driver = properties.getProperty("jdbc.driverClassName");
	    String connectionURL = properties.getProperty("mysql.url");
	    String username = properties.getProperty("mysql.username");
	    String password = properties.getProperty("mysql.password");
	    String port = properties.getProperty("mysql.port");
	    String dataBase = properties.getProperty("mysql.database");
	          
	    String connUrl = connectionURL+port+"/"+dataBase+"?useSSL=false";
	       
	    try
		{
			Class.forName(driver);
			//System.out.print("\nThis is SUCCESS DBConnect");
			
			if(con == null){
			con = DriverManager.getConnection(connUrl, username, password);
			}
			
			if(!con.isValid(5)){
				con = DriverManager.getConnection(connUrl, username, password);
				}
								
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return con;
	
	}	
		
}
