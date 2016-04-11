/**
 * @author Tejal Shah
 * This class is used initially in the Driver to setup the database if it doesn't already
 * exist. The class provides methods to load a properties file for getting all the DDL
 * statements to setup the DB. Then it provides methods to setuptheDB by creating a DB connection
 * and then executing the DDL queries from the properties object.
 */
package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBSetup {
	
	//Private variable that stores all the DDL queries
	private Properties queries = new Properties();
	
	//Method to load DDL queries from properties file
	public void loadDBSetupFile(){
		try {
			File file = new File("DBSetup.prop");
			FileInputStream in = new FileInputStream(file);
			queries.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("Incorrect File Name -- File not found");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Method to connect to DB and create schema and call setupTable method
	public void setupDB(){
		Connection conn;
		try {
			   Properties p = new Properties();
			   File file = new File("DBConfig.prop");
			   FileInputStream in;
			   in = new FileInputStream(file);
			   p.load(in);
			   in.close();
			   String url = p.getProperty("DBURLSTART");
			   String userid = p.getProperty("UID");
			   String pwd = p.getProperty("PWD");
			conn = DriverManager.getConnection(url,userid,pwd);
			setupTable(conn, "createDB");
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		conn = DBConnect.getInstance();
		setupTable(conn, "createAutomobile");
		setupTable(conn, "createOptionSet");
		setupTable(conn, "createOptionValue");
		setupTable(conn, "createAutoOptions");
		System.out.println("Table Setup successful");
		DBConnect.closeDBConnection();
	}
	
	//Method to execute Table creation DDL queries
	private void setupTable(Connection conn, String propName){
		Statement stmt=null;
		try {
			stmt = conn.createStatement();
			String query = queries.getProperty(propName);
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error occured while setting up DB");
			System.exit(1);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
