package com.framework.suite;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import com.framework.core.Global;
import com.framework.helper.DataBase;

public class CustomLogging implements ITestListener {
  
  //Called when the test-method execution starts  
  @Override
  public void onTestStart(ITestResult result) {
	  ITestNGMethod method = result.getMethod();
	  String methodDescription = method.getDescription();
	  String name = method.getMethodName();
      System.out.println("Test method " + name + " "+
	  "Test Descritpion " +  methodDescription + " started: "+ result.getName() 
	    + " and time is: "+getCurrentTime());    
  }

  //Called when the test-method execution is a success
  @Override
  public void onTestSuccess(ITestResult result) {
	ITestNGMethod method = result.getMethod();
    String description = method.getDescription();
	String test_case = method.getMethodName();
    System.out.println("Test method success: "+ result.getName()+ " and time is: "+getCurrentTime());

    
    try {
		DataBase.insertIntoTestDetails(Global.RUN_ID + "", test_case, description, Global.XPATH, Global.ACTUAL_VALUE, Global.EXPECTED_VALUE, 
				"Pass", getDuration(result) +"");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    Global.XPATH = "";
    Global.ACTUAL_VALUE = "";
    Global.EXPECTED_VALUE = "";
  }
  
  //Called when the test-method execution fails
  @Override
  public void onTestFailure(ITestResult result) {
    System.out.println("Test method failed: "+ result.getName()+ "  and time is: "+getCurrentTime());
  
    ITestNGMethod method = result.getMethod();
    String description = method.getDescription();
	String test_case = method.getMethodName();
    System.out.println("Test method success: "+ result.getName()+ " and time is: "+getCurrentTime());

    
    try {
		DataBase.insertIntoTestDetails(Global.RUN_ID + "", test_case, description, Global.XPATH, Global.ACTUAL_VALUE, Global.EXPECTED_VALUE, 
				"Fail", getDuration(result) +"");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    Global.XPATH = "";
    Global.ACTUAL_VALUE = "";
    Global.EXPECTED_VALUE = "";
  }

  //Called when the test-method is skipped
  @Override
  public void onTestSkipped(ITestResult result) {
	  
	    System.out.println("Test method failed: "+ result.getName()+ "  and time is: "+getCurrentTime());
	    
	    ITestNGMethod method = result.getMethod();
	    String description = method.getDescription();
		String test_case = method.getMethodName();
	    System.out.println("Test method success: "+ result.getName()+ " and time is: "+getCurrentTime());

	    
	    try {
			DataBase.insertIntoTestDetails(Global.RUN_ID + "", test_case, description, Global.XPATH, Global.ACTUAL_VALUE, Global.EXPECTED_VALUE, 
					"Skipped", getDuration(result) +"");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
    System.out.println("Test method skipped: "+ result.getName()+ "  and time is: "+getCurrentTime());    
  }

  //Called when the test-method fails within success percentage
  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    // Leaving blank
    
  }

  //Called when the test in xml suite starts
  @Override
  public void onStart(ITestContext context) {
    System.out.println("Test in a suite started: "+ context.getName()+ " and time is: "+getCurrentTime());
    Global.XPATH = "";
    Global.ACTUAL_VALUE = "";
    Global.EXPECTED_VALUE = "";
  }
  
  //Called when the test in xml suite finishes
  @Override
  public void onFinish(ITestContext context) {
    System.out.println("Test in a suite finished: "+ context.getName()+ " and time is: "+getCurrentTime());
    
  }
  
  public String  getDuration(ITestResult result) {
	return (result.getEndMillis() - result.getStartMillis())/ 1000 +"";
	  
  }
  //Returns the current time when the method is called
  public String getCurrentTime(){
    DateFormat dateFormat = 
        new SimpleDateFormat("HH:mm:ss:SSS");
    Date dt = new Date();
    return dateFormat.format(dt);    
  }
}