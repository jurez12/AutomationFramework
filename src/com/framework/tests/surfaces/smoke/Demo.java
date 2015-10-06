package com.framework.tests.surfaces.smoke;

import org.testng.annotations.Test;
import com.framework.tests.surfaces.SurfaceLibrary;

public class Demo extends SurfaceLibrary {

	@Test (description = "Verify Flooring Order Page")
	public void Test01_Verify_Flooring_Order() throws Exception {
		System.out.println("The user path " +  IMAGE_PATH);
		loginPage.login();
		listFloorOrderPage.verifyFloorOrders();
	}
	
	@Test (description = "Verify Flooring Page")
	public void Test02_Verify_Flooring_Page() throws Exception {
		floorPage.verifyFloor(IMAGE_PATH);
	}
}
