package com.framework.tests.surfaces;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import com.framework.core.Global;
import com.framework.core.SeleniumLibrary;
import com.framework.tests.surfaces.pages.FlooringPage;
import com.framework.tests.surfaces.pages.ListFloorOrderPage;
import com.framework.tests.surfaces.pages.LoginPage;


public class SurfaceLibrary  extends SeleniumLibrary {
	protected FlooringPage floorPage;
	protected ListFloorOrderPage listFloorOrderPage;
	protected LoginPage loginPage;
    protected int timeout = 10;
    
	@Parameters("browser")
	@BeforeClass
	 public void setup(String browser) throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		Global.setBrowserName(browser);
		assignBrowser();
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		String browserName = cap.getBrowserName().toLowerCase();
		Global.TEST_SET.add(browserName + "@" + cap.getVersion().toString());
		IMAGE_PATH =  new File(".").getCanonicalPath() + "\\src\\com\\framework\\tests\\surfaces\\images\\";
		floorPage = new FlooringPage(driver);
		loginPage= new LoginPage(driver);
		listFloorOrderPage = new ListFloorOrderPage(driver);
	}
	
	 @AfterClass
	 public void tearDown() {
		 closeDriver();
	 }	
}
