package test.java;


import java.io.FileNotFoundException;
import java.io.IOException;
import org.openqa.selenium.WebDriver;

/* This test scenario is to authenticate users for all given data in table.   
 * INPUT: DataPool 
 * 
 * OUTPUT:
 * Results.xls
 */
public class HA_TestUserAuthentication extends genericGlobalComponents.LocalBusinessFunctions{
	public HA_TestUserAuthentication() throws FileNotFoundException,IOException 
	{
		super();
	}

	public void Call(WebDriver driver)
	{
			
		GG_Report("HA_TestUserAuthentication","", false);
		String login,logout="";	
		for(int i=1; i<=10; i++)
		{
			login=HA_LB_Login(driver,i);
			if(login.equalsIgnoreCase("Pass"))
					logout = HA_LB_Logout(driver);	
			if(login.equalsIgnoreCase("Pass") && logout.equalsIgnoreCase("Pass"))
			 {
				 GG_Report("Test Result for HA_TestUserAuthentication for data row: "+ i,"Pass", true);
			 }
			 else
			 {
				 GG_Report("Test Result for HA_TestUserAuthentication for data row: "+ i,"Fail", true);
				 driver.quit();
				 driver = GG_LaunchApplication();
			 }
		}
		GG_Report("End Test HA_TestUserAuthentication","Pass", false);
		GG_Report("","", true);
		//driver.quit();
		}

}
