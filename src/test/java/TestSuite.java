package test.java;

import genericGlobalComponents.LocalBusinessFunctions;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;


public class TestSuite extends LocalBusinessFunctions 
{
	//BusinessGlobalFunctions businessF;
	HA_BookAHotel bp1;
	HA_CancelBooking bp2;
	HA_TestUserAuthentication bp3;
	
	public TestSuite() throws FileNotFoundException, IOException  {
		super();
		bp1 = new HA_BookAHotel();
		bp2 = new HA_CancelBooking();
		bp3 = new HA_TestUserAuthentication();
		}
	
	@BeforeClass 
	public void launchDriver() throws FileNotFoundException, IOException
	{
		
		
	}
	@Test 
		public void HA_BookAHotel() 
		{
			 if(GG_VerifyExecution("HA_BookAHotel"))
			 {
				 driver=GG_LaunchApplication();	
				 bp1.Call(driver);
			 } 
			 else	 throw new SkipException("Skipping Test: HA_BookAHotel" );
		}
	
	 
/*	@Test 
	public void HA_CancelBooking() 
	{
		 if(GG_VerifyExecution("HA_CancelBooking"))
		 {
			 driver=GG_LaunchApplication();	
			 bp2.Call(driver);
		 }
		 else	 throw new SkipException("Skipping Test: HA_CancelBooking" );
	}

	@Test 
	public void HA_TestUserAuthentication() 
	{
		 if(GG_VerifyExecution("HA_TestUserAuthentication"))
		 {
			 driver=GG_LaunchApplication();	
			 bp3.Call(driver);
	 } 
		 else	 throw new SkipException("Skipping Test: HA_TestUserAuthentication" );
	}
*/
  @AfterTest
  public void afterMethod() {
	if(!driver.equals(null)) driver.quit();
	  
  }

  @AfterClass
  public void afterClass() {
	  
	  //driver.quit();
  }

}
