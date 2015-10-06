package com.framework.tests.misc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class Test99  {
	static int rownum = 0;
	static HSSFWorkbook workbook = new HSSFWorkbook();
	static HSSFSheet sheet = workbook.createSheet("test99");
	static Row row;
	static Cell cell;
	final static int count = 9616; 
	private static String test99FileName = "test99.xls";
	static File f = new File(test99FileName);
	static WebDriver driver;
	
	public static void main(String args[]) throws Exception {
		row = sheet.createRow(rownum);
		cell = row.createCell(0);
		cell.setCellValue("URL");
		cell = row.createCell(1);
		cell.setCellValue("Name");
		cell = row.createCell(2);
		cell.setCellValue("City");
		cell = row.createCell(3);
		cell.setCellValue("Country");
		cell = row.createCell(4);
		cell.setCellValue("About Section");
		cell = row.createCell(5);
		cell.setCellValue("Bug Logged");
		rownum++;
	    FirefoxProfile fp = new FirefoxProfile();
	    fp.setPreference("webdriver.load.strategy", "unstable"); // As of 2.19. from 2.9 - 2.18 use 'fast'
	    driver = new FirefoxDriver(fp);

		for (int i = 1; i < count; i++) {
			String url = "http://99tests.com/testers/" + i;
			rownum = i +3;
			try {
			driver.get(url);
			String name = getString(By.xpath("//h4"));
			log("Name:" +name);
			String aboutSection = getString(By.xpath("//div[contains(@class, 'pad')]/p"));
			String profileView = getString (By.xpath("//div[@class='div']//*[contains(.,'From')]"));
			if (profileView.equals("")) {
				profileView = "From:,";
			}
			
			log("Log city country " +profileView);
			String cityAndCountry[] = profileView.substring(profileView.indexOf("From:"), profileView.length()).split(",");
			String city, country;
			try {
				log("City:" + cityAndCountry[0]);
				city = cityAndCountry[0].replace("From:", "");
			} catch (Exception e) {
				city = "";
				
			}
			try {
				log("Country:" +cityAndCountry[1]);
				country = cityAndCountry[1].trim().substring(0, cityAndCountry[1].indexOf("Profile viewed")-1);
			} catch (Exception e) {
				country ="";
			}
			String bugLogged = getString(
					By.xpath("//ul[contains(@class,'contribution ')]/li[1]/strong"));
			excelwrite(url, name, city, country, aboutSection, bugLogged);
			} catch (Exception e) {
				System.out.println(url + "Error " + e);
			}
		}
	}  
		
	private static String getString(By by) {
		String value = "";
		try {
			if (driver.findElement(by).isDisplayed()) {
				value = driver.findElement(by).getText();
			} else {
				value = "";
			} 
		} catch(Exception e) {
			log("No aboutpage ");
			value = "";
		}
		log("String " + value);
		return value;
	}
	
	private static void log(String log) {
		System.out.println("Log "+ log);
	}

	public static void excelwrite(String url, String name,String city, String country, String aboutPage, String bugLogged) throws Exception {
		log("Inserting " + url);
		row = sheet.createRow(rownum);
		cell = row.createCell(0);
		cell.setCellValue( "" + url);
		cell = row.createCell(1);
		cell.setCellValue(name);
		cell = row.createCell(2);
		cell.setCellValue(city);
		cell = row.createCell(3);
		cell.setCellValue(country);
		cell = row.createCell(4);
		cell.setCellValue(aboutPage);
		cell = row.createCell(5);
		cell.setCellValue(bugLogged);
		rownum++;
		try {
			FileOutputStream out = new FileOutputStream(f);
			workbook.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
