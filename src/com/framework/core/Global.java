package com.framework.core;

import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;


public class Global {
    
    static final Logger logger = Logger.getLogger(Global.class);
    
    // SYSTEM PROPERTIES
    public static final String JAVA_HOME = System.getProperty("java.home");
    public static final String JAVA_VENDOR = System.getProperty("java.vendor");
    public static final String JAVA_VERSION = System.getProperty("java.version");
    public static final String OS_ARCH = System.getProperty("os.arch");
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String OS_VERSION = System.getProperty("os.version");
    public static final String USER_DIR = System.getProperty("user.dir");
    public static final String USER_HOME = System.getProperty("user.home");
    public static final String USER_NAME = System.getProperty("user.name");
    public static final boolean MULTIPLE_BROWSER = false;
    
    // Scheduler for xml 
	public static final String SCHEDULER_FILE = "/Files/schedueler.xml";
	public static final int SCHEDULER_EXPIRE_TIME_DELAY = 10;
	
	// Browser and path
	 private static String browserName = "firefox";
	 public static WebDriver driver;  
	 private static String reportsDir = USER_DIR;
	 public static final String FROM_USER = "AutomationDemoLive@gmail.com";
//	 public static final String FROM_USER_CC = "suresh.babu@pass.ch";
//	 public static final String FROM_USER_BCC = "shiva.shankar@pass.ch";
	 
	 public static final String PASSWORD = "auto1234";
//	 public static final String TO_USER = "parimala.hariprasad@pass.ch";
	 public static final String TO_USER = "AutomationDemoLive@gmail.com";
	 public static String HOST = "smtp.gmail.com";
     public static String PORT = "587";
     public static final String REPORT_NAME ="Report.html";
     public static String MANAGEMENT_REPORT ="";
     public static String AUTOMATION_REPORT = "";
     public static String TEST_PATH;
     public static String SUBJECT = "Test Results(Automation Demo)";
     public static String START_TIME;
     public static String END_TIME;
     
	 
     // TestnG Result 
     public static String SUITE_NAME;
     public static String TEST_NAME;
     public static int RUN_ID;
     public static String PROJECT_NAME;
     public static String MACHINE_NAME;
     public static String BROWSER_NAME;
     
     
     // Test Details 
     public static String XPATH;
     public static String ACTUAL_VALUE;
     public static String EXPECTED_VALUE;
     
     public static Set<String> TEST_SET = new HashSet<String>();
     
	  public static synchronized void setReportsDir(String path) {
	        logger.debug("Setting reports dir to : " + path);
	        reportsDir = path;
	   }
	   
	   public static synchronized String getReportsDir() {
	        return reportsDir;
	    }
	    
	    public static synchronized String getBrowserName() {
	        return browserName;
	    }
	    
	  public static synchronized void setBrowserName(String browser) throws Exception {
	        browser = browser.trim();
	        logger.debug("Received browser [" + browser + "]");
	        if(browser.toLowerCase().startsWith("ie") || browser.toLowerCase().equals("internetexplorer")) {
	            logger.debug("Setting, browser = [ie]");
	            browserName = "ie";
	        } else if(browser.toLowerCase().equals("ff") || browser.toLowerCase().equals("firefox")){
	            logger.debug("Setting, browser = [firefox]");
	            browserName = "firefox";
	        } else if(browser.toLowerCase().equals("chrome")){
	            logger.debug("Setting, browser = [chrome]");
	            browserName = "chrome";
	        } else if(browser.toLowerCase().equals("safari")){
	            logger.debug("Setting, browser = [safari]");
	            browserName = "safari";
	        } else {
	            logger.debug("Received an invalid browser name");
	            //TODO: Handle this 
	        }
	        
	    }
}