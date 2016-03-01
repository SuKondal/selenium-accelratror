package genericGlobalComponents;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import com.BusinessGlobalFunctions;
//import com.GlobalGenericFunctions;


public class LocalBusinessFunctions extends Keywords  {

	public int intTimeOutForPaging=10000;
	private int iTimeOut;
	
	///For random generated data
	protected String strGenFName;
	protected String strGenLName;
	protected String strGenEntityName;
	
	
	
	Properties serv = new Properties(); 
	///////
	public LocalBusinessFunctions() throws FileNotFoundException, IOException {
		super();
		
	}

public void TB_LB_SetNames()
{
	this.strGenFName=GG_GenerateRandomName();
	this.strGenLName=GG_GenerateRandomName();
	this.strGenEntityName=GG_GenerateRandomName()+" Ltd";
}
	/**********************************************************************************************************
	'  Function Name:	TB_LB_Login 
	'  Purpose: 		This function will login to the Application 
	'  Inputs Parameters: row of data table
	'  Returns: 		String
	'  Author       : Skaur
/**********************************************************************************************************/

	public String HA_LB_Login(WebDriver driver,int intRowid)
	{
		
		super.driver= driver;
		String strFile=GG_SetInputFile("HA_DFL_Login.xls");
		String strResult = "";
		String strTitle="";
		String strLogInfo="Login";
		GG_Log(strLogInfo);
		GG_Report(strLogInfo, "", true, false);
		try
			{
			String usName = GG_ReadXL(intRowid,"Ed_LG_Username",strFile);
			GG_Input(driver,By.xpath(prop.getProperty("Ed_LG_Username")),"Usr Name", GG_ReadXL(intRowid,"Ed_LG_Username",strFile));	
			GG_Input(driver,By.xpath(prop.getProperty("Ed_LG_Password")),"Password", GG_ReadXL(intRowid,"Ed_LG_Password",strFile));		
			GG_Click(driver,By.xpath(prop.getProperty("Btn_LG_Login")),"Login");
			GG_Wait(2000);
			strTitle=driver.getTitle();
			if(strTitle.equalsIgnoreCase("AdactIn.com - Search Hotel"))
				{
					strResult="Pass";
					strLogInfo = usName+ ": Logged in successfully";
				}
			else if(GG_DoesWebElementExist(driver,By.xpath(prop.getProperty("Txt_LG_LoginError")),intTimeOutForPaging))
				{
					strResult="Fail";
					strLogInfo ="Invalid Login Details"; 
							
				}
			else 
			   {
				strResult="Fail";
				strLogInfo ="Cannot Login to the Hotel App"; 
			   }
				
				
			GG_Log(strLogInfo);
			GG_Report(strLogInfo, strResult, true);
			
			}
			catch(Exception ex)
			{
	            GG_Log("Got an exception! "+ ex.getMessage().toString());
	            
			}
			return strResult; 	
	}
	/**********************************************************************************************************
	'  Function Name:	TB_LB_Logout 
	'  Purpose: 		This function will logout to the Application
	'  Returns: 		String
	'  Author       : Skaur
/**********************************************************************************************************/
	public String HA_LB_Logout(WebDriver driver)
	{
		String strResult = "";
		String strLogInfo="";
		try{
		//	driver.findElement(By.xpath(prop.getProperty("Btn_Logout"))).click();
			driver.findElement(By.linkText("Logout")).click();
			
			if(driver.getTitle().equalsIgnoreCase("AdactIn.com - Logout"))
			strResult="Pass";
			else
			strResult="Fail";
		
			Thread.sleep(5000);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			strResult="Fail";
			
		}
		strLogInfo ="Logged out";	
		GG_Log(strLogInfo);
		GG_Report(strLogInfo, strResult, true);
		return strResult;
		
	}
	
	/**********************************************************************************************************************************************************
	* @doc For filling booking details
	* @param :Row number of data table
	* @return boolean
	* @author Skaur
	*
	********************************************************************************************************************************************************/                
public boolean HA_LB_BookingDetails(WebDriver driver, int intRowid)
{
		//String strFile=GG_SetInputFile("");		
		GG_SelectDDListItem(driver,prop.getProperty("Dlst_SH_location"), "Location","Sydney");
		GG_SelectDDListItem(driver,prop.getProperty("Dlst_SH_hotels"), "Hotel","Hotel Creek");
		GG_SelectDDListItem(driver,prop.getProperty("Dlst_SH_room_type"), "Room Type","Deluxe");
		GG_SelectDDListItem(driver,prop.getProperty("Dlst_SH_room_nos"), "Room No","3 - Three");
		//GG_Input(driver,By.xpath(prop.getProperty("Dlst_SH_datepick_in")),"Check In", "11/02/2013");
		//GG_Input(driver,By.xpath(prop.getProperty("Dlst_SH_datepick_out")),"Check Out","" );
	    GG_Click(driver,By.id("Submit"),"Submit");
	    if(driver.getTitle().equals("AdactIn.com - Select Hotel"));
	    {
	    	GG_Report("Open Select a Hotel Page","Pass", true);
	    	GG_Click(driver,By.id("radiobutton_0"),"Confirm");
	    	GG_Click(driver,By.id("continue"),"Continue");
	    	if(driver.getTitle().equals("AdactIn.com - Book A Hotel"))
	    	{
	    		GG_Report("Open Book A Hotel Page","Pass", true);
	    		return true;
	    	}
	    		
	    }
	    return false;
		
							
	}
/**********************************************************************************************************
@author Skaur
@doc   for providing personal details and booking to get Order No
@return boolean 
@param  data row number
@Date 13th Feb, 2012
***********************************************************************************************************/	
	
public boolean HA_LB_PersonaDetails(WebDriver driver, int intRowId)
{
		
		//String strFile=GG_SetInputFile("TB_DFL_CustomerDetails.xls");
		GG_Report("Open Book A Hotel Page","Pass", true);
		GG_Input(driver,By.xpath(prop.getProperty("Ed_BH_first_name")),"First Name", "Martha");
	    GG_Input(driver,By.id("last_name"),"Last Name", "Philip");
	    GG_Input(driver,By.id("address"),"Address","47 Pitt Street, Newtown NSW, 2056");
	    GG_Input(driver,By.id("cc_num"),"Card Number", "7878787878787878");
	    GG_SelectDDListItem(driver,prop.getProperty("Dlst_BH_cc_type"), "Card Type","VISA");
	    GG_SelectDDListItem(driver,prop.getProperty("Dlst_BH_cc_exp_month"), "Card Exp Month","May");
	    GG_SelectDDListItem(driver,prop.getProperty("Dlst_BH_cc_exp_year"), "Card Exp Year","2014");
	    GG_Input(driver,By.id("cc_cvv"),"CVV", "5006");
	    GG_Click(driver,By.id("book_now"),"Book Now");
	    GG_Wait(10000);
	    //GG_WaitAnElementDisappears(driver, By.xpath(prop.getProperty("Lb_BH_Msg")), 12000);
	    //GG_WaitAnElementPresent(driver, By.xpath(prop.getProperty("Ed_CP_orderNo")), 12000);
	    if(driver.getTitle().equals("AdactIn.com - Hotel Booking Confirmation"))
	    {
	    	System.out.println("IN");
	    	WebElement we = GG_FindElement(driver, By.xpath(prop.getProperty("Ed_CP_orderNo")), 10000);
	    	String strNo = we.getAttribute("value");
	    	GG_Report("Confirmation recieved","Pass", true);
	    	GG_Report("Confirmation No: "+strNo,"Pass", true);
	    	 return true;
	    }
	    else 
	    {
	    	GG_Report("Confirmation recieved","Fail", true);
	    	 return false;
	    }
	   
		
	}


}

















