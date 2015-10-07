package com.framework.helper;
import jxl.*;
import jxl.read.biff.BiffException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;


public class ExcelReaderOld {
    
    public void readAllRows(String path, String sheetname) throws BiffException, IOException {
		FileInputStream fs = new FileInputStream(path);
		Workbook wb = Workbook.getWorkbook(fs);
		Sheet sheet = wb.getSheet(sheetname);
		// To get the number of rows present in sheet
		int totalNoOfRows = sheet.getRows();
		// To get the number of columns present in sheet
		int totalNoOfCols = sheet.getColumns();
		for (int row = 0; row < 200; row++) {

			for (int col = 0; col < totalNoOfCols; col++) {
				System.out.print(sheet.getCell(col, row).getContents() + "\t");
			}
			if (sheet.getCell(0, row).getContents().length() <3){
				continue;
			}
			System.out.println();
		}
	}
    
    
    public void readAllRowsOnePoint(String path, String sheetname) throws BiffException, IOException {
 		FileInputStream fs = new FileInputStream(path);
 		Workbook wb = Workbook.getWorkbook(fs);
 		Sheet sheet = wb.getSheet(sheetname);
 		// To get the number of rows present in sheet
 		int totalNoOfRows = sheet.getRows();
 		// To get the number of columns present in sheet
 		int totalNoOfCols = sheet.getColumns();
 		HashMap<String, Float> map = new HashMap<>();
 		for (int row = 2; row < totalNoOfRows; row++) {
 			String date = sheet.getCell(0, row).getContents();
 			String psid = sheet.getCell(1, row).getContents();
 			String name = sheet.getCell(2, row).getContents();
 			String quantity = sheet.getCell(8, row).getContents();
 			
 			System.out.print(date + "\t");
 			System.out.print(psid + "\t");
 			System.out.print(name + "\t");
 			System.out.print(Float.parseFloat(quantity) + "\t");
// 			for (int col = 0; col < totalNoOfCols; col++) {
// 				System.out.print(sheet.getCell(col, row).getContents() + "\t");
// 			}
 			if (sheet.getCell(0, row).getContents().length() <3){
 				continue;
 			}
 			if (map.containsKey(psid)) {
                map.put(psid, map.get(psid) + Float.parseFloat(quantity));

            } else {
                map.put(psid,  Float.parseFloat(quantity));
            }
 			
 			
 			System.out.println();
 		}
 		
 		Set<String> keys = map.keySet();
        for (String key : keys) {
            System.out.println("PSID: " + key + "\t Hours" + map.get(key));
            
        }
 	}


 
}
