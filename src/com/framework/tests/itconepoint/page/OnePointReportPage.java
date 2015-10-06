package com.framework.tests.itconepoint.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.framework.core.SeleniumLibrary;

public class OnePointReportPage extends SeleniumLibrary {

	protected int timeout = 10;


	public OnePointReportPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void verifyLogin() throws Exception{
		verifyTextPresent(By.xpath("//h2[contains(.,'Enterprise')]"), "Enterprise");
	
	}

}
	