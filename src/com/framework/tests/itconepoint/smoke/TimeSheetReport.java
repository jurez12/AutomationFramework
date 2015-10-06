package com.framework.tests.itconepoint.smoke;

import org.testng.annotations.Test;

import com.framework.tests.itconepoint.OnePointLibrary;

public class TimeSheetReport extends OnePointLibrary {

	@Test (description = "One Point Time Sheet Report Generation")
	public void Test_01_OnePointReportGeneration() throws Exception { 
		onePointLogin.login();
	}
}
