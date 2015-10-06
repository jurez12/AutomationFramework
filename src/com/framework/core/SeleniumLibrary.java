package com.framework.core;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.framework.library.SikuliLibrary;
import org.openqa.selenium.Alert;

public class SeleniumLibrary extends Library {
	static final Logger log = Logger.getLogger(SeleniumLibrary.class);
	
	
	
	protected SikuliLibrary sikuli = null;
	WebElement e = null;
	public static enum commandEnum{click, isDisplayed, isElementPresent, isNotDisplayed, getText, sendKeys, submit, compareText, getAttr, moveToElementAndClick,select};
	public static enum IframeEnum{defaultContent, other}
	public String returnString = null;
	
	public void dragdrop(WebElement LocatorFrom, WebElement LocatorTo) {
		String xto=Integer.toString(LocatorTo.getLocation().x);
		String yto=Integer.toString(LocatorTo.getLocation().y);
		((JavascriptExecutor)driver).executeScript("function simulate(f,c,d,e){var b,a=null;for(b in eventMatchers)if(eventMatchers[b].test(c)){a=b;break}if(!a)return!1;document.createEvent?(b=document.createEvent(a),a==\"HTMLEvents\"?b.initEvent(c,!0,!0):b.initMouseEvent(c,!0,!0,document.defaultView,0,d,e,d,e,!1,!1,!1,!1,0,null),f.dispatchEvent(b)):(a=document.createEventObject(),a.detail=0,a.screenX=d,a.screenY=e,a.clientX=d,a.clientY=e,a.ctrlKey=!1,a.altKey=!1,a.shiftKey=!1,a.metaKey=!1,a.button=1,f.fireEvent(\"on\"+c,a));return!0} var eventMatchers={HTMLEvents:/^(?:load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll)$/,MouseEvents:/^(?:click|dblclick|mouse(?:down|up|over|move|out))$/}; " +
				"simulate(arguments[0],\"mousedown\",0,0); simulate(arguments[0],\"mousemove\",arguments[2],arguments[3]); simulate(arguments[0],\"mouseup\",arguments[2],arguments[3]); ",
				LocatorFrom,"mousedown",xto,yto);
	}

	public void dragdrop(By ByFrom, By ByTo) {
		WebElement LocatorFrom = driver.findElement(ByFrom);
		WebElement LocatorTo = driver.findElement(ByTo);
		String xto=Integer.toString(LocatorTo.getLocation().x);
		String yto=Integer.toString(LocatorTo.getLocation().y);
		((JavascriptExecutor)driver).executeScript("function simulate(f,c,d,e){var b,a=null;for(b in eventMatchers)if(eventMatchers[b].test(c)){a=b;break}if(!a)return!1;document.createEvent?(b=document.createEvent(a),a==\"HTMLEvents\"?b.initEvent(c,!0,!0):b.initMouseEvent(c,!0,!0,document.defaultView,0,d,e,d,e,!1,!1,!1,!1,0,null),f.dispatchEvent(b)):(a=document.createEventObject(),a.detail=0,a.screenX=d,a.screenY=e,a.clientX=d,a.clientY=e,a.ctrlKey=!1,a.altKey=!1,a.shiftKey=!1,a.metaKey=!1,a.button=1,f.fireEvent(\"on\"+c,a));return!0} var eventMatchers={HTMLEvents:/^(?:load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll)$/,MouseEvents:/^(?:click|dblclick|mouse(?:down|up|over|move|out))$/}; " +
				"simulate(arguments[0],\"mousedown\",0,0); simulate(arguments[0],\"mousemove\",arguments[2],arguments[3]); simulate(arguments[0],\"mouseup\",arguments[2],arguments[3]); ",
				LocatorFrom,"mousedown",xto,yto);
	}
	
	public void mouseClickUsingSikuliOnFlash(int offSetX, int offSetY) throws Exception{
		By byto = By.id("flashFrame");
		SikuliLibrary sik = new SikuliLibrary();
		WebElement elementLocation = driver.findElement(byto);
		int x = elementLocation.getLocation().x;
		int y = elementLocation.getLocation().y;
		x+=offSetX;
		y+=offSetY;
		Point p=new Point(x, y);
		log("Mouse click using Sikuli on Flash: Point "+x+","+y);
		sik.mouseClick(p);
	}
	
	public void goToURL(String url){
		logWithScreenshot("Go to url [ "+url+ " ]");
		driver.get(url);
	}
	
	//click(By.name("grant_required_clicked"), 5);
	public void click(By locator, int timeout) {
		logWithScreenshot("Clicking on [" + locator.toString() + "]");
		e=waitFindElement(locator, timeout);
		AssertResult(locator);
		Assert.assertNotNull(e, "Could not find the WebElement " + locator);
		if(Global.getBrowserName().equalsIgnoreCase("firefox") || Global.getBrowserName().equalsIgnoreCase("chrome")) {
			e.click();
		} else {
			log("Can be passing IE8, IE7, INTERNET EXPLORER.etc");
			((JavascriptExecutor) driver).executeScript("arguments[0].click()", e);
		}
		log("Successfully clicked the element");
	}
	
	public boolean clickContinueOnFail(By locator, int timeout) {
		boolean clickHappen;
		AssertResult(locator);
		try{
			logWithScreenshot("Clicking on [" + locator.toString() + "]");
			e=waitFindElement(locator, timeout);
			
			log(Global.getBrowserName());
			//Assert.assertNotNull(e, "Could not find the WebElement " + locator);
			if(Global.getBrowserName().equalsIgnoreCase("firefox") || Global.getBrowserName().equalsIgnoreCase("chrome")) {
				e.click();
			} else if(Global.getBrowserName().toLowerCase().startsWith("ie")) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click()", e);
			}
			//Assert.assertTrue(e.isDisplayed(), "Element " + locator + " is not visible");
			clickHappen = true;
			log("Successfully clicked the element");
			
		}
		catch (Exception e) {
			log("Did not click the element");
			clickHappen =  false;
		}
		return clickHappen;
	}
	
	public void isDisplayed(By locator, int timeout) {
		logWithScreenshot("Checking if [" + locator.toString() + "] is displayed");
		e=waitFindElement(locator, timeout);
		AssertResult(locator);
		Assert.assertTrue(e.isDisplayed(), "Element " + locator + " is not visible");
	}
	
	private void AssertResult(By locator) {
		Global.ACTUAL_VALUE = driver.findElement(locator).getText();
		Global.XPATH = locator.toString();
	}
	
	protected void AssertResult(String expected, String actual) {
		Global.ACTUAL_VALUE = actual;
		Global.EXPECTED_VALUE = expected;
	}

	public void AssertResult(By locator, String text) {
		AssertResult(locator);
		Global.EXPECTED_VALUE = text;
	}
	
	public void isNotDisplayed(By locator, int timeout) {
		logWithScreenshot("Checking if [" + locator.toString() + "] is not displayed");
		try {
			e=waitFindElement(locator, timeout);
			Assert.assertFalse(e.isDisplayed(), "Element " + locator + " is not visible");
			AssertResult(locator);
		} catch (Exception e) {
			Assert.assertTrue(true, "Element " + locator + " is visible");
			AssertResult(locator);
		}
	}
	
	public boolean isElementVisible(By locator, int timeout){
		try {
			e=waitFindElement(locator, timeout);
			logWithScreenshot("[" + locator.toString() + "] is present");
			AssertResult(locator);
			return true;
		} catch (Exception e) {
			logWithScreenshot("[" + locator.toString() + "] is not present");
			AssertResult(locator);
			return false;
		}
	}
	
	public void sendKeys(By locator, String key, int timeout) {
		logWithScreenshot("Checking if [" + locator.toString() + "] is displayed");
		e=waitFindElement(locator, timeout);
		Assert.assertNotNull(e, "Could not find the WebElement " + locator);
		e.clear();
		e.sendKeys(key);
		AssertResult(locator);
	}
	
	public String getText(By locator, int timeout) {
		logWithScreenshot("Checking if [" + locator.toString() + "] is displayed");
		e=waitFindElement(locator, timeout);
		Assert.assertNotNull(e, "Could not find the WebElement " + locator);
		AssertResult(locator);
		return e.getText();
	}
	

	public void CloseTab() {
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_W);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyRelease(KeyEvent.VK_W);
		} catch (AWTException e) {
			System.out.println("Failed to close the tab");
		}
	}
	public void closeDriver() {
		driver.quit();
	}
	
	public void NavigateBack() {
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_LEFT);
			robot.keyRelease(KeyEvent.VK_ALT);
			robot.keyRelease(KeyEvent.VK_LEFT);
		} catch (AWTException e) {
			System.out.println("Failed to close the tab");
		}
	}
	
	public void switchThroughFrames(String framePath) {
		log("Trying to switch through frames [" + framePath +"]");
		log("Switching to default frame");
		driver.switchTo().defaultContent();
		String[] framesList = framePath.split(">");
		
		for(int i=0; i < framesList.length; i++) {
			log("Switching to frame [" + framesList[i] + "]");
			driver.switchTo().frame(framesList[i]);
		}
	}
	
	public void switchFrame(String frame) {
		log("Switching directly to frame [" + frame + "]");
		driver.switchTo().frame(frame);
	}
	
	public void switchFrame(int frameIndex) {
		log("Switching directly to frame [" + frameIndex + "]");
		driver.switchTo().frame(frameIndex);
	}
	
	public void switchFrame(WebElement element) {
		log("Switching directly to WebElement [" + element + "]");
		driver.switchTo().frame(element);
	}
	
	public void switchToDefault() {
		log("Switching to default frame");
		driver.switchTo().defaultContent();
	}

	public void switchToWindowByTitle(String title) {
		boolean found = false;
		log("Switching directly to window with title [" + title + "]");
		while(found == false) {     
			for (String handle : driver.getWindowHandles()) {
				String myTitle = driver.switchTo().window(handle).getTitle();
				if(myTitle.equalsIgnoreCase(title)){
					found = true;
					break;
				}
			}
			found = true;
		}
	}
	
	public void switchToWindow(String handle) {
		log("Switching directly to window with handle [" + handle + "]");
		driver.switchTo().window(handle);
	}
	
	public void closeTab() {
		log("Closing the browser tab"); 
		driver.close();
		
	}
	
	public String getHandleOfWindow() {
		log("Returning the handle of the current window");
		return driver.getWindowHandle();
	}
	
	public WebElement getElement(final By locator) {
		AssertResult(locator);
		return  driver.findElement(locator);
	}

	public  List<WebElement> getElements(final By locator) {
		return driver.findElements(locator);
	}

	public WebElement waitFindElement(By locatorname, int timeout) {
		log("Waiting for the element [" + locatorname.toString() + "], timeout = [" + timeout + "]");
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locatorname));
	}
	
	public List<WebElement> waitFindElements(By locatorname, int timeout) {
		log("Waiting for the element [" + locatorname.toString() + "], timeout = [" + timeout + "]");
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		AssertResult(locatorname);
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locatorname));
	}
	
	/*
	 * SeleniumCommand implementation
	 * 
	 * click, isDisplayed, isElementPresent, isNotDisplayed, getText, sendKeys, submit, compareText, getAttr, moveToElementAndClick,select
	 * e.g seleniumCommand(commandEnum.click, By.xpath("//*[@class='close_btn']")
	 *     seleniumCommand(commandEnum.click, By.id("zsc_button")
	 */
 
	public String seleniumCommand(commandEnum selCommand, By Locator){
		return seleniumCommand(selCommand, Locator, null, IframeEnum.other);
	}
	
	public String seleniumCommand(commandEnum selCommand, By Locator, String arg){
		return seleniumCommand(selCommand, Locator, arg, IframeEnum.other);
	}
	
	public String seleniumCommand(commandEnum selCommand, By Locator, IframeEnum Iframe_name){
		return seleniumCommand(selCommand, Locator, null, Iframe_name);
	}
	
	public String seleniumCommand(commandEnum selCommand, By Locator, IframeEnum Iframe_name,int timeout){
		return seleniumCommand(selCommand, Locator, null, Iframe_name,timeout);
	}
	
	public String seleniumCommand(commandEnum selCommand, By locator, String arg, IframeEnum Iframe_name, int timeout ){
		WebElement e = null;
		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
		AssertResult(locator);
		switch(Iframe_name){
			case defaultContent:
				driver.switchTo().defaultContent();
				break;
			case other:
				break;
		}
		
	
		switch(selCommand){
		case click:
			click( locator, timeout);
			log("Clicked at the locator ***| "+locator+" |***");
			break;
						
				
		case isDisplayed:
			isDisplayed(locator, timeout);
			break;		
			
			
			
		case isElementPresent:
			logWithScreenshot("Verifying element Present ***| "+locator+" |***");
		    waitFindElement(driver, locator,timeout);
		    break;
			
		case isNotDisplayed:
			isNotDisplayed(locator, timeout);
			break;	
			
		case getText:
			logWithScreenshot("Get text from the locator ***| "+locator);
			e=waitFindElement(driver, locator,timeout);
			returnString = e.getText();
			break;	
			
		case getAttr:
			logWithScreenshot("Get text from the locator ***| "+locator);
			e=waitFindElement(driver, locator,timeout);
			returnString = e.getAttribute(arg);
			break;	
			
		case sendKeys:
			log("Send text ***| "+arg+" |*** to the locator ***| "+locator);
			e=waitFindElement(driver, locator,timeout);
			e.clear();
			e.sendKeys(arg);
			break;	
			
			
		case submit:
			log("Clicking Submit using selenium |*** to the locator ***| "+locator);
			e=waitFindElement(driver, locator,timeout);
			e.submit();
			break;	
			
		case compareText:
			log("Compare text at locator ***| "+locator+" |*** with ***| "+arg+ "|*** using selenium");
			e=waitFindElement(driver, locator,timeout);
			String temp=e.getText();
			returnString = temp;
			Assert.assertTrue(temp.contains(arg), "Element text expected:"+arg+" Got: "+returnString);
			break;	
			
		case moveToElementAndClick:
			log("Move to Element using selenium and then click |*** to the locator ***| "+locator);
			e=waitFindElement(driver, locator,timeout);
			Actions action =new Actions(driver);
			action.moveToElement(e, 10,10).click().build().perform();				
			break;
		case select:
			log("Select a Elemnet from drop down box using selenium  "+locator);
			e=waitFindElement(driver, locator,timeout);
			new Select(e).selectByVisibleText(arg);
			break;

		}
			
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return returnString;
	}
	
	public void waitFor(long time) {
		try {
			Thread.sleep(time*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String seleniumCommand(commandEnum selCommand, By Locator, String arg, IframeEnum Iframe_name ){
		return seleniumCommand(selCommand, Locator, arg, Iframe_name, 20 );
	}
	
	// timeout in seconds=30
	public WebElement waitFindElement(WebDriver driver, By locatorname) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locatorname));
	}

	// timeout in seconds
	public WebElement waitFindElement(WebDriver driver, By locatorname,
			int timeout) {
		AssertResult(locatorname);
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locatorname));
	}
	

	
	// waits for an element to have a certain text timeout is 30 seconds
	public boolean waitElementHasText(WebDriver driver, By locatorname,
			String txt) {
		WebElement elm=null; 
		int count = 30; // - 30 seconds
		try {
			while (count >= 1) {
				elm=waitFindElement(driver, locatorname);
				AssertResult(locatorname, txt);
				if (elm.getText().contains(txt)) return true;
				Thread.sleep(1000);
				--count;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	//wait for an element to have an attribute contain a particular text
	public boolean waitElementHasAttribute(WebDriver driver, By locatorname, String attribute,
			String txt) {
		WebElement elm=null; 
		int count = 90; // - 30 seconds
		try {
			while (count >= 1) {
				elm=waitFindElement(driver, locatorname);
				AssertResult(locatorname, txt);
				//System.out.println("ATTRIBUTE IS _-------"+elm.getAttribute(attribute));
				if (elm.getAttribute(attribute).toLowerCase().contains(txt)) return true;
				//if (elm.getText().contains(txt)) return true;
				Thread.sleep(1000);
				--count;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	//will look for a bunch of elements and returns the first found element. timeout is in seconds
	public By waitFindElements(WebDriver driver,int timeout, By... locators) {
		int count = timeout; // - 10 seconds
		while (count >= 1) {
			for (By locator : locators) {
				try {
					driver.findElement(locator);
					AssertResult(locator);
					return locator;
				} catch (Exception e) {

				}
			}
			try { Thread.sleep(1000);}
			catch(Exception e) {}
			--count;
		}
		return null;
	}
	
	public WebElement waitFindElementsIsVisible(WebDriver driver, By locator, int timeout) {
		WebElement elm = null;
		int count = timeout; // - 10 seconds
		while (count >= 1) {
			try {
				elm = driver.findElement(locator);
				if (elm.isDisplayed()){
					AssertResult(locator);
					return elm;
				}
			} catch (Exception e) {
				AssertResult(locator);
			}
			try {
				Thread.sleep(1000);
				AssertResult(locator);
			} catch (Exception e) {
			}
			--count;
		}
		return null;
	}
	
	
	/** Method to get the value from the attribute if it has more that one set of values as a key value pair.
	 * @param attr
	 * @return
	 */
	public String getAttrValue(String attr, String val){
		StringTokenizer st = new StringTokenizer(attr, ":,"); 
		while(st.hasMoreTokens()) { 
			if(st.nextToken().contains(val)){
				return st.nextToken(); 
			}

		} 
		return null;
	}
	
	  public boolean verifyElementExists(By locator,IframeEnum iFrameLocator){
		 	
		  boolean bExists = false;
	   
		  try{
		   seleniumCommand(commandEnum.isElementPresent, locator, iFrameLocator);
//	       Assert.assertEquals(, true);
		   AssertResult(locator);
	       bExists =  true;
		  }
		  catch(Exception e){
			  AssertResult(locator);
			  bExists = false;  
		  }
	     
   
		return bExists ; 
	}
	  
	public boolean findFrameByElement(By locator, String parentFrame){
		if (isElementVisible(locator, 5)==true)
			return true; 
		switchThroughFrames(parentFrame);
		List<WebElement> iFrames = driver.findElements(By.tagName("iframe")); 
		int i=0;
		while(iFrames.size() > 0 && i<40 && i<iFrames.size())
		{
			log("Iteration "+Integer.toString(i));
			driver.switchTo().frame(iFrames.get(i));
			if (isElementVisible(locator, 5)==true) {
				AssertResult(locator);
				return true;
			}
			else
				switchThroughFrames(parentFrame);
			i++;
		}
		return false;
	}
	
	/*
     * @Author : Suresh
     * Desc: click Alert Ok Button 
    */
	public boolean clickAlertOkButton(String alertString) {
		Alert alert = driver.switchTo().alert();
		if (!alert.getText().contains(alertString)) {
			return false;
		}
		alert.accept();
		return true;
	}
	 
	public void refresh() {
		 driver.navigate().refresh();
	}
	
	public List<WebElement> getWebElements(By by){
		return driver.findElements(by);
	}
	
	public WebElement getWebElement(By by){
		return driver.findElement(by);
	}
	
	/*
     * @Author : Suresh
     * Desc: This function is used to verify whether text present in the web element.
    */
	public boolean verifyTextPresent(By by, String text) {
		WebElement webElemnent = driver.findElement(by);
		if (webElemnent == null) {
			return false;
		} else {
			if (verifyTextPresent(webElemnent, text)) {
				AssertResult(by, text);
				return true;
			}
		}
		return false;
	}
	
	public boolean verifyTextPresent(WebElement webElement, String text) {
		if (webElement == null) {
			return false;
		} else {
			if (webElement.getText().contains(text)) {
				return true;
			}
			log("String contains " + webElement.getText());
		}
		return false;
	}
	
	/*
     * @Author : Suresh
     * Desc: This function is used to check whether webElement is enable or disabled.
    */
	public boolean verifyElementIsEnabled(By by) {
		AssertResult(by);
		return  verifyElementIsEnabled(driver.findElement(by));
	}

	public boolean verifyElementIsEnabled(WebElement webElement) {
		return webElement.isEnabled();
	}
	
	public String getText(By by) throws Exception {
		try {
			return getText(driver.findElement(by));	
		} catch(Exception e){
			throw e;
		}
	}
	
	public String getText(WebElement webElement) {
		try {
			return webElement.getText();	
		} catch(Exception e){
			throw e;
		}
	}

	/*
     * @Author : Suresh
     * Desc: get the Attribute value by giving attribute(i.e Id, name etc).
    */
	public String getAttribute(By by, String attribute) throws Exception {
		try {
			AssertResult(by);
			return getAttribute(driver.findElement(by), attribute);	
	   } catch(Exception e){
		   AssertResult(by);
			throw e;
		  }
	}

	public String getAttribute(WebElement webElement, String attribute) throws Exception {
		try {
				return webElement.getAttribute(attribute);	
		   } catch(Exception e){
			throw e;
		  }
	}
	
	public String getCssValue(By by, String cssAttribute) throws Exception {
		String cssValue;
		try	{
			cssValue = getCssValue(driver.findElement(by), cssAttribute);
		} catch(Exception e) {
			throw e;
		}
		return cssValue;
	}
	
	public String getCssValue(WebElement webElement, String cssAttribute) throws Exception {
		String cssValue;
		try	{
			cssValue = webElement.getCssValue(cssAttribute);
		} catch(Exception e) {
			throw e;
		}
		return cssValue;
	}

	public void clear(By by) throws Exception {
		try {
			clear(driver.findElement(by));
	   } catch(Exception e){
			throw e;
		}
	}
	
	public void clear(WebElement webElement) throws Exception {
		try {
			webElement.clear();
	   } catch(Exception e){
			throw e;
		}
	}
	
	public void clearAndsendKeys(By by, String data) throws Exception {
		try {
			clear(by);
			sendKeys(driver.findElement(by), data);	
	    } catch(Exception e){
			throw e;
		}
	}
	
	public void sendKeys(By by, String data) throws Exception {
		try {
			sendKeys(driver.findElement(by), data);	
	    } catch(Exception e){
			throw e;
		}
	}
	
	public void sendKeys(WebElement webElement, String data) throws Exception {
		try {
			webElement.sendKeys(data);	
	    } catch(Exception e){
			throw e;
		}
	}
	
	/** 
	 * @Author: Suresh
	*   onMouseOver is used to move mouse over. 
	*/
	protected void onMouseOver(By by) {
		onMouseOver(driver.findElement(by));
	}
	
	protected void onMouseOver(WebElement webElement) {
		Actions builder = new Actions(driver);
		WebElement menuRegistrar = webElement;
		builder.moveToElement(menuRegistrar).build().perform();
	}
	
	public void executeClickJs(WebElement e, int timeout) throws InterruptedException {
		  wait(timeout);
		  ((JavascriptExecutor) driver).executeScript("arguments[0].click()", e);
	}
	
	/*
   * @Author : Suresh
   * Desc: execute JavaScript using command javaScriptFunction
  */
	protected void executeJavaScript(String javaScriptFunction) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(javaScriptFunction);
	}
	
	public void doubleClick(By by) {
		doubleClick(driver.findElement(by));
	}
	
	public void doubleClick(WebElement webElement) {
		((JavascriptExecutor)driver).executeScript("var evt = document.createEvent('MouseEvents');"
				+ "evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
				+ "arguments[0].dispatchEvent(evt);", webElement);
	}
	
	public void  waitUntilCloseWindow() {
		executeJavaScript("window.showModalDialog(\" wait for close\",\"\",\"dialogWidth:500px;dialogHeight:500px\")");
	}
	
	/** 
	 * @Author: Suresh
	*   mouseOverJavascript is used to move mouse over using javascript(without native elements).
	*/
	public void mouseOverJavascript(By by) {
		mouseOverJavascript(driver.findElement(by));
	}
	
	public void mouseOverJavascript(WebElement webElement) {
		String code = "var fireOnThis = arguments[0];"
            + "var evObj = document.createEvent('MouseEvents');"
            + "evObj.initEvent( 'mouseover', true, true );"
            + "fireOnThis.dispatchEvent(evObj);";
		((JavascriptExecutor) driver).executeScript(code, webElement);

	}
	
	protected void facebookLogin(String userName, String pwd) throws InterruptedException {
		switchToDefault();
		wait(10);
		switchToWindowByTitle("Facebook");
        seleniumCommand(commandEnum.sendKeys,By.id("email"), userName);
        seleniumCommand(commandEnum.sendKeys,By.id("pass"), pwd);
        click(By.id("u_0_1"),1);
        wait(10);
	}

}