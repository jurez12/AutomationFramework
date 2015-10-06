package com.framework.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

import com.framework.library.HttpUtil;
import com.framework.library.utils.MiscUtils;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;



public class Library {
	static final Logger log = Logger.getLogger(Library.class);
	public static String curDirPath="";
	public static File path=new File(".");	
	public WebDriver driver;
	private final int ADMIN_CALL_RETRY_COUNT = 2;
	private final int AUTH_TOKEN_RETRY_COUNT = 2;
	private final int GET_FACEBOOK_LOGIN_URL_RETRY_COUNT = 2;
	private final int TARGETING_CALL_RETRY_COUNT = 2;
	static final int CONNECTION_TIMEOUT = 10 * 1000;
    static final int SO_TIMEOUT = 120 * 1000;
    protected String IMAGE_PATH;
    protected int HIGH_TOLERANCE = 70;
    protected int TOLERANCE = 60;
    protected int LOW_TOLERANCE = 50;
    protected int TIME_OUT = 20;
    
	public void assignBrowser() throws IOException {
		if ("firefox".equals(Global.getBrowserName())) {
			log.debug("Starting Firefox driver");
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("capability.policy.default.Window.frameElement", "allAccess");
			try {
				if(!MiscUtils.empty(System.getProperty("plugin.firebug"))) {
				log.debug("Adding Firebug (" + System.getProperty("plugin.firebug") + ")to Firefox");
				profile.addExtension(new File(".\\libs\\firebug-1.9.0.xpi"));
				
				profile.setPreference("extensions.firebug.currentVersion", "1.9.0"); 
				//profile.setPreference("extensions.firebug.currentVersion", System.getProperty("plugin.firebug"));
				}
				if(!MiscUtils.empty(System.getProperty("plugin.firepath"))) {
				log.debug("Adding Firepath (" + System.getProperty("plugin.firepath") + ")to Firefox");
				profile.addExtension(new File(".\\libs\\firepath.xpi"));
				}
				driver = new FirefoxDriver(profile);
			} catch (Exception ex) {
				profile = new FirefoxProfile();
				profile.setAcceptUntrustedCertificates(true); 
				profile.setPreference("capability.policy.default.Window.frameElement", "allAccess");
				log.error("Could not find the firefox extensions, launching Firefox without the extensions", ex);
				driver = new FirefoxDriver(profile);
			}
			driver.manage().window().maximize();
		} else if ("ie".equals(Global.getBrowserName())) {
			log.debug("Starting IE driver");
			File file = new File("libs\\IEDRiverServer.exe");
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath() );
			driver = new InternetExplorerDriver();
			driver.manage().window().maximize();
		} else if ("chrome".equals(Global.getBrowserName())) {
				log.debug("Starting Chrome driver");
				if (System.getProperty("os.name").toLowerCase().contains("windows")) {
					log.debug("Found Windows: Setting system property [webdriver.chrome.driver=libs/chromedriver.exe]");
					System.setProperty("webdriver.chrome.driver", "libs/chromedriver.exe");
				} else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
					log.debug("Found Mac: Setting system property [webdriver.chrome.driver=libs/chromedriver]");
					System.setProperty("webdriver.chrome.driver", "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
				}
				ChromeOptions options = new ChromeOptions();
				options.addArguments("test-type");
				log.debug("Setting Chrome option: [start-maximized]");
				options.addArguments("start-maximized");
				log.debug("Setting Chrome option: [disable-bundled-ppapi-flash]");
				options.addArguments("--disable-bundled-ppapi-flash");
				driver = new ChromeDriver(options);
		} else if ("safari".equals(Global.getBrowserName())) {
			log.debug("Starting Safari driver");
			driver = new SafariDriver();
		} else if ("htmlunit".equals(Global.getBrowserName())){
			log.debug("Starting HtmlUnit driver");
			driver = new HtmlUnitDriver();
		} else {
			log.error("Could not find a suitable browser");
		}
		
	}
	
	public WebDriver launchBrowser() throws IOException {
		assignBrowser();
		if(!"CHROME".equalsIgnoreCase(Global.getBrowserName()))
		maximize();
		return driver;
	}
	 
	public void maximize() {
		log.debug("Maximizing browser window to screen size");
		driver.manage().window().setPosition(new Point(0,0));
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dim = new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight());
		driver.manage().window().setSize(dim);
	}
	
	public void setRootDirectory(){
		try {
			curDirPath=path.getCanonicalPath();
		} catch (Exception e) {
			log.error("Failed to set Current Directory Path", e);
		}
	}
	
	public String getRootDir(){
		if(curDirPath==""){
			setRootDirectory();
		}
		return curDirPath;
	}
		
	public void wait(int timeout) throws InterruptedException{
		log("Wait: Sleeping for [" + timeout + "] seconds" );
		try {
			Thread.sleep(timeout*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void sleep(long ms){
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// do nothing
		}
	}
	
	/*
	 *  Logs to HTML report and Framework logs
	 */
	public static void log(String message) {
		log(message, null);
	}
	
	
	public static void log(String message, String prefix) {
		String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (System.currentTimeMillis()));
		if(prefix != null) {
			log.info(date + ": " + prefix + ":: " + message);
			Reporter.log(date + ": - " + prefix + ":: " + message);
		} else {
			log.info(date + ": " + message);
			Reporter.log(date + ": - " + message);
		}
	}
		
	/*
	 *  Logs to HTML report with screenshot.
	 *  Logs to Framework logs
	 */
	public static void logWithScreenshot(String message) {
		logWithScreenshot(message, null);
	}
	
	public static void logWithScreenshot(String message, String prefix) {
		String path = Global.getReportsDir() + File.separator + "screenshots" + File.separator;
		String fileName = System.currentTimeMillis() + ".png";
		String screenshotsPath = "./screenshots/" + fileName;
		try {
			if (MiscUtils.empty(path) || !new MiscUtils().captureScreen(path, fileName)) {
				log.error("Screenshot Capture Failed");
				Reporter.log(message);
				return;
			}
		} catch (Exception e) {
		log.error("Screenshot Capture Failed with Exception", e);
		}
		log(message + ", [<a href='" + screenshotsPath + "'>Screenshot</a>]", prefix);
	}
	/*
	 *  Kills all browser process specified in the list array
	 */
	public void killBrowser(String[] browserList) {
		for (String browser : browserList) {
			try {
				Process p = Runtime.getRuntime().exec("taskkill /F /IM " + browser);
				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					log.debug(line);
				}
			} catch (Exception e) {
				log.error("Exception in KillBrowser", e);
			}
		}
	}
	
	public void clearCacheAndDeleteCookies(){
		driver.manage().deleteAllCookies();
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		try {
			log("attempting to clear cache and cookies");
			p  = rt.exec("RunDll32.exe InetCpl.cpl,ClearMyTracksByProcess 4351");
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
			log.debug(line);
			}
		} catch (Exception e) {
			log.error("Exception in clearCacheAndDeleteCookies", e);
		}
	}
	
	
	/*
	 *  Make admin call with params
	 */
	public String makeAdminCall(Map<String, String> params) throws Exception {
		String urlParams = "?";
		int count = 0;
		HttpResponse r;
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				urlParams = urlParams + entry.getKey() + "=" + entry.getValue()	+ "&";
			}
			urlParams = urlParams.substring(0,urlParams.length()-1);
			String url = "http://transliteration.reverie.co.in/api/processString" + urlParams;
			Library.log("Requesting Admin call [" + url + "]");
			
			while(count < ADMIN_CALL_RETRY_COUNT) {
				count++;
				try {
					r = HttpUtil.get(url, CONNECTION_TIMEOUT, SO_TIMEOUT);
					if(r.getStatusLine().getStatusCode() == 200) {
						String response = HttpUtil.getResponse(r);
						Reporter.log("Response: [" + response + "]");
						return response;
					} else {
						log.debug("Admin call did not return a 200 OK, retrying (" + count + ") ...");
						Thread.sleep(3000);
					}
				} catch(Exception e) {
					log.error("Exception occured in making the admin call, retrying(" + count + ") ...", e);
					Thread.sleep(3000);
				}
			}
			return HttpUtil.getResponse(HttpUtil.get(url));			
		} catch (Exception e) {
			log.error("Admin Call Failed", e);
			throw e;
		}
	}

	
	public void makeAdminCall(String url) throws Exception {
		driver.get(url);
		//Assert.assertTrue(driver.findElement(By.xpath ("//body")).getText().length() > 10);
		
//		int count = 0;
//		HttpResponse r;
//		try {
//			
//			Library.log("Requesting Admin call [" + url + "]");
//			while(count < ADMIN_CALL_RETRY_COUNT) {
//				count++;
//				try {
//					r = HttpUtil.get(url, CONNECTION_TIMEOUT, SO_TIMEOUT);
//					if(r.getStatusLine().getStatusCode() == 200) {
//						String response = HttpUtil.getResponse(r);
//						Reporter.log("Response: [" + response + "]");
//						return response;
//					} else {
//						log.debug("Admin call did not return a 200 OK, retrying (" + count + ") ...");
//						Thread.sleep(3000);
//					}
//				} catch(Exception e) {
//					log.error("Exception occured in making the admin call, retrying(" + count + ") ...", e);
//					Thread.sleep(3000);
//				}
//			}
//			return HttpUtil.getResponse(HttpUtil.get(url));			
//		} catch (Exception e) {
//			log.error("Admin Call Failed", e);
//			throw e;
//		}
	}
}
