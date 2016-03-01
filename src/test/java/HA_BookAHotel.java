package test.java;
//import genericBussinessComponents.LocalBusinessFunctions;
import static org.testng.AssertJUnit.assertEquals;
import genericGlobalComponents.LocalBusinessFunctions;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

/* This BP tests the E2E business process of booking a hotel room using AdactIn Hotel App.   
 * INPUT: DataPool 
 * 
 * OUTPUT:
 * Results.xls
 */

public class HA_BookAHotel extends LocalBusinessFunctions
{
	
	public HA_BookAHotel() throws FileNotFoundException, IOException 
	{
		super();
	}

	public void Call(WebDriver driver)
	{
			
		GG_Report("HA_BookAHotel","", false,false);
		String login,logout="";
		boolean bOpp=false,bfinal= true;			
		login=HA_LB_Login(driver,1);
		//bOpp= HA_LB_BookingDetails(driver, 1);
		//bfinal= HA_LB_PersonaDetails(driver, 1);
		logout = HA_LB_Logout(driver);	
		if(login.equalsIgnoreCase("Pass") && logout.equalsIgnoreCase("Pass"))//&& bOpp  && bfinal)
		 {
			 GG_Report("Final Test Result for HA_BookAHotel","Pass", false, false);
			 GG_Report("","", true);
		 }
		 else
		 {
			 GG_Report("Final Test Result for HA_BookAHotel","Fail", false,false);
			 GG_Report("","", true);
		 }
		assertEquals("Pass", login);
		assertEquals("Pass", logout);
		//Assert.assertTrue(bOpp);
		//Assert.assertTrue(bfinal);
		driver.quit();
		}

}