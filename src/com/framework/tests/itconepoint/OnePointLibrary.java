package com.framework.tests.itconepoint;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import com.framework.core.Global;
import com.framework.core.SeleniumLibrary;
import com.framework.tests.itconepoint.page.OnePointLogin;


public class OnePointLibrary extends SeleniumLibrary {

	protected OnePointLogin onePointLogin = null;
	
	
	static final Logger logger = Logger.getLogger(OnePointLibrary.class);
	int timeout = 20;
	
	@Parameters("browser")
	@BeforeClass
	 public void setup(String browser) throws Exception {
		 PropertyConfigurator.configure("log4j.properties");
		 Global.setBrowserName(browser);
		 assignBrowser();
		 Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		 String browserName = cap.getBrowserName().toLowerCase();
		 Global.TEST_SET.add(browserName + "@" + cap.getVersion().toString());
		 onePointLogin = new OnePointLogin(driver);
	 }

	 @AfterClass
	 public void tearDown() {
		 closeDriver();
	 }
	 
 
}
