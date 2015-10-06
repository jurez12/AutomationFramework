package com.framework.core;

import io.appium.java_client.AppiumDriver;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;

public class MobileLibrary {
	
	protected static AppiumDriver driver = null;
	protected static int waitTimeLong = 5;
	protected static int waitTimeShort = 1;
    private static WebDriverWait driverWait;
	
	 protected void wait(int timeout) throws InterruptedException {
		 Thread.sleep(timeout * 1000);
	 }
	 
	protected  void executeJS( final int para1, final int para2, final double para3, final int para4, final int para5) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("mobile: tap", new HashMap<String, Double>() {{ put("tapCount",(double) para1); put("touchCount", (double)para2); put("duration",(double) para3); put("x", (double)para4); put("y", (double)para5); }});
	}
	
	protected static void waitAndClick(AppiumDriver driver, By locatorname) throws InterruptedException {
		verifyIsPresent(driver, locatorname);
		driver.findElement(locatorname).click();
	}
	
	public void swipeHorizontal() {  
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    HashMap<String, Double> swipeObject = new HashMap<String, Double>();
	    swipeObject.put("startX", 0.95);
	    swipeObject.put("startY", 0.5);
	    swipeObject.put("endX", 0.05);
	    swipeObject.put("endY", 0.5);
	    swipeObject.put("duration", 1.8);
	    js.executeScript("mobile: swipe", swipeObject);
     }
	
	public void swipeVertical() {  
		System.out.println("The swipe Vertical" );
		driver.swipe(200,600,200,300,700);
     }
	
	
	protected static boolean verifyIsPresent(AppiumDriver driver, By locatorname) throws InterruptedException {
		WebElement element = driver.findElement(locatorname);
		for (int i = 0; i < 40; i++) {
			try {
				if (element.isDisplayed()){
					System.out.println("Element " + locatorname + " found");
					Thread.sleep(waitTimeShort);
					return true;
				} else {
					System.out.println("Element " + locatorname + " found");
				}
			} catch(Exception e) {
					System.out.println("Element " + locatorname + " not found");
					Thread.sleep(waitTimeShort);
			}
		}
		return false;
	}
	
	protected void clickMobElement(WebElement element) {
		 if(element != null) {
			 element.click();
		 }
	}
	
	protected WebElement getMobileElement(By by) {
		try {
			return driver.findElement(by);
		} catch (Exception e) {
			System.out.println ("No found By" + by.toString());
			return null;
		}
	}
	
	protected String getText(By by) {
		try {
	   return driver.findElement(by).getText();
		} catch (Exception e) {
			System.out.println ("No found By" + by.toString());
			return null;
		}
	}
	
	protected void sendKeys(By by, String send) {
		getMobileElement(by).sendKeys(send);
	}
	
	public void softAssert(String expected, String actual) {
		if (actual.contains(expected)) {
			System.out.println(" Soft Assert Expected String  " + expected + "contains  Actual String " + actual);
		} else {
			System.out.println("Soft Assert Expected String " + expected + "not contains Actual String " + actual);
		} 
	}
	
	public void hardAssert(String expected, String actual) {
		Assert.assertEquals(actual, expected, "Hard Assert Expected String " + expected + "not contains Actual String " + actual);
	}
	
	public void AssertBooleanWithLog(boolean status, String log) {
		Assert.assertTrue(status, "Error with log" + log);
	}

	 /**
	 * Set implicit wait in seconds *
	 */
	public static void setWait(int seconds) {
	    driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	 }

	/**
	 * Return an element by locator *
	*/
	public static WebElement element(By locator) {
	    return driver.findElement(locator);
	}

	  /**
	   * Return a list of elements by locator *
	   */
	public static List<WebElement> elements(By locator) {
	    return driver.findElements(locator);
	}

	/**
	   * Press the back button *
	*/
	public static void back() {
	    driver.navigate().back();
	}

	/**
	   * Return a list of elements by tag name *
	*/
	public static List<WebElement> tags(String tagName) {
	    return elements(for_tags(tagName));
	}

	/**
	   * Return a tag name locator *
	*/
	public static By for_tags(String tagName) {
	    return By.className(tagName);
	}

	/**
	   * Return a static text element by xpath index *
	*/
	public static WebElement s_text(int xpathIndex) {
	    return element(for_text(xpathIndex));
	}

	/**
	   * Return a static text locator by xpath index *
	*/
	 public static By for_text(int xpathIndex) {
	    return By.xpath("//android.widget.TextView[" + xpathIndex + "]");
	 }

	 /**
	   * Return a static text element that contains text *
	 */
	 public static WebElement text(String text) {
	    return element(for_text(text));
	 }

	  /**
	   * Return a static text locator that contains text *
	   */
	  public static By for_text(String text) {
	    return By.xpath("//android.widget.TextView[contains(@text, '" + text + "')]");
	  }

	  /**
	   * Return a static text element by exact text *
	   */
	  public static WebElement text_exact(String text) {
	    return element(for_text_exact(text));
	  }

	  /**
	   * Return a static text locator by exact text *
	   */
	  public static By for_text_exact(String text) {
	    return By.xpath("//android.widget.TextView[@text='" + text + "']");
	  }

	  public static By for_find(String value) {
	    return By.xpath("//*[@content-desc=\"" + value + "\" or @resource-id=\"" + value +
	        "\" or @text=\"" + value + "\"] | //*[contains(translate(@content-desc,\"" + value +
	        "\",\"" + value + "\"), \"" + value + "\") or contains(translate(@text,\"" + value +
	        "\",\"" + value + "\"), \"" + value + "\") or @resource-id=\"" + value + "\"]");
	  }

	  public static WebElement find(String value) {
	    return element(for_find(value));
	  }

	  /**
	   * Wait 30 seconds for locator to find an element *
	   */
	  public static WebElement wait(By locator) {
	    return driverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	  }

	  /**
	   * Wait 60 seconds for locator to find all elements *
	   */
	  public static List<WebElement> waitAll(By locator) {
	    return driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	  }

	  /**
	   * Wait 60 seconds for locator to not find a visible element *
	   */
	  public static boolean waitInvisible(By locator) {
	    return driverWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	  }

	
}
