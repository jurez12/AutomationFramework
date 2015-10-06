package com.framework.tests.surfaces.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.framework.tests.surfaces.SurfaceLibrary;

public class FlooringPage  extends SurfaceLibrary {
	
	String productName = "Alliance";
	String subProductName = "test padding";
	
	public FlooringPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void verifyFloor(String imagePath) throws Exception {
		goToURL("http://115.248.199.156/Surfaces-Staging/flooringpage.aspx?PlanID=70");
		//goToURL("http://115.248.199.156/Surfaces-Staging/selectplan.aspx");
		//click(By.xpath("//*[@id='ContentPlaceHolder1_dtPlanList_listItemPlanName_0']"), timeout);
		click(By.xpath("//*[@id='Patio']"), timeout);
		sendKeys(By.xpath("//div[contains(@id, 'template_options')]//input[contains(@id,'txtNew')]"), "NewTemplate");
		click(By.xpath("//div[contains(@id, 'template_options')]//input[@type='button']"), timeout);
		click(By.xpath("//a[contains(@title,'Draw Surface')]"), timeout);
		sikuli.waitAndClickContinueOnFail(imagePath +"corner.png", HIGH_TOLERANCE, TIME_OUT);
		sikuli.waitAndClickContinueOnFail(imagePath +"corner2.png", HIGH_TOLERANCE, TIME_OUT);
		sikuli.waitAndClickContinueOnFail(imagePath +"corner3.png", HIGH_TOLERANCE, TIME_OUT);
		wait(5);
		sikuli.waitAndRightClick(imagePath +"patio_color.png", LOW_TOLERANCE, TIME_OUT, false);
//		wait(5);
//		sendKeys(By.xpath("//input[@id='txtAreaname']"), "My Area");
//
//		for (int i =0; i< 5; i++) {
//			  click(By.xpath("//div[contains(@class,'col_product')]//a[contains(@onclick,'product')]/img[@class='advance_search']"), timeout);
//			  wait(5);
//			  if (isElementVisible(By.xpath("//form/div[3]/*[@id='txtProduct_search']"), 5)) {
//					break;
//			  }
//		}
//		sendKeys(By.xpath("//form/div[3]/*[@id='txtProduct_search']"), productName);
//		wait(5);
//		click(By.xpath("//form/div[4]//input[@type='button']"), timeout);
//		wait(2);
//		click(By.xpath("//img[contains(@src,'cs_plus.jpg')]"), timeout);
//		for (int i =0; i< 5; i++) {
//			  click(By.xpath("//div[contains(@class,'col_sub_product')]//a[contains(@onclick,'product') ]/img[@class='advance_search']"), timeout);
//			  wait(5);
//			  if (isElementVisible(By.xpath("//form/div[3]/*[@id='txtProduct_search']"), 5)) {
//					break;
//				}
//		}
//		sendKeys(By.xpath("//form/div[3]/*[@id='txtProduct_search']"), subProductName);
//		wait(5);
//		click(By.xpath("//form/div[4]//input[@type='button']"), timeout);
//		wait(2);
//		click(By.xpath("//img[contains(@src,'cs_plus.jpg')]"), timeout);
//		wait(5);
//
//		click(By.xpath("//*[@id='ddlInstallMethode']/option[contains(.,'BERBER')]"), timeout);
//		wait(5);
//		click(By.xpath("//*[@id='ddlInstaller']/option[contains(.,'INTERIOR SPECIALISTS')]"), timeout);
//		click(By.xpath("//*[@id='ddlProductColor']/option[contains(.,'01 ADMIRE')]"), timeout);
//		boolean unitPrice = verifyTextPresent(By.xpath("//*[@id='txtUnitPrice']"), "20.4");
//		boolean totalPrice = verifyTextPresent(By.xpath("//*[@id='txtTotalPriceNew']"), "0.00");
//		if (!unitPrice || !totalPrice) {
//			log("Unit price and total price is not correct");
//		}
//		for (int i =0; i< 5; i++) {  
//			click(By.xpath("//*[@id='shapepropertiestabs']//a[contains(.,'Unit')]"), timeout);
//			wait(5);
//		    if (isElementVisible(By.xpath("//*[@id='txtCarpet5']"), 5)) {
//				break;
//			}
//		}
//		clearAndsendKeys(By.xpath("//*[@id='txtCarpet5']"), "5");
//		clearAndsendKeys(By.xpath("//*[@id='txtPadding6']"), "10");
//		click(By.xpath("//a[contains(@title,'Save And Close')]"), timeout);
//		sikuli.verifyImage(imagePath +"final.png", HIGH_TOLERANCE, TIME_OUT);
	}
}
