package com.framework.tests.surfaces.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.framework.tests.surfaces.SurfaceLibrary;

public class LoginPage extends SurfaceLibrary{
	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void login () throws Exception{
		goToURL("http://115.248.199.156/Surfaces-Staging/login.aspx");
		sendKeys(By.xpath("//input[contains(@id,'UserName')]"), "admin@dev.alpha-vision.com");
		sendKeys(By.xpath("//input[contains(@id,'Password')]"), "alpha123");
		click(By.xpath("//input[contains(@value,'Sign In')]"), timeout);
	}

}
