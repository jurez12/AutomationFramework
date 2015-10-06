package com.framework.tests.passbrains.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.sikuli.script.Key;
import com.framework.core.SeleniumLibrary;

public class NoVoiceRegistrationPage extends SeleniumLibrary {
	protected int timeout = 10;
	String firstName = "PB Platform";
	String lastName = "Automated Testing Bangalore";
	String email = "automation_demo_live@yahoo.com";
	String phone = "9886863191";
	String skype = "Skype Test Automation";
	String payPal = "bug.wrangler@outlook.com";
	String city = "Bangalore";
	String zip = "363310";
	String street = "Virani Park";
	String exp = "4";
	
	public NoVoiceRegistrationPage(WebDriver driver) {
		this.driver = driver;
	}

	public void selectNoviceRadioButton(){
		click(By.id("tester-type2"), timeout);
	}
	
	public void enterProfilePageDetails () throws Exception {
		sendKeys(By.id("first_name"),firstName);
		sendKeys(By.id("last_name"),lastName);
		sendKeys(By.id("email"),email);
		sendKeys(By.id("phone"),phone);
		sendKeys(By.id("skype"),skype);
		sendKeys(By.id("paypal"),payPal);
		seleniumCommand(commandEnum.select,By.id("country"),"India");
	    sendKeys(By.xpath("//*[@id='cityc']"),city);
	    try {
		    click(By.xpath("//li[contains(@class,'ui-menu-item')]//a[contains(.,'Bangalore')][1]"), 5);
	    } catch(Exception e) {
	    	sikuli.pressKey(Key.DOWN);
	    	sikuli.pressKey(Key.DOWN);
	       	sikuli.pressKey(Key.ENTER);
	    }
	    sendKeys( By.id("zip"),zip);
	    sendKeys(By.id("street"),street);
		click(By.xpath("//*[@id='gender2']"),timeout);
		seleniumCommand(commandEnum.select, By.id("year"),"1988");
		click(By.xpath("//select[@id='hours']/option[contains(.,'1-5')]"),timeout);
		Boolean term = driver.findElement(By.id("terms")).isDisplayed();
		if(term==true){
		click(By.id("terms"),timeout);
		}
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
		click(By.xpath("//select[@id='carrier']/option[contains(.,'BSNL')]"),timeout); 	
	}
	
	public void clickOnAddMobileDevice(){
		click(By.xpath("//*[@value='Add Mobile Device']"),timeout);
	}
	

	
}