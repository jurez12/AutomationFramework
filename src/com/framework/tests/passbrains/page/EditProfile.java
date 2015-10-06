package com.framework.tests.passbrains.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.sikuli.script.Key;

import com.framework.core.SeleniumLibrary;
import com.framework.core.SeleniumLibrary.commandEnum;

public class EditProfile extends SeleniumLibrary {

protected int timeout = 10;
String firstName = "PB Platform NewName";
String lastName = "Automated Testing Bangalore NewName";
String city = "Mumbai";
String company = "Pass Technology";
String phon = "9880287073";

	public EditProfile(WebDriver driver) {
		this.driver = driver;
}

	public void clickOnEditProfile(){
			driver.findElement(By.xpath("//ul[@class='profile']//input[@class='button editprofile']")).click();
}
	
	public void editProfileOfCurrentUser() throws Exception{
		clearAndsendKeys(By.id("first_name"),firstName);
		clearAndsendKeys(By.id("last_name"),lastName);
			seleniumCommand(commandEnum.select,By.id("country"),"India");
			clearAndsendKeys(By.xpath("//*[@id='cityc']"),city);
//		    try {
//			    click(By.xpath("//li[contains(@class,'ui-menu-item')]//a[contains(.,'Mumbai')][1]"), 5);
//		    } catch(Exception e) {
//		    	sikuli.pressKey(Key.DOWN);
//		       	sikuli.pressKey(Key.ENTER);
//		    }  
			Boolean phonechage = driver.findElement(By.id("phone")).isDisplayed();
			if(phonechage==true){
		    clearAndsendKeys(By.id("phone"),phon); 
			}
			else{
			clearAndsendKeys(By.id("phonec"),phon);	
			}
		}
	
	public void saveEditProfile(){
		click(By.xpath("//div[@class='gray-row-top']//input[@class='button submit-next']"), timeout);
	}
	
	public void verifyEditedField(){
		//Validating FirstName
		String firstNam = driver.findElement(By.xpath("//li/span[text()='First Name']/..")).getText();
		AssertResult(firstName, firstNam);
		assert firstNam.contains(firstName);
		
		//Validating LastName
		String lastNam = driver.findElement(By.xpath("//li/span[text()='Last Name']/..")).getText();
		AssertResult(lastName, lastNam);
		assert lastNam.contains(lastName);
		
		//Validating City
		String cit = driver.findElement(By.xpath("//li/span[text()='City']/..")).getText();
		AssertResult(lastName, lastNam);
		assert cit.contains(city);	
		
		//Validating phone
		String pho = driver.findElement(By.xpath("//li/span[text()='Phone']/..")).getText();
		AssertResult(phon, pho);
		assert pho.contains(phon);
		
	}
}
	
