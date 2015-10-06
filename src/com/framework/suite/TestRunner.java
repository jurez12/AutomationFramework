package com.framework.suite;

import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.testng.TestNG;
import org.testng.xml.*;
import com.framework.core.Global;
import com.framework.helper.DataBase;
import com.framework.library.utils.MiscUtils;
import com.framework.shared.RunType;
import com.framework.shared.Scheduler_Info;
import com.framework.shared.Status;
 
public class TestRunner {
 
	static TestNG testng;

	public static void main(String[] args) throws Exception {
		String path = Global.getReportsDir() + File.separator + "screenshots" + File.separator;
		String fileName = System.currentTimeMillis() + ".png";
		String screenshotsPath = "./screenshots/" + fileName;
		//new MiscUtils().captureScreen(path, fileName);
		  java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
		  String machineName = localMachine.getHostName();
		  System.out.println("Machine Name: " + machineName + " Operation System: " + Global.OS_NAME);
	      for(;;) {
				try {
					//Thread.sleep(5000);
					 System.out.println("Searching for test run");
					 Scheduler_Info scheduler = DataBase.getSchedulerId(machineName);
					 if(scheduler!= null && scheduler.getMachine().contains(machineName)) {
						 Global.RUN_ID = scheduler.getRun_id();
						 Global.PROJECT_NAME = scheduler.getProject();
						 Global.BROWSER_NAME = scheduler.getBrowser();
						 Global.MACHINE_NAME = scheduler.getMachine();
						 DataBase.updateStatusForScheduler(Global.RUN_ID, Status.inprogress);
						 testng = new TestNG();
						 testng.setPreserveOrder(true);
						 testng.setVerbose(1);
						 List<Class> listnerClasses = new ArrayList<Class>();
						 listnerClasses.add(com.framework.suite.CustomLogging.class);
						 listnerClasses.add(com.framework.suite.TestHTMLReporter.class);
						 testng.setListenerClasses(listnerClasses);
			 			 String commonPackage = "com.framework.tests.";
			 			 String projectName = scheduler.getProject();
			 			 String frameworkType = scheduler.getFrameworkType();
			 			 if (frameworkType.contains("Mobile")) {
			 				 String projectPackage = null ;
			 				 if(projectName.contains("Chess")) {
			 					projectPackage = "com.framework.tests.mobile.android.smoke";
			 				 } else  if(projectName.contains("Hybrid")) {
			 					projectPackage = "com.framework.tests.mobile.ios.smoke";
			 				 } else  if(projectName.contains("University")) {
				 					projectPackage = "com.framework.tests.mobile.product.smoke";
				 			 }
			 				
			 				  String testName = scheduler.getType();
							  String browser = scheduler.getBrowser();
							  Global.TEST_SET.add("Mobile" + "@" + "Hybrid");
							  testng.setXmlSuites(getSuite(projectName, testName,  projectPackage, browser));
			 			 } else {
				 			 if (projectName.length() > 3) {
				 				 
				 				 String projectPackage = commonPackage + projectName +".smoke"; 
								 String testName = scheduler.getType();
								 String browser = scheduler.getBrowser();
								 System.out.println("The schduler type " +  scheduler.getType() + "The run type regression " +RunType.regression );
								 if (scheduler.getType().equals(RunType.regression.toString())) {
									  testng.setXmlSuites(getSuite(projectName, testName,  projectPackage, browser));
								 } 
				 			 } 
			 			 }
						 testng.run();
					} 
				} catch (Exception e1) {	
					e1.printStackTrace();
				}
		  }
	}
	
	
	   public static List<XmlSuite> getSuite(String projectName, String testName, String projectPackage, String browser) {
	        List<XmlSuite> suites = new ArrayList<XmlSuite>();
	        XmlSuite eachSuite = new XmlSuite();
	        eachSuite.setName(projectName);
	        eachSuite.setTests(getTest(eachSuite, testName, projectPackage, browser));
	        System.out.println(eachSuite.toXml());
	        suites.add(eachSuite);
	        return suites;
	    }

	    public static List<XmlTest> getTest(XmlSuite suite, String testName, String projectPackage, String browser) {
	        List<XmlTest> tests = new ArrayList<XmlTest>();
	        XmlTest eachTest = new XmlTest();
	        tests.add(eachTest);
	        eachTest.setName(testName);
	        eachTest.addParameter("browser", browser);
	        eachTest.setPackages(getPackages(projectPackage));
	        eachTest.setSuite(suite);
	        return tests;
	    }


	public static List<XmlPackage> getPackages(String projectPackage) {
        List<XmlPackage> allPackages = new ArrayList<XmlPackage>();
        XmlPackage eachPackage = new XmlPackage();
        eachPackage.setName(projectPackage);
        allPackages.add(eachPackage);
        return allPackages;
    }

//	 XmlSuite suite = new XmlSuite();
		// suite.setName(projectName);
	 //XmlTest xmltest = new XmlTest(suite);

//	 else {
//	 xmltest.setXmlClasses(Arrays.asList(new XmlClass(projectPackage + "TestHotelIbibo")));
//}
//testng.setXmlSuites(Arrays.asList(suite));

	 //xmltest.setName(scheduler.getType());
	 
	// xmltest.addParameter("browser", scheduler.getBrowser());

	// List<XmlPackage> listPackage = new ArrayList<XmlPackage>();
	// XmlPackage xmlPackage = new XmlPackage();
	// xmlPackage.setName(projectPackage + "*");
	 //xmltest.setPackages(getPackages(projectPackage));

	
	/* Add package to run 
	 List<XmlPackage> listPackage = new ArrayList<XmlPackage>();
	 XmlPackage xmlPackage = new XmlPackage();
	 xmlPackage.setName("com.framework.tests.goibibo.smoke.*");
	 xmltest.setXmlPackages(listPackage);
	*/
	
	// add a suite-file to the suite
	//suite.setSuiteFiles(Arrays.asList("./Suite1.xml"));

	// 1. To run with testng.xml file, uncomment this one, comment 2
	// testng.setTestSuites(Arrays.asList("testng.xml"));

	// 2. to run with XmlSuite, uncomment this one, comment 1
}