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

public class UTest {
	static int rownum = 0;
	static HSSFWorkbook workbook = new HSSFWorkbook();
	static HSSFSheet sheet = workbook.createSheet("utest");
	static Row row;
	static Cell cell;
	final static int count = 200000; 
	private static String test99FileName = "utest.xls";
	static File f = new File(test99FileName);
	static WebDriver driver;
	
	public static void main(String args[]) throws Exception {
		row = sheet.createRow(rownum);
		cell = row.createCell(0);
		cell.setCellValue("URL");
		cell = row.createCell(1);
		cell.setCellValue("First Name");
		cell = row.createCell(2);
		cell.setCellValue("Last Name");
		cell = row.createCell(3);
		cell.setCellValue("Gender");
		cell = row.createCell(4);
		cell.setCellValue("Join Date");
		cell = row.createCell(5);
		cell.setCellValue("Website");
		cell = row.createCell(6);
		cell.setCellValue("Country");
		cell = row.createCell(7);
		cell.setCellValue("State");
		cell = row.createCell(8);
		cell.setCellValue("Postal Code");
		cell = row.createCell(9);
		cell.setCellValue("About Me");
		rownum++;
	    FirefoxProfile fp = new FirefoxProfile();
	    fp.setPreference("webdriver.load.strategy", "unstable"); // As of 2.19. from 2.9 - 2.18 use 'fast'
	    driver = new FirefoxDriver(fp);
		for (int i = 1; i < count; i++) {
			String url = "https://profile.utest.com/" + i;
			rownum = i +1;
			try {
				String firstName = "";
				String lastName = "";
				String gender = "";
				String joinedDate = "";
				String website = "";
				String country = "";
				String state = "";
				String postalCode = "";
				String aboutMe = "";
				
				driver.get(url);
				if(!getString(By.xpath("//h1")).contains("Whoops, looks like something went wrong")) {
					firstName = getString(By.xpath("//div[@class='cf']/div[1]/table[@class='profileDisplayBasicInfo']//tr[1]/td"));
					lastName = getString(By.xpath("//div[@class='cf']/div[1]/table[@class='profileDisplayBasicInfo']//tr[2]/td"));
					gender = getString(By.xpath("//div[@class='cf']/div[1]/table[@class='profileDisplayBasicInfo']//tr[3]/td"));
					joinedDate = getString(By.xpath("//div[@class='cf']/div[1]/table[@class='profileDisplayBasicInfo']//tr[4]/td"));
					website = getString(By.xpath("//div[contains(@class,'profileBox')]/div[2]/table[@class='profileDisplayBasicInfo']//td"));
					country = getString(By.xpath("//div[contains(@class,'profileBox')]/div[@class='cf']/div[2]/table[@class='profileDisplayBasicInfo']//tr[1]/td"));
					state = getString(By.xpath("//div[contains(@class,'profileBox')]/div[@class='cf']/div[2]/table[@class='profileDisplayBasicInfo']//tr[2]/td"));
					postalCode = getString(By.xpath("//div[contains(@class,'profileBox')]/div[@class='cf']/div[2]/table[@class='profileDisplayBasicInfo']//tr[3]/td"));
					aboutMe = getString(By.xpath("//div[contains(@class,'c5')]/div[2]/p"));
				}
				excelwrite(url, firstName, lastName, gender, joinedDate, website, country, state, postalCode,  aboutMe);
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
			log("No aboutpage " + by.toString());
			value = "";
		}
		log("String " + value);
		return value;
	}
	
	private static void log(String log) {
		System.out.println("Log "+ log);
	}

	public static void excelwrite(String url, String firstName, String lastName,
			String gender, String joinedDate, String website, String country, String state, String postalCode, String  aboutMe) throws Exception {
		log("Inserting " + url);
		row = sheet.createRow(rownum);
		cell = row.createCell(0);
		cell.setCellValue( "" + (rownum-1));
		cell = row.createCell(1);
		cell.setCellValue(firstName);
		cell = row.createCell(2);
		cell.setCellValue(lastName);
		cell = row.createCell(3);
		cell.setCellValue(gender);
		cell = row.createCell(4);
		cell.setCellValue(joinedDate);
		cell = row.createCell(5);
		cell.setCellValue(website);
		cell = row.createCell(6);
		cell.setCellValue(country);
		cell = row.createCell(7);
		cell.setCellValue(state);
		cell = row.createCell(8);
		cell.setCellValue(postalCode);
		cell = row.createCell(9);
		cell.setCellValue(aboutMe);
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
