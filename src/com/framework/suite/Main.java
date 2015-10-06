package com.framework.suite;

import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.framework.core.Global;
import com.framework.helper.DataBase;
import com.framework.shared.RunType;
import com.framework.shared.Status;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class Main {
	
	static Connection conn;
	static String driver = "com.mysql.jdbc.Driver";
	
	
//	create table if not exists Test_result(test_id  INTEGER(10),  test_name VARCHAR(50), run_id  INTEGER(10) 
//	,no_of_pass INTEGER(10), no_of_fail INTEGER(10), no_not_executed INTEGER(10), test_start_time VARCHAR(50), tes_end_time 
//	VARCHAR(50), attached_file1 LONGTEXT, attached_file2 LONGTEXT, modification_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
//	PRIMARY KEY (test_id), FOREIGN KEY (run_id) REFERENCES scheduler(run_id));
//	create table if not exists scheduler(run_id int, status VARCHAR(50), ip VARCHAR(50),  machine_name VARCHAR(50), PRIMARY KEY (run_id));
// Status start/inprogress/completed/end
//	

	//attachedFile

  public static void main(String args[]) throws UnknownHostException {
	//  insertIntoAutomationDatadb();
	  //getSchedulerId();
	  
	//insertIntoScheduler();
	  
	  headerRequest();
//	  System.out.println("User name " + System.getProperty("user.name"));
//	  java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
//	  System.out.println("Hostname of local machine: " + localMachine.getHostName());
//	  DataBase.insertIntoScheduler("goibibo", Status.start, localMachine.getHostName(), "firefox", RunType.regression);
	 // DataBase.updateStatusForScheduler(2, Status.completed);
	//  DataBase.getSchedulerId();
	//	DataBase.insertIntoTestResult("Firefox", 80, 10, 110);
//		DataBase.insertIntoTestResult("Firefox", 90, 110, 10);
//		DataBase.insertIntoTestResult("Firefox", 50, 150, 10);
	//insertIntoTestResult("hello", Integer.parseInt("80"), Integer.parseInt("90"));
  }
  
  
  public static void headerRequest() {

		  try {
			  
				HttpClient client = HttpClientBuilder.create().build();
				HttpGet request = new HttpGet("http://mkyong.com");
				HttpResponse response = client.execute(request);
			 
				System.out.println("Printing Response Header...\n");
			 
				Header[] headers = response.getAllHeaders();
				for (Header header : headers) {
					System.out.println("Key : " + header.getName() 
			                           + " ,Value : " + header.getValue());
			 
				}
			 
				System.out.println("\nGet Response Header By Key ...\n");
				String server = response.getFirstHeader("Server").getValue();
			 
				if (server == null) {
					System.out.println("Key 'Server' is not found!");
				} else {
					System.out.println("Server - " + server);
				}
			 
				System.out.println("\n Done");
			 
			    } catch (Exception e) {
				e.printStackTrace();
			    }
  }
  
  
  

	public static void connectDB(String serverName) throws SQLException {
		if(serverName.contains("localhost")) {
			connectDB("localhost", "frameworkdb", "root", "");
		} else  {
			connectDB("72.249.76.192:3306", "testdb", "root", "");
		}
	}
	
	public static void connectDB(String server, String dbName, String userName, String password) throws SQLException {
			conn = DriverManager.getConnection(
					  "jdbc:mysql://" +server +"/" + dbName,
					  userName, password);
	}
	
  private static void insertIntoTestResult(String testName, int no_of_pass, int no_of_fail) {
	  try {
			String query = "INSERT INTO Test_result(test_name, no_of_pass, no_of_fail, modification_time)" +
					" VALUES ('" + testName + "'," + no_of_pass +", " + no_of_fail +", NOW())";
			System.out.println("Inserting the query " + query);
			Class.forName(driver);
			connectDB("localhost");
			PreparedStatement  preparedStatement = conn.prepareStatement(query);
			preparedStatement.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
		}
		System.out.println("Inserted new row");
  }

  private static void insertIntoScheduler() {
	  try {
			String query = "INSERT INTO scheduler(status) VALUES ('start' )";
			System.out.println("Inserting the query " + query);
			Class.forName(driver);
			connectDB("localhost", "testdb", "root", "");
			PreparedStatement  preparedStatement = conn.prepareStatement(query);
			preparedStatement.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
		}
		System.out.println("Inserted new row");
}
	//223.227.107.118
	public static void insertIntoAutomationDatadb() {
			try {
				String query = "INSERT INTO mytables(status, id, age) VALUES (1,2,3)";
					
				System.out.println("Inserting the query " + query);
				Class.forName(driver);
				conn = DriverManager.getConnection(
						  "jdbc:mysql://72.249.76.192:3306/mypassbr_framework",
						  "mypassbr_suresh", ".mZcp0Gr(Cr{");
				PreparedStatement  preparedStatement = conn.prepareStatement(query);
				preparedStatement.executeUpdate();
				conn.close();
			} catch (Exception e) {
				System.out.println("Error " + e.getMessage());
			}
			System.out.println("Inserted new row");
	}

	
    
} 