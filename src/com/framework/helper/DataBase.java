package com.framework.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.framework.core.Global;
import com.framework.library.utils.MiscUtils;
import com.framework.shared.RunType;
import com.framework.shared.Scheduler_Info;
import com.framework.shared.Status;

public class DataBase {

	static Connection conn = null;
	static String driver = "com.mysql.jdbc.Driver";
	static String SERVER = "";
	
//	create table if not exists test_result(test_id  INTEGER(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,  test_name VARCHAR(50), run_id  INTEGER(10) 
//	,no_of_pass INTEGER(10), no_of_fail INTEGER(10), no_not_executed INTEGER(10), test_start_time VARCHAR(50), tes_end_time 
//	VARCHAR(50), attached_file1 LONGTEXT, attached_file2 LONGTEXT, modification_time TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
//	 FOREIGN KEY (run_id) REFERENCES scheduler(run_id));
//	create table if not exists scheduler(run_id INTEGER(10) NOT NULL AUTO_INCREMENT PRIMARY KEY, type varchar(50), status VARCHAR(50), project varchar(50), browser varchar(25), ip VARCHAR(50), machine_name VARCHAR(50), scheduler_time TIMESTAMP ON UPDATE CURRENT_TIMESTAMP);
// create table if not exists test_details(run_id INTEGER(10) NOT NULL AUTO_INCREMENT PRIMARY KEY, test_case varchar(200), description VARCHAR(200), xpath varchar(200), actual_value varchar(200), expected_value VARCHAR(50), status VARCHAR(200), duration VARCHAR(200), test_details_time TIMESTAMP ON UPDATE CURRENT_TIMESTAMP);	
	public static void connectDB() throws SQLException {
		if (conn == null) {
			connect();
		}
	}

	private static void connect() throws SQLException {
		if(SERVER.contains("localhost")) {
			connectDB("localhost", "frameworkdb", "root", "");
		} else  {
			connectDB("72.249.76.192:3306", "mypassbr_frameworkdb", "mypassbr_suresh", ".mZcp0Gr(Cr{");
		}
	}
	
	public static void connectDB(String server, String dbName, String userName, String password) throws SQLException {
		conn = DriverManager.getConnection(
			  "jdbc:mysql://" +server +"/" + dbName,
			  userName, password);
	}
	
  public static void insertIntoTestResult(int no_of_pass, int no_of_fail, int no_not_executed) throws InterruptedException, SQLException {
	  String attached_file1= Global.MANAGEMENT_REPORT.replaceAll("\\'", "\\\\'");
	  String attached_file2= Global.AUTOMATION_REPORT.replaceAll("\\'", "\\\\'");
	  for (int i =0 ; i< 5; i++) {
		  try {
				String query = "INSERT INTO test_result(run_id, test_name, no_of_pass, no_of_fail, no_not_executed, attached_file1, attached_file2, project, browser, machine_name,  modification_time)" +
						" VALUES (" + Global.RUN_ID + ", '" + Global.TEST_NAME + "'," + no_of_pass +", " + no_of_fail +"," + no_not_executed + ", '" + attached_file1 + "', '" + attached_file2 +
						"', '" + Global.PROJECT_NAME + "', '" + Global.BROWSER_NAME  + "', '" + Global.MACHINE_NAME +  "', NOW())";
				System.out.println("Inserting the query " + query);
				Class.forName(driver);
				
				connectDB();
				PreparedStatement  preparedStatement = conn.prepareStatement(query);
				preparedStatement.executeUpdate();
				//conn.close();
				break;
				
			} catch (Exception e) {
				System.out.println("Error " + e.getMessage());
				connect();
			}
	  }
		System.out.println("Inserted test result new row");
  }
  
  public static void insertIntoScheduler(String project, Status start, String machine_name, String browserName, RunType type) {
	  try {
			String query = "INSERT INTO scheduler(project, status, machine_name, browser, type,  scheduler_time)" +
				" VALUES ('" + project + "', '" + start + "', '" + machine_name + "', '" + browserName + "', '" + type+ "', NOW())";
			System.out.println("Inserting the query " + query);
			Class.forName(driver);
			connectDB();
			PreparedStatement  preparedStatement = conn.prepareStatement(query);
			preparedStatement.executeUpdate();
		//	conn.close();
			
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
		}
		System.out.println("Inserted scheduler new row");
  }
  
  public static void insertIntoTestDetails(String runid, String test_case, String description, String xpath, String actual_value,
		  String expected_value, String status, String duration) throws SQLException {
	  String query= "";
	  xpath = xpath.replaceAll("'", "");
	  for(int i = 0; i< 15; i++) { 
	  try {
			 query = "INSERT INTO test_details(run_id, test_case, description, xpath, actual_value,  expected_value, status, duration, test_details_time)" +
				" VALUES (" + Integer.valueOf(runid) + ", '" +  MiscUtils.getString(test_case) + "', '" +  MiscUtils.getString(description) + "', '" +  MiscUtils.getString(xpath) + "', '" +  MiscUtils.getString(actual_value) + "', '" +  MiscUtils.getString(expected_value) +
				"', '" + status + "', '"  + duration  + "', NOW())";
			System.out.println("Inserting the query " + query);
			Class.forName(driver);
			connectDB();
			PreparedStatement  preparedStatement = conn.prepareStatement(query);
			preparedStatement.executeUpdate();
			//conn.close();
			break;
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			connect();
		}
		System.out.println("Inserted test Details  new row with query " + query);
	
  }
  }

  public static Scheduler_Info getSchedulerId(String machineName) {
	  Scheduler_Info scheduler = null;
		try  {
		    Class.forName(driver);
		    connectDB();
		    PreparedStatement  preparedStatement = conn.prepareStatement(
		      "SELECT run_id, project, browser, machine_name, type, frameworkType From scheduler where status='start' and machine_name='" + machineName + "' limit 1");
		    ResultSet rs = preparedStatement.executeQuery();
		    if (rs.next()) {
		    	 scheduler = new Scheduler_Info();
		    	 scheduler.setRun_id(rs.getInt(1));
		    	 scheduler.setProject(rs.getString(2));
		    	 scheduler.setBrowser(rs.getString(3));
		    	 scheduler.setMachine(rs.getString(4));
		    	 scheduler.setType(rs.getString(5));
		    	 scheduler.setFrameworkType(rs.getString(6));
		    }
		    //conn.close();
	    } catch (Exception e) {	
	    	System.err.println("Error " + e);
	    }
	
		return scheduler;
	}
  
  public static void updateStatusForScheduler(int schedulerId, Status status) throws SQLException {
	  for (int i =0; i< 5; i++) {
			try {
				String query = "update scheduler set status='" + status + "' where run_id=" + schedulerId;
				System.out.println("Updated  " + query);
				Class.forName(driver);
				connectDB();
				PreparedStatement  preparedStatement = conn.prepareStatement(query);
				preparedStatement.executeUpdate();
				//conn.close();
				break;
			} catch (Exception e) {
				System.err.println(e.getMessage());
				connect();
			}
	  }
  }
  
}
