package com.framework.tests.passbrains.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.sikuli.script.Key;

import com.framework.core.SeleniumLibrary;
import com.framework.core.SeleniumLibrary.commandEnum;

public class UPCcablecomRegistrationPage extends SeleniumLibrary {

	protected int timeout = 10;
	String firstName = "PB Platform";
	String lastName = "Automated Testing Bangalore";
	String email = "automation_demo_live@yahoo.com";
	String city = "Zurich";
	String zip = "1400";
	String street = "Virani Park";
	String text= "Expert";
	String customerID = "5744096";
	String phone = "9886863191";
	String availabilty = "5";
	
	public UPCcablecomRegistrationPage(WebDriver driver) {
		this.driver = driver;
	}

	public void enterProfilePageDetails () throws Exception {
		sendKeys(By.id("first_name"),firstName);
		sendKeys(By.id("last_name"),lastName);
		sendKeys(By.id("email"),email);
	    sendKeys( By.id("zip"),zip);
	    sendKeys( By.xpath("//input[@id='cityc']"),city);
	    try {
		    waitFindElement(driver, By.xpath("//li[contains(@class,'ui-menu-item')]//a[contains(.,'Zurich')][1]"));
		    click(By.xpath("//li[contains(@class,'ui-menu-item')]//a[contains(.,'Zurich')][1]"), timeout);
	    } catch(Exception e) {
	    wait(5);
    	sikuli.pressKey(Key.DOWN);
    	wait(2);
    	sikuli.pressKey(Key.DOWN);
       	sikuli.pressKey(Key.ENTER);
	    }
	    sendKeys(By.id("street"),street);
		click(By.xpath("//*[@id='gender2']"),timeout);
		seleniumCommand(commandEnum.select, By.id("year"),"1988");	
		
		WebElement dropdownbox = driver.findElement(By.id("technical_acumen"));
		Select click = new Select(dropdownbox);
		click.selectByVisibleText(text);
		
		sendKeys( By.id("customer_id"),customerID);
		
		WebElement dropdownbox1 = driver.findElement(By.id("decision_maker"));
		Select click1 = new Select(dropdownbox1);
		click1.selectByVisibleText("Yes");
		
		WebElement dropdownbox2 = driver.findElement(By.id("cablecom_employee"));
		Select click2 = new Select(dropdownbox2);
		click2.selectByVisibleText("Yes");
		
		sendKeys(By.id("phone"),phone);
		click(By.id("terms"),timeout);
		sendKeys(By.id("availability"), availabilty);
	}
	
	public void selectDesktopConfi(){
		//Config---Windows,Windows 8.1,English,IE-7
		click(By.xpath("//select[@id='os_version']/option[contains(.,'Windows')]"),timeout);
		click(By.xpath("//select[@id='os_system_language']/option[contains(.,'Windows 8.1')]"),timeout);
		click(By.xpath("//select[@id='os_language']/option[contains(.,'English')]"),timeout);
		click(By.xpath("//div[@id='formholder']/div[1]//li[16]/label"),timeout); 
	}
	
	public void clickOnAddDesktopPC(){
		click(By.xpath("//*[@value='Add Desktop PC']"),timeout);
	}
	
	public void selectMobileConfi(){
		//Config---Apple,iPad 2,iOS 7.0,BSNL
		click(By.xpath("//select[@id='manufacturer']/option[contains(.,'Apple')]"),timeout);
		click(By.xpath("//select[@id='model']/option[contains(.,'iPad 2')]"),timeout);
		click(By.xpath("//select[@id='mobile_os']/option[contains(.,'iOS 7.0')]"),timeout);
		click(By.xpath("//select[@id='carrier']/option[contains(.,'Orange')]"),timeout); 	
	}
	
	public void clickOnAddMobileDevice(){
		click(By.xpath("//*[@value='Add Mobile Device']"),timeout);
	}
	
	public void selectTelevisionSet() {
		WebElement dropdownbox1 = driver.findElement(By.id("tv_sets_number"));
		Select click1 = new Select(dropdownbox1);
		click1.selectByVisibleText("0");
		click(By.xpath("//div[@id='register-merged']//input[@value='Next']"),timeout);
	}
	
	public void selectPage3Credentials(){
		//online TV
		click(By.id("upc cablecom"),timeout);
		//TV on mobile
		click(By.id("upc cablecom_1"),timeout);
		
		WebElement dropdownbox1 = driver.findElement(By.id("tv-customer-select"));
		Select click1 = new Select(dropdownbox1);
		click1.selectByVisibleText("No");
		
		WebElement dropdownbox2 = driver.findElement(By.id("additional-provider-select"));
		Select click2 = new Select(dropdownbox2);
		click2.selectByVisibleText("No");
		
		WebElement dropdownbox3 = driver.findElement(By.id("internet-customer"));
		Select click3 = new Select(dropdownbox3);
		click3.selectByVisibleText("No");
		
		WebElement dropdownbox4 = driver.findElement(By.id("internet-provider"));
		Select click4 = new Select(dropdownbox4);
		click4.selectByVisibleText("No");
		
		WebElement dropdownbox5 = driver.findElement(By.id("phone-customer"));
		Select click5 = new Select(dropdownbox5);
		click5.selectByVisibleText("No");
		
		WebElement dropdownbox6 = driver.findElement(By.id("phone-provider"));
		Select click6 = new Select(dropdownbox6);
		click6.selectByVisibleText("No");
	}
	
	//div[@class='button-holders fixedbuttons']//input[@value='Register']
	public void registerUPC(){
		click(By.xpath("//div[@class='button-holders fixedbuttons']//input[@value='Register']"),timeout);
	}
	
	public void signinPB() throws Exception{
		click(By.xpath("//a[@id='login2']/img"),timeout);
		sendKeys(By.id("emaillog"),"automation_demo_live@yahoo.com");
		sendKeys(By.id("passwordlog"),"pass1234");
		click(By.id("submit-login"),timeout);
		
	}
	
}