/**
 * @author Tejal Shah
 *Singleton class that loads DBConfig file and connects to the auto database.
 *Any class that calls the getInstance method receives a handle to a DB connection.
 */
package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnect {
	private static DBConnect dbConnection = null;
	private static Connection conn = null;
	   
	   //No-arg constructor calls private method to create DB connection
	   private DBConnect() {
		   this.openDBConnection();
	   }
	   
	   //Method exposed to get a DB connection 
	   public static Connection getInstance() {
	      if(dbConnection == null || conn == null) {
	         dbConnection = new DBConnect();
	      }
	      return conn;
	   }
	   
	   //Method to create a DB Connection by reading properties file
	   private void openDBConnection(){
		   try {
			   Properties p = new Properties();
			   File file = new File("DBConfig.prop");
			   FileInputStream in = new FileInputStream(file);
			   p.load(in);
			   in.close();
			   String url = p.getProperty("DBURL");
			   String userid = p.getProperty("UID");
			   String pwd = p.getProperty("PWD");
			conn = DriverManager.getConnection(url, userid, pwd);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				System.out.println("Incorrect File Name -- File not found");
			} catch (IOException e) {
				e.printStackTrace();
			}
	   }
	   
	   //Method to close a DB Connection
	   public static void closeDBConnection(){
		   try {
			conn.close();
			conn = null;
			dbConnection = null;
		   } catch (SQLException e) {
			e.printStackTrace();
		   }
	   }
}
