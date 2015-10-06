package com.framework.tests.passbrains.page;

import java.util.Calendar;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.framework.core.SeleniumLibrary;
import com.framework.core.SeleniumLibrary.commandEnum;

public class HomePage extends SeleniumLibrary {
	protected int timeout =10;
	public String actual = "Thank You";
	
	public HomePage(WebDriver driver) {
		this.driver =driver;
	}

	public void clickOnJoinNow() {
		click(By.id("registertype"),timeout);
	}
	
	public void clickOnTester() {
		//click(By.xpath("//*[@id='expbut']/a/span"),timeout);
		//click(By.id("//*[@id='crowd-join']"),timeout);
		click(By.xpath("//div[@class='type-choose']/div/input[@id='crowd-join']"),timeout);
		click(By.xpath("//input[@id='join-now']"),timeout);
	}
	
	public void clickOnUPCcablecom() {
		click(By.xpath("//div[@class='type-choose']/div/input[@id='cablecom-join']"),timeout);
		click(By.xpath("//input[@id='join-now']"),timeout);
	}
	
	public void clickOnClosePopUp() {
		click(By.id("fancybox-close"),timeout);
	}
	

    public void clickOnLogin() {
    	click(By.id("login"),timeout);
    }  
    
    public void validatingConfirmation() {
    	String conf = getText(By.xpath("html/body/div[1]/div[3]/div/div"), 5);
       	conf.contains(actual);
     //  	driver.manage().deleteAllCookies();
    }
    
    public void clickOnRegisterNow() throws InterruptedException {
    	click(By.xpath("//div[@class='button-holders fixedbuttons']//input[@value='Register Now']"),timeout);
    	Boolean dd=driver.findElement(By.id("fancybox-content")).isDisplayed();
    	System.out.println(dd);
    	if(dd==true){
    		executeClickJs(driver.findElement(By.xpath("//div[@id='leave-register-page-msg-container']/input[2]")),timeout);
    	}
	}
	
	public void clickOnCancel(){
		  click(By.name("Cancel"),timeout);
	}
	
	public void clickOnNext(){
		  click(By.xpath("//div[@id='register-merged']//input[@value='Next >']"),timeout);
	}
	
	public void signINLinkedIN() throws Exception { 
		String title = driver.getTitle();
		System.out.println(title);
		click(By.xpath("//span[contains(.,'Sign in with Linked') and (contains(@id,'title-text'))]"), timeout);
		wait(10);
		try {
			switchToWindowByTitle("Authorize | LinkedIn");
		    boolean frame=driver.findElement(By.id("frame-contents")).isDisplayed();
            System.out.println(frame);
		} catch(Exception e) {
			System.out.println("Error "+ e);
		}
		switchToDefault();
		sendKeys(By.id("session_key-oauthAuthorizeForm"), "shivashankar.ram.123@gmail.com");
		sendKeys(By.id("session_password-oauthAuthorizeForm"), "shivu@123");
		click(By.className("allow"), timeout);
		switchToWindowByTitle(title);
	}
	
	public void signOut() throws Exception {
		wait(10);
		AssertResult("Logout", getText(By.xpath("//a[contains(.,'Logout')]")));
		click(By.xpath("//a[contains(.,'Logout')]"),timeout);
		//driver.manage().deleteAllCookies();
		wait(15);
	}
	
	public void signUPenterprise(){
		click(By.xpath("//li[@class='join']/a"),timeout);
	}
	
	public void clickUpdateProfile() throws InterruptedException {
		wait(2);
		click(By.xpath("//li[@class='user']/a[contains(@href,'profile.php')]"), timeout);
		//executeClickJs(driver.findElement(By.xpath("//li[@class='user']/li[@class='user']")),timeout);
	
	}
	
}