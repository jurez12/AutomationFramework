package com.framework.tests.itconepoint.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.framework.core.SeleniumLibrary;


public class OnePointLogin extends SeleniumLibrary {

	protected int timeout = 10;
	protected String userId = "8631";
	protected String password = "Itcinfotech9(";

	public OnePointLogin(WebDriver driver) {
		this.driver = driver;
	}
	
	public void login() throws Exception{
		goToURL("http://i3lerpweb4.itcinfotech.com:8000/psp/OPPORTAL/?cmd=login&languageCd=ENG&");
		clearAndsendKeys(By.xpath("//input[@id='userid']"), userId);
		wait(2);
		clearAndsendKeys(By.xpath("//input[@id='pwd']"), password);
		wait(2);
		click(By.xpath("//input[@name='Submit']"), timeout);
	}

}
	
