package com.framework.tests.passbrains.page;

import java.util.Calendar;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.sikuli.script.Key;

import com.framework.core.SeleniumLibrary;
import com.framework.core.SeleniumLibrary.commandEnum;


public class CustomerRegisterPage extends SeleniumLibrary {

	protected int projectStart = 0;
	protected int projectDeadLine = 0;
	protected int timeout = 10;
	String firstName = "PB Platform";
	String lastName = "Automated Testing Bangalore";
	String email = "automation_demo_live@yahoo.com";
	//String email = "pb.automation@outlook.com";
	String phone = "9886863191";
	String skype = "Skype Test Automation";
	String payPal = "bug.wrangler@outlook.com";
	String city = "Bangalore";
	String zip = "363310";
	String street = "Virani Park";
	String job = "Test Automation";
	String department = "Automation";
	String company = "Pass";
	
	public CustomerRegisterPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void nonLinkedIn() throws Exception{
		sendKeys(By.id("first_name"),firstName);
		sendKeys(By.id("last_name"),lastName);
	}

	public void enterProfilePageDetails () throws Exception {
		wait(10);
		Boolean test1 = driver.findElement(By.className("notice-link")).isDisplayed();
		System.out.println(test1);
		if(test1 == false){
		nonLinkedIn();
		}
		sendKeys(By.id("email"),email);
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
	    sendKeys(By.id("job"),job);
	    sendKeys(By.id("department"),department);
	    sendKeys(By.id("company"),company);
	    click(By.xpath("//select[@id='industry']/option[16]"), timeout);
		sendKeys(By.id("phonec"), phone);
	}
	
	public void clickCustomerTester(){
		click(By.id("customer-tester"),timeout);
	}
	
	public void enetrCustomerTesterDetails() throws Exception{
		enterProfilePageDetails();
		click(By.xpath("//*[@id='gender2']"),timeout);
		seleniumCommand(commandEnum.select, By.id("year"),"1988");
		click(By.xpath("//select[@id='hours']/option[contains(.,'1-5')]"),timeout);
		click(By.id("terms"),timeout);	
	}
	
	public void selectProjectStartdate() throws InterruptedException{
		click(By.id("date_start"), timeout);
		Calendar now = Calendar.getInstance();
	    now.add(Calendar.DATE, -4);
	    projectStart = now.get(Calendar.DATE);
	    System.out.println(projectStart);
	    executeClickJs(driver.findElement(By.xpath("//div[contains(@class,'first')]//table[1]//a[contains(.,"+projectStart+")]")),timeout);
	}
	
	public void selectProjectDeadlinedate() throws InterruptedException{
		click(By.id("date_end"), timeout);
		Calendar now = Calendar.getInstance();
	    now.add(Calendar.DATE, 8);
	    projectDeadLine = now.get(Calendar.DATE);
	    System.out.println(projectDeadLine);
	    executeClickJs(driver.findElement(By.xpath("//div[contains(@class,'first')]//table[1]//a[contains(.,"+projectDeadLine+")]")),timeout);
	}
	
	public void enterTheBudget() throws Exception{
		sendKeys(By.id("budget"), "10");
	}
	
	public void enterDetailsOfProject() throws Exception{
		sendKeys(By.id("detailsproject"), "This is Automation Script ");
	}
	
}