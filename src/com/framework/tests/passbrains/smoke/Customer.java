package com.framework.tests.passbrains.smoke;

import org.testng.annotations.Test;
import com.framework.tests.passbrains.PassBrainsLibrary;

public class Customer extends PassBrainsLibrary {

	@Test (description = "Testing Customer Register")
	public void Customer_Test01_CustomerRegistration() throws Exception { 
		deleteAllActivationEmail();
		passAdminCallCustomer();
		customerRegister.enterProfilePageDetails();
		homePage.clickOnRegisterNow();
		homePage.validatingConfirmation();
		activateThroughEmail();
		homePage.signOut();
	}
		
	
//
//	@Test (description = "Testing Customer Register with 2 page")
//	public void Customer_Test02_CustomerWith2Page() throws Exception { 
//		deleteAllActivationEmail();
//		passAdminCallCustomer();
//		enterCustomerWith2Pages();
//		activateThroughEmail();
//		homePage.signOut();
//	
//	}
//		
//	@Test (description = "Testing Customer with Tester Register")
//	public void Customer_Test03_CustomerTesterRegistration() throws Exception { 
//	   deleteAllActivationEmail();
//	   passAdminCallCustomer();
//	   customerRegister.clickCustomerTester();
//	   customerRegister.enetrCustomerTesterDetails();
//	   homePage.clickOnRegisterNow();
//       homePage.validatingConfirmation();
//       activateThroughEmail();
//       homePage.signOut();
//	}
//		
//	@Test (description = "Testing Customer with Tester Register 2 pages")
//	public void Customer_Test04_CustomerTesterRegistration2Page() throws Exception { 
//	   deleteAllActivationEmail();
//	   passAdminCallCustomer();
//	   enterCustomerTesterNext();
//       activateThroughEmail();
//       homePage.signOut();
//	}
//	
//	@Test (description = "Testing Customer Register-Linked in")
//	public void Customer_Test05_CustomerRegistrationWithLinkedIn() throws Exception { 
//	   passAdminCallCustomer();
//	   homePage.signINLinkedIN();
//	   customerRegister.enterProfilePageDetails();
//	   homePage.clickOnRegisterNow();
//       homePage.validatingConfirmation();
//       homePage.signOut();
//	}
//		
//	@Test (description = "Customer with Tester Register Next-Linked IN")
//	public void Customer_Test06_CustomerTesterRegistrationLinkedIn() throws Exception { 
//	   passAdminCallCustomer();
//	   homePage.signINLinkedIN();
//	   enterCustomerTesterNext();
//       homePage.signOut();
//	}
//		
//	@Test (description = "Customer 2 Pages- Linked IN")
//	public void Customer_Test07_CustomerWith2PageLinkedIN() throws Exception { 
//	   passAdminCallCustomer();
//	   homePage.signINLinkedIN();
//	   enterCustomerWith2Pages();
//	   homePage.signOut();
//	}
//
//	
//	@Test (description = "Customer tester Register with 2 Pages-Linked in")
//	public void Customer_Test08_CustomerTesterRegistration2PageLinkedIN() throws Exception { 
//	   passAdminCallCustomer();
//	   homePage.signINLinkedIN();
//	   enterCustomerTesterNext();
//       homePage.signOut();
//	}
//	
//	@Test (description = "Editing Customer Register for 1Page Registration")
//	public void Customer_Test09_EditCustomerRegistration() throws Exception { 
//		deleteAllActivationEmail();
//		passAdminCallCustomer();
//		customerRegister.enterProfilePageDetails();
//		homePage.clickOnRegisterNow();
//		homePage.validatingConfirmation();
//		activateThroughEmail();
//		homePage.clickUpdateProfile();
//		editProfile.clickOnEditProfile();
//		editProfile.editProfileOfCurrentUser();
//		editProfile.saveEditProfile();
//		editProfile.verifyEditedField();
//		homePage.signOut();
//	}
//	
//	private void enterCustomerWith2Pages() throws Exception, InterruptedException {
//		   customerRegister.enterProfilePageDetails();
//		   selectProjectWithDateAndDetailsRegister();
//	}
//
//	private void enterCustomerTesterNext() throws Exception, InterruptedException {
//	   customerRegister.clickCustomerTester();
//	   customerRegister.enetrCustomerTesterDetails();
//	   selectProjectWithDateAndDetailsRegister();
//	}
//	
//	private void selectProjectWithDateAndDetailsRegister()
//			throws InterruptedException, Exception {
//	   homePage.clickOnNext();
//	   customerRegister.selectProjectStartdate();
//	   customerRegister.selectProjectDeadlinedate();
//	   customerRegister.enterTheBudget();
//	   customerRegister.enterDetailsOfProject();
//	   homePage.clickOnRegisterNow();
//	   homePage.validatingConfirmation();
//	}

		
}