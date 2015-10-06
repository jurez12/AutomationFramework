package com.framework.tests.surfaces.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.framework.tests.surfaces.SurfaceLibrary;

public class ListFloorOrderPage extends SurfaceLibrary {
	
	public ListFloorOrderPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void verifyFloorOrders() {
		click(By.xpath("//li[contains(@class,'flooring')]/a[contains(@href,'listflooring')]"), timeout);
		isDisplayed(By.xpath("//a[contains(.,'Order#')]"), timeout);
		isDisplayed(By.xpath("//a[contains(.,'Community')]"), timeout);
		isDisplayed(By.xpath("//a[contains(.,'Lot#')]"), timeout);
		isDisplayed(By.xpath("//a[contains(.,'Date')]"), timeout);
		isDisplayed(By.xpath("//a[contains(.,'Customer')]"), timeout);
		isDisplayed(By.xpath("//th[contains(.,'Warning')]"), timeout);
		isDisplayed(By.xpath("//th[contains(.,'Status')]"), timeout);
		
	}

}
