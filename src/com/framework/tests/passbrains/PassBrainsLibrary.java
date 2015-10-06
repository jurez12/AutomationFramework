package com.framework.tests.passbrains;

import java.io.File;

import org.openqa.selenium.By;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import com.framework.core.Global;
import com.framework.core.Library;
import com.framework.core.SeleniumLibrary;
import com.framework.helper.YahooEmail;
import com.framework.library.utils.MiscUtils;
import com.framework.tests.passbrains.page.CustomerRegisterPage;
import com.framework.tests.passbrains.page.EditProfile;
import com.framework.tests.passbrains.page.HomePage;
import com.framework.tests.passbrains.page.NoVoiceRegistrationPage;
import com.framework.tests.passbrains.page.ProfessionalRegisterPage;
import com.framework.tests.passbrains.page.UPCcablecomRegistrationPage;

public class PassBrainsLibrary extends SeleniumLibrary {
	protected String userName;
	protected String password;
	
	static final Logger logger = Logger.getLogger(PassBrainsLibrary.class);
	protected HomePage homePage=null;
	protected NoVoiceRegistrationPage noVoiceNextregistrationpage = null; 
	protected ProfessionalRegisterPage professionalNextRegister = null;
	protected CustomerRegisterPage customerRegister =null;
	protected String subject ="Activate your account on passbrains";
	protected UPCcablecomRegistrationPage upcCablecom = null;
	protected EditProfile editProfile = null;
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
		 homePage = new HomePage(driver);
		 noVoiceNextregistrationpage = new NoVoiceRegistrationPage(driver);
		 professionalNextRegister = new ProfessionalRegisterPage(driver);
		 customerRegister = new CustomerRegisterPage(driver);
		 upcCablecom = new UPCcablecomRegistrationPage(driver);
		 editProfile = new EditProfile(driver);
	 }

	 @AfterClass
	 public void tearDown() {
		 closeDriver();
	 }
	 
 public void deleteAllActivationEmail () {
	 try {
	 YahooEmail.deleteAllYahooEmail(subject);
	 } catch (Exception e) {
		 System.out.println("Delete email by subject " + subject + " Email Issue " + e);
	 }
 }
	 public void activateThroughEmail() throws Exception {
		 try {
		   String link = null;
		   String activationCode = null;
		   for (int i = 0; i < 5; i++) {
			   wait(5);
		       String content = YahooEmail.readBySubject(subject);
		       activationCode =YahooEmail.getActivationCode(content);
		       link = YahooEmail.getActivationLink(content);
		       if (content.length() > 100) {
		    	   break;
		       }
		   }
	       goToURL(link);

	       String newPassword = "pass1234";
	       click(By.xpath("//input[@name='password']"), 30);
	       sendKeys(By.xpath("//input[@name='password']"),  activationCode);
	       sendKeys(By.xpath("//input[@name='new_password']"),  newPassword);
	       sendKeys(By.xpath(" //input[@name='new_password_repeat']"),  newPassword);
	   	   click(By.xpath("//input[@value='Activate']"), 20);
	       verifyElementIsEnabled(driver.findElement
	    		 (By.xpath("//p[contains(.,'Your account has been successfully activated')]")));
		 } catch(Exception e) {
			System.out.println("Actication Email Not Found, issue : " + e);
		 }
	   	
	 }
	 
		public void passAdminCallTester() throws Exception{
			makeAdminCall("http://juthika.paul@pass.ch:!juthik@@sandbox.passbrains.com");
			//isDisplayed(By.xpath("//a[contains(.,'Join Now')]"), timeout);
			makeAdminCall("http://sandbox.passbrains.com/controllers/captchaController.php?action=testavoid");		
			//isDisplayed(By.xpath("//body[contains(.,'Captcha is disabled!')]"), timeout);
			makeAdminCall("http://sandbox.passbrains.com/controllers/registerController.php?action=deleteMember&email=automation_demo_live@yahoo.com");
			//isDisplayed(By.xpath("//a[contains(.,'Join Now')]"), timeout);
			makeAdminCall("http://sandbox.passbrains.com");
		}
		
		public void passAdminCallCustomer() throws Exception{
			makeAdminCall("http://juthika.paul@pass.ch:!juthik@@sandbox.passbrains.com");
			makeAdminCall("http://sandbox.passbrains.com/controllers/captchaController.php?action=testavoid");		
			//isDisplayed(By.xpath("//body[contains(.,'Captcha is disabled!')]"), timeout);
			makeAdminCall("http://sandbox.passbrains.com/controllers/registerController.php?action=deleteMember&email=automation_demo_live@yahoo.com");
			//isDisplayed(By.xpath("//a[contains(.,'Join Now')]"), timeout);
			makeAdminCall("http://sandbox.passbrains.com/register.php?user_type=customer");
		}
		
			public void enterpriseCall() throws Exception{
			makeAdminCall("http://juthika.paul@pass.ch:!juthik@@sandbox.passbrains.com");
			makeAdminCall("http://sandbox.passbrains.com/controllers/captchaController.php?action=testavoid");		
			//isDisplayed(By.xpath("//body[contains(.,'Captcha is disabled!')]"), timeout);
			makeAdminCall("http://sandbox.passbrains.com/controllers/registerController.php?action=deleteMember&email=automation_demo_live@yahoo.com");
			//isDisplayed(By.xpath("//a[contains(.,'Join Now')]"), timeout);
			makeAdminCall("http://sandbox.passbrains.com/login-enterprise.php?ref=defa19e5aada04e488f28ac273df0a8c56c3cf06");
		}
}
