package com.framework.suite;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.framework.library.utils.MiscUtils;
public class MultiBrowser {
 private WebDriver driver;

 @Parameters("browser")
 @BeforeMethod
 public void setup(String browser)
 {
  if(browser.equalsIgnoreCase("firefox"))  {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("capability.policy.default.Window.frameElement", "allAccess");
		try {
			if(!MiscUtils.empty(System.getProperty("plugin.firebug"))) {
			profile.addExtension(new File(".\\libs\\firebug.xpi"));
			profile.setPreference("extensions.firebug.currentVersion", System.getProperty("plugin.firebug"));
			}
			if(!MiscUtils.empty(System.getProperty("plugin.firepath"))) {
			profile.addExtension(new File(".\\libs\\firepath.xpi"));
			}
			driver = new FirefoxDriver(profile);
		} catch (Exception ex) {
			profile = new FirefoxProfile();
			profile.setPreference("capability.policy.default.Window.frameElement", "allAccess");
			driver = new FirefoxDriver(profile);
		}
  } else if(browser.equalsIgnoreCase("iexplorer")) {
		File file = new File("libs\\IEDRiverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath() );
		driver = new InternetExplorerDriver();
  } else if(browser.equalsIgnoreCase("chrome")) {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			System.setProperty("webdriver.chrome.driver", "libs/chromedriver.exe");
		} else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			System.setProperty("webdriver.chrome.driver", "libs/chromedriver");
		}
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		options.addArguments("--disable-bundled-ppapi-flash");
		driver = new ChromeDriver(options);
  }
  driver.manage().window().maximize();
 }

 @AfterMethod
 public void tearDown()
 {
 driver.quit();
 }

 @Test
 public void testMultiBrowser() throws InterruptedException
 {
  driver.get("http://www.google.com");
  Thread.sleep(3000);
 }
}