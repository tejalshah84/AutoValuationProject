/**
 * @author Tejal Shah
 * Class containing implementation of DBCreateAuto, DBUpdateAuto and DBDeleteAuto interfaces
 * This class allows execution of all the DB CRUD operations. It loads the queries from a properties file.
 */
package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import model.Automobile;

public class DBOperations implements DBCreateAuto, DBUpdateAuto, DBDeleteAuto {

	private Properties queries = new Properties();

	public DBOperations(){
		loadCRUDQueries();
	}

	//Load DML queries from properties file
	private void loadCRUDQueries(){
		try {
			File file = new File("DBQueries.prop");
			FileInputStream in = new FileInputStream(file);
			queries.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("Incorrect File Name -- File not found");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	//Add Automobile object to DB
	public boolean addAutoToDB(Automobile a){
		boolean done = false;
		Connection conn = DBConnect.getInstance();

		try {
			conn.setAutoCommit(false);
			//Add to automobile table
			done = addToAutomobile(conn, a);
			if (done){
				done = false;
				//Add to optionset table
				done = addToOptionsets(conn, a);
				if (done){
					conn.commit();
				}
				else{
					conn.rollback();
				}
			}
			else{
				int autoRow = automobileRowCheck(conn, a.getAutomobileName());
				done = (autoRow>0?true:false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally{
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DBConnect.closeDBConnection();
		}
		return done;
	}

	//Add Automobile to Automobile table
	private boolean addToAutomobile(Connection conn, Automobile a){
		String name = a.getAutomobileName();
		String make = a.getAutomobileMake();
		String model = a.getAutomobileModel();
		float price = a.getBasePrice();
		try {
			int autoRow = automobileRowCheck(conn, a.getAutomobileName());
			if(autoRow<=0){
				PreparedStatement bAuto = conn.prepareStatement(queries.getProperty("InsertAutomobile"));
				bAuto.setString(1, name);
				bAuto.setString(2, make);
				bAuto.setString(3, model);
				bAuto.setFloat(4, price);
				int work = bAuto.executeUpdate();
				bAuto.close();
				return (work>0?true:false);
			}
			else {
				System.out.println("Automobile already exists--Duplicate");
				return false;
			}	
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	//Get RowID of automobile by name, check if it exists in automobile table
	private int automobileRowCheck(Connection conn, String autoName){
		PreparedStatement findAuto = null;
		try {
			findAuto = conn.prepareStatement(queries.getProperty("AutoRowCheck"));
			findAuto.setString(1, autoName);
			ResultSet rs = findAuto.executeQuery();
			if(rs.next()){
				return rs.getInt("auto_id");
			}
			else{
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (findAuto!=null)
					findAuto.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	//Add optionsets, options and their mapping in respective tables
	private boolean addToOptionsets(Connection conn, Automobile a) {
		int num = a.getNumberOfOptions();
		int autoRow = automobileRowCheck(conn, a.getAutomobileName());
		boolean done = false, error = false;

		for (int i=0; i<num; i++){
			String osName = a.getOptionSet(i);
			int osRow = optionsetRowCheck(conn, osName);
			if(osRow<=0){
				done = insertOptionset(conn, osName);
			}
			else{
				done=true;
			}

			if (done){
				osRow = optionsetRowCheck(conn, osName);
				Properties p = a.getOptionValues(osName);
				try {
					for(String k: p.stringPropertyNames()){
						done = false;
						//Add option in option table
						int oRow = insertOption(conn, k, Float.parseFloat(p.getProperty(k)));
						//Add mapping of automobile to optionsets and options in table
						done = insertAutoOptionsMap(conn, autoRow, osRow, oRow);
						if (!done){
							System.out.println("Error occured while inserting into auto_map table");
							error = true;
						}
					}
				} catch (NullPointerException e){
					e.printStackTrace();
				}
			}
			else{
				System.out.println("Error occured while inserting optionset");
			}
		}
		return (done && !error);
	}

	//Get RowID of optionset by name from optionset table if it exists
	private int optionsetRowCheck(Connection conn, String osName){
		PreparedStatement findOs = null;
		try {
			findOs = conn.prepareStatement(queries.getProperty("OsRowCheck"));
			findOs.setString(1, osName);
			ResultSet rs = findOs.executeQuery();
			if(rs.next()){
				return rs.getInt("optionset_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (findOs!=null)
					findOs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	//Get rowID of option by option name from option table if it exists
	private int optionRowCheck(Connection conn, String oName){
		PreparedStatement findO = null;
		try {
			findO = conn.prepareStatement(queries.getProperty("ORowCheck"));
			findO.setString(1, oName);
			ResultSet rs = findO.executeQuery();
			if(rs.next()){
				return rs.getInt("option_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (findO!=null)
					findO.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	//Insert optionset  in optionset table
	private boolean insertOptionset(Connection conn, String osName){
		PreparedStatement addOs = null;
		int work = 0;
		try {
			addOs = conn.prepareStatement(queries.getProperty("InsertOptionSet"));
			addOs.setString(1, osName);
			work = addOs.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (addOs!=null)
					addOs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return (work>0?true:false);
	}

	//Insert option in option table
	private int insertOption(Connection conn, String oName, float price){
		PreparedStatement addO = null;
		PreparedStatement findO = null;
		int work = 0;
		try {
			addO = conn.prepareStatement(queries.getProperty("InsertOption"));
			addO.setString(1, oName);
			addO.setFloat(2, price);
			findO = conn.prepareStatement(queries.getProperty("FindOption"));
			findO.setString(1, oName);
			findO.setFloat(2, price);
			work = addO.executeUpdate();
			if(work!=0){
				ResultSet rs = findO.executeQuery();
				if (rs.next()){
					work = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (addO!=null) addO.close();
				if (findO!=null) findO.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return work;
	}

	//Insert mapping of automobile, optionset and option in bridge table
	private boolean insertAutoOptionsMap(Connection conn, int a, int os, int o){
		PreparedStatement addAutoMap = null;
		int work = 0;
		if(a==0||os==0||o==0){
			return false;
		}
		try {
			addAutoMap = conn.prepareStatement(queries.getProperty("InsertAutoMap"));
			addAutoMap.setInt(1, a);
			addAutoMap.setInt(2, os);
			addAutoMap.setInt(3, o);
			work = addAutoMap.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (addAutoMap!=null) addAutoMap.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return (work>0?true:false);
	}

	//Update automobile name in automobile table if name not already in use
	public boolean updateAutomobileName(String modelName, String newName){
		Connection conn = DBConnect.getInstance();
		int work = 0;

		try {
			conn.setAutoCommit(false);
			int autoRow = automobileRowCheck(conn, modelName);
			int newAutoRow = automobileRowCheck(conn, newName);
			if(autoRow>0 && newAutoRow<=0){
				PreparedStatement stmt = conn.prepareStatement(queries.getProperty("UpdateAutoName"));
				stmt.setString(1, newName);
				stmt.setString(2, modelName);
				work = stmt.executeUpdate();
				stmt.close();
				conn.commit();
			}
			else if (autoRow<=0 && newAutoRow>0){
				//System.out.println("AutoName already existing");
				work=1;
			}
			else if (autoRow>0 && newAutoRow>0){
				//System.out.println("AutoName already in use");
				work=0;
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeDBConnection();
		}
		return (work>0?true:false);
	}

	//Update optionset name in optionset table if name not already in use
	public boolean updateOptionSetName(String modelName, String osName, String newName){
		Connection conn = DBConnect.getInstance();
		int work = 0;

		try {
			conn.setAutoCommit(false);
			int autoRow = automobileRowCheck(conn, modelName);
			int osRowOld = optionsetRowCheck(conn, osName);
			int osRowNew = optionsetRowCheck(conn, newName);

			if(autoRow>0 && osRowOld>0 && osRowNew<=0){
				PreparedStatement stmt = conn.prepareStatement(queries.getProperty("UpdateOSName"));
				stmt.setString(1, newName);
				stmt.setString(2, osName);
				stmt.setInt(3, autoRow);
				work = stmt.executeUpdate();
				stmt.close();
				conn.commit();
			}
			else if(autoRow>0 && osRowNew>0){
				//System.out.println("OptionSet Name already used");
				work = 0;
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeDBConnection();
		}
		return (work>0?true:false);
	}

	//Update option price in option table
	public boolean updateOptionPrice(String modelName, String osName, String oName, float price){
		Connection conn = DBConnect.getInstance();
		int work = 0;

		try {
			conn.setAutoCommit(false);
			int autoRow = automobileRowCheck(conn, modelName);
			int oRow = optionRowCheck(conn, oName);
			if(autoRow>0 && oRow>0){
				PreparedStatement stmt = conn.prepareStatement(queries.getProperty("UpdateOptionPrice"));
				stmt.setFloat(1, price);
				stmt.setString(2, oName);
				stmt.setInt(3, autoRow);
				stmt.setString(4, osName);
				work = stmt.executeUpdate();
				stmt.close();
				conn.commit();
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeDBConnection();
		}
		return (work>0?true:false);
	}

	//Remove automobile object from DB (parent method)
	public boolean removeAutoFromDB(String modelName){
		Connection conn = DBConnect.getInstance();
		int work = 0;
		boolean done = false;

		try {
			conn.setAutoCommit(false);
			int autoRow = automobileRowCheck(conn, modelName);
			boolean error = false;

			if(autoRow>0){
				PreparedStatement stmt = conn.prepareStatement(queries.getProperty("GetAutoMap"));
				stmt.setInt(1, autoRow);
				ResultSet rs = stmt.executeQuery();
				//Delete from bridge table
				PreparedStatement dltStmt = conn.prepareStatement(queries.getProperty("DeleteAutoMap"));
				dltStmt.setInt(1, autoRow);
				work = dltStmt.executeUpdate();
				//Delete from automobile table
				PreparedStatement dltStmt1 = conn.prepareStatement(queries.getProperty("DeleteAuto"));
				dltStmt1.setInt(1, autoRow);
				work += dltStmt1.executeUpdate();
				dltStmt.close();
				dltStmt1.close();
				while(rs.next()){
					//Delete from option table
					boolean opDlt = deleteOption(conn, rs.getInt("option_id"));
					//Delete from optionset table
					boolean opsetDlt = deleteOptionSet(conn, rs.getInt("optionset_id"), autoRow);
					if (!opDlt || !opsetDlt) error = true;
				}
				stmt.close();
				if(work>=2 && !error){
					conn.commit();
					done = true;
				}
				else {
					conn.rollback();
					done = false;
				}
			}
			else{
				System.out.println("Automobile not available for deletion");
				done = true;
			}
			conn.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeDBConnection();
		}
		return done;
	}

	//Check if optionset is in use by other automobiles before deleting
	private boolean checkUsageofOptionSet(Connection conn, int id, int autoRow){

		PreparedStatement checkAffected;
		try {
			checkAffected = conn.prepareStatement(queries.getProperty("CheckOSUse"));
			checkAffected.setInt(1, id);
			checkAffected.setInt(2, autoRow);
			ResultSet rs = checkAffected.executeQuery();
			if (rs.next()){
				if (rs.getInt("auto_id")!=autoRow && rs.getInt("auto_id")>0){
					checkAffected.close();
					return true;
				}
			}
			else{
				checkAffected.close();
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	//Delete option from option table
	private boolean deleteOption(Connection conn, int oid){
		int work = 0;
		try {
			PreparedStatement dltStmt1 = conn.prepareStatement(queries.getProperty("DeleteOption"));
			dltStmt1.setInt(1, oid);
			work = dltStmt1.executeUpdate();
			dltStmt1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (work>0?true:false);
	}

	//Delete optionset from optionset table
	private boolean deleteOptionSet(Connection conn, int osid, int autorow){
		int work = 0;
		int rowid = 0;
		if(checkUsageofOptionSet(conn, osid, autorow)) return true;

		try {
			PreparedStatement s1 = conn.prepareStatement(queries.getProperty("FindOptionSet"));
			s1.setInt(1, osid);
			ResultSet rs = s1.executeQuery();
			if(rs.next()){
				PreparedStatement dltStmt1 = conn.prepareStatement(queries.getProperty("DeleteOptionSet"));
				dltStmt1.setInt(1, osid);
				work = dltStmt1.executeUpdate();
				dltStmt1.close();
			}
			else{
				work=1;
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (work>0?true:false);
	}
}
