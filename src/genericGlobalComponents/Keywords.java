package genericGlobalComponents;

/*import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;*/


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;




import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;



public class Keywords extends IOOpperations{
	
	public static String strBrowser="";
	public static String strUrl="";
	public WebDriver driver;
	protected Properties prop;
	public int intTimeOut = 10000;    
	public Keywords() throws FileNotFoundException, IOException{
		prop=GG_LoadPropertiesfiles();
	}
/**********************************************************************************************************
	'  Function Name:	GG_LoadPropertiesfiles 
	'  Purpose		: 		This function will load the properties file 
	'  Parameters	:
	'  Returns		: 		Property value
	'  Author       : 	Ruchi
	'  Creation Date: 	02/08/2012
/**********************************************************************************************************/

	
	public Properties GG_LoadPropertiesfiles() throws FileNotFoundException, IOException{
		String strDM="";
		try{
			URL resource = this.getClass().getResource("/Configuration.properties");
    		File file = new File(resource.toURI());
    		//URL resource2 = this.getClass().getResource("/Xpath.properties");
    		//File file2 = new File(resource2.toURI());
			prop = new Properties();
			//prop.load(new FileInputStream(file2));
			prop.load(new FileInputStream(file));		
			strDM = prop.getProperty("Environment");
			return prop;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			GG_Log("Got an exception: Could not load Properties"+ e.getMessage().toString());

		}
		GG_Log("Could not load Properties file!");
		return null;
		
	}

	
/**********************************************************************************************************
	'  Function Name:	GGF_LaunchApplication 
	'  Purpose: 		This function will launch the application with the given browser in configuration.properties
	'  Inputs Parameters:
	'  Returns: 
	'  Author       : Ruchi
	'  Creation Date: 02/08/2012
/
  **********************************************************************************************************/

	
	public WebDriver GG_LaunchApplication()  {
	
		
		
		strBrowser = prop.getProperty("strBrowser");
		strUrl=prop.getProperty("strUrl");
	
		
		if (strBrowser != null && strBrowser.equalsIgnoreCase("firefox")) 
		{	
			
			FirefoxProfile fp = new FirefoxProfile();
			fp.setEnableNativeEvents(false);
			driver = new FirefoxDriver(fp);
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			
		}
		else if (strBrowser != null && strBrowser.equalsIgnoreCase("iexplore")) 
		{
			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			driver= new InternetExplorerDriver(ieCapabilities);
			//driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

		}

			
		else if (strBrowser != null && strBrowser.equalsIgnoreCase("chrome")) 
		{
			System.setProperty("webdriver.chrome.driver", "lib\\chromedriver.exe");
			driver = new ChromeDriver();
		}
		driver.get(strUrl);
		driver.manage().window().maximize();		
		return driver;

	}
	/**********************************************************************************************************
	'  Function Name:	waitAnElementPresent 
	'  Purpose: 		This function will wait an object present until timeup. 
	'  Inputs Parameters:	WebDriver webDriver, By by
				webDriver:	WebDriver to run the test
				by:	By.xpath(oObjectXpath)
	'  Returns: 	
	'  Creation Date: 04/05/2012
	/**********************************************************************************************************/
	
/*	public void GG_WaitAnElementPresent(WebDriver webDriver, By by, int iTimeOut)
	{
		int iTotal = 0;
		int iSleepTime = 5000;
		while(iTotal < iTimeOut)
		{
			List<WebElement> oWebElements = webDriver.findElements(by);
			if(oWebElements.size()>0)
				return;
			else
			{
				try
				{
					Thread.sleep(iSleepTime);
					iTotal = iTotal + iSleepTime;
					//System.out.println(String.format("Waited for %d milliseconds.[%s]", iTotal, by));          
				}
				catch(InterruptedException ex) 
				{
					throw new RuntimeException(ex);
				}
			}
		}
	}*/
	



	/**********************************************************************************************************
	 ' @ update 
	  	GG_WaitAndgetValuesOfDropdownlist 
	 ' Purpose:   This function will get and return list values of dropdown list and wait if the value is not loaded. 
	 @ updated skaur
	 	 /**********************************************************************************************************/
	public String[] GG_WaitAndGetValuesOfDropdownlist(WebElement webElement)
	{
		String []arrValue = null;
		String []arrAcutalValue = null;
		try
		{   
			
			
			int iSleepTime = 5000;
			for(int i = 0; i < 50000; i += iSleepTime)
			{
				List <WebElement> lstOptions = webElement.findElements(By.tagName("option"));
				int intCount = lstOptions.size();
				if(lstOptions.size()>0)
				{
					int k = 0;
					arrValue = new String[intCount];	
					for (WebElement weOption : lstOptions)
					{
						String strListValue = weOption.getText();
						arrValue[k] = strListValue;
						k++;
					}
					if (arrValue[0].contains("Select"))
					{
						arrAcutalValue = new String[arrValue.length - 1];
						for(int j = 0; j < arrAcutalValue.length; j++)
						{
							arrAcutalValue[j] = arrValue[j+1];     
						}
					}
					else
					{
						arrAcutalValue = arrValue;
					}  
				}
				else
				{
						Thread.sleep(iSleepTime);
						System.out.println("DROP DOWN LIST NOT UPDATED! ");
						System.out.println(String.format("Waited for %d milliseconds.[%s]", i + iSleepTime, webElement.getText()));          
					
				}
			}
		}
		catch(Exception ex)
		{   
			System.err.println("Got an exception! ");
			ex.printStackTrace(); 
			GG_Log(ex.getMessage().toString());
		}  
		return arrAcutalValue;
	}
	
	/**********************************************************************************************************
	 '  Function Name: getValuesOfDropdownlist 
	 '  Purpose:   This function will get and return list values of dropdown list. 
	 '  Inputs Parameters: WebElement webElement
	    	webElement: Dropdown list object we want to get values.    
	 '  Returns:  []arrValue - array value of dropdown lsit.
	 '  Author       : Thuan Nguyen
	 '  Creation Date: 05/07/2012
	 /**********************************************************************************************************/
	public String[] GG_GetValuesOfDropdownlist(WebElement webElement)
	{
		String []arrValue = null;
		String []arrAcutalValue = null;
		try
		{   
			List <WebElement> lstOptions = webElement.findElements(By.tagName("option"));
			int intCount = lstOptions.size();
			arrValue = new String[intCount];
			int i = 0;
			for (WebElement weOption : lstOptions)
			{
				String strListValue = weOption.getText();
				arrValue[i] = strListValue;
				i++;
			}   
			if (arrValue[0].contains("Select"))
			{
				arrAcutalValue = new String[arrValue.length - 1];
				for(int j = 0; j < arrAcutalValue.length; j++)
				{
					arrAcutalValue[j] = arrValue[j+1];     
				}
			}
			else
			{
				arrAcutalValue = arrValue;
			}  
		}
		catch(Exception ex)
		{   
			System.err.println("Got an exception! ");
			ex.printStackTrace(); 
			GG_Log(ex.getMessage().toString());
		}  
		return arrAcutalValue;
	}
	
	/********************************************************************************************************** 
	   Function used to get the selected option of drop down list.
	  @category	GG_getSelectedOption 
	 @return String
	 @author Skaur
	 	 /**********************************************************************************************************/		
	public String GG_getSelectedOption(WebElement webElement)
	{
		//String []arrValue = null;
		String strSelectedValue = null;
		try
		{   
			List <WebElement> Ooptions = webElement.findElements(By.tagName("option"));
			//int intCount = Ooptions.size();
			//arrValue = new String[intCount];
			
			for (WebElement weOption : Ooptions)
			{
				
				if(weOption.isSelected())
				{
					strSelectedValue =weOption.getText();
					return strSelectedValue;
				}	
			}   
			
		}
		catch(Exception ex)
		{   
			System.err.println("Got an exception! ");
			ex.printStackTrace(); 
			GG_Log(ex.getMessage().toString());
		}  
		return strSelectedValue;
	}
	
	
	
	
	
/*	public static void main(String  str[]) throws FileNotFoundException, IOException
	{
		String strLastNameDisplayed= "welcome";
		String strName =strLastNameDisplayed;
				// "\""+strLastNameDisplayed+ "\"";
		System.out.println(strName);
	}*/

	boolean clickAndWait(WebDriver webDriver, String oObjectXpath, String strObjectName, String strData)
	{
		String strStepDetails;
		boolean bResult = false;
		try
		{
			WebElement oClick = GG_FindElement(webDriver, By.xpath(oObjectXpath), intTimeOut);
			//Click on Object
			oClick.click();
			strStepDetails = "Click on " + strObjectName + "and Wait for " + strData;
			System.out.println(strStepDetails);
			GG_Log(strStepDetails);
			bResult = true;
			Long longData = new Long(strData);
			Thread.sleep(longData);
		}
		catch(Exception ex)
		{
			System.err.println("Got an exception! ");
			ex.printStackTrace();
		//	Log.info(ex.getStackTrace().toString());
		}
		return bResult;
	}

	/**********************************************************************************************************
	'  Function Name:	GG_SelectDDListItem 
	'  Purpose: 		This function will select a value in combox object. 
	'  Inputs Parameters: (WebDriver webDriver, String oObjectXpath, String strObjectName, String strData)
			webDriver:	WebDriver to run the test
			oObjectXpath:	The xpath of specific object
			strObjectName:	Name of object in testcase file
			strData:		Data to fill in Object
	'  Returns: true or false
				true if PASS
				false if FAIL
	'   Author       : Thuan Nguyen
	'   Creation Date: 09/05/2012
	*/
	public boolean GG_SelectDDListItem(WebDriver webDriver, String oObjectXpath, String strObjectName, String strData)
	{
		String strStepDetails="";
		String strStatus="";
		boolean bResult = false;
		try
		{
			WebElement weDropDownList = GG_FindElement(webDriver, By.xpath(oObjectXpath), intTimeOut);
			//Select data for Object
			
			List <WebElement> lstOptions = weDropDownList.findElements(By.tagName("option"));
			for (WebElement weOption : lstOptions)
			{
				if(weOption.getText().equalsIgnoreCase(strData.trim()))
				{
					
					weOption.click();
					bResult = true;
					strStepDetails = "Selected " + strObjectName + ": " + strData;
					strStatus="Pass";
					break;
				}
				else
				{
					strStepDetails = "Not selected " + strObjectName + ": " + strData;
					 strStatus="Fail";
				}
			}
			//	strStepDetails = "Select value of Web list " + strObjectName + ": " + strData;
			GG_Report(strStepDetails, strStatus, true);
			//System.out.println(strStepDetails);
			GG_Log(strStepDetails);
		}
		catch(Exception ex)
		{
			System.err.println("Got an exception! ");
			ex.printStackTrace();
			GG_Log(ex.getMessage().toString());
		}
		return bResult;
	}

	/**********************************************************************************************************
	@author Skaur
	@doc  WaitSelectDDListItem is used for dependent dropdown list option selects
	@return boolean : true when given option gets selected in the drop down
	@param 
	@Date 16Aug,2012
	*/	
		
	public boolean GG_WaitSelectDDListItem(WebDriver webDriver, String oObjectXpath, String strObjectName, String strData)
	{
		String strStepDetails="";
		boolean bResult = false;
		try
		{
			WebElement weDropDownList = GG_FindElement(webDriver, By.xpath(oObjectXpath),intTimeOut);
			//Select data for Object
			//List <WebElement> lstOptions = weDropDownList.findElements(By.tagName("option"));
			
			int iSleepTime = 5000;
			for(int i = 0; i < 50000; i += iSleepTime)
			{
				List <WebElement> lstOptions = weDropDownList.findElements(By.tagName("option"));
				if(lstOptions.size()>1)
				{
					for (WebElement weOption : lstOptions)
					{
						if(weOption.getText().equalsIgnoreCase(strData))
						{
							weOption.click();
							bResult = true;
							strStepDetails = "Selected " + strObjectName + ": " + strData;
							GG_Report(strStepDetails, "Pass", true);
							return bResult;
						}
					}
				}
				else
				{
						Thread.sleep(iSleepTime);
						System.out.println("DROP DOWN LIST NOT UPDATED! ");
						System.out.println(String.format("Waited for %d milliseconds.[%s]", i + iSleepTime, weDropDownList));
						strStepDetails = "Not selected " + strObjectName + ": " + strData;
					
				}
			
				
			}
			
			GG_Log(strStepDetails);
		}
		catch(Exception ex)
		{
			System.err.println("Got an exception! ");
			ex.printStackTrace();
			GG_Log(ex.getMessage().toString());
		}
		return bResult;
	}
	
/*********************************************
 * 	
 * @return

*/	
//////////////////////////////////////////////////////////////

  public boolean GG_DoesWebElementExist(WebDriver webDriver, By by,int iTimeOut) 
	{
		boolean bPresent = false;
		try 
		{
			GG_FindElement(webDriver, by,iTimeOut);
			bPresent = true;
		}
		catch(NullPointerException ex) 
		{	
			System.out.println("Element does not found");
			ex.printStackTrace();
		}
		catch (RuntimeException ex)
		{
			bPresent = false;
			GG_Log(ex.getMessage().toString());
			//System.out.println("Got Exception");
		}
		
		return bPresent;
	}
  /**********************************************************************************************************
	 * Waits till the element disappears from the page
	 * @author skaur
	 * 17 Oct, 2012
	 * 
	 ***********************************************************************************************************/
  public void GG_WaitAnElementDisappears(WebDriver webDriver, By by, int iTimeOut)
	{
		int iTotal = 0;
		int iSleepTime = 6000;
		while(iTotal <= iTimeOut)
		{
			try
			{
				Thread.sleep(iSleepTime);
			
				if(GG_FindElement(webDriver, by,9000).isDisplayed())
					{
							iTotal = iTotal + iSleepTime;
							System.out.println("Element Found");
							System.out.println(String.format("Waiting to disappear %d milliseconds.[%s]", iTotal, by));          
						
					}
						
					else
					{
						GG_Log("Wait over");
						return;
					}
			}
			catch(Exception ex)
			{
				GG_Log("Element Not Found");
				return;
			}
		/*	catch(InterruptedException ex) 
			{
				throw new RuntimeException(ex);
				return;
			}*/
			
		}
	}
  
  /**********************************************************************************************************
 	 * @author skaur
 	 * @doc overload GG_FindElement for find by web element (not driver)
 	 * 
 	 ***********************************************************************************************************/

  public WebElement GG_FindElement(WebElement we, By by, int iTimeOut)
	{
		int iSleepTime = 3000;
		for(int i = 0; i < iTimeOut; i += iSleepTime)
		{
			List<WebElement> oWebElements = we.findElements(by);
			if(oWebElements.size()>0)
			{
				return oWebElements.get(0);
			}
			else
			{
				try
				{
					Thread.sleep(iSleepTime);
					System.out.println(String.format("Waited for %d milliseconds.[%s]", i + iSleepTime, by));          
				}
				catch(InterruptedException ex) 
				{	
					System.out.println("Element not found");
					GG_Log(ex.getMessage().toString());					
				}
			}
		}
		return null;
	}
  
  
  

	/**********************************************************************************************************
	 '  Function Name: LB_SwitchToIframe
	 '  Purpose:   This function will switch to Iframe
	 '  Inputs Parameters: (WebDriver webDriver, String strData)
	   webDriver: WebDriver to run the test
	   oObjectXpath: The xpath of specific object
	 '  Returns: true or false
	    true if switch successful
	    false if switch unsuccessful
	 '   Author       : Hung Nguyen Dang
	 '   Creation Date: 13/06/2012
	 /**********************************************************************************************************/
	
	
	public boolean GG_SwitchToIframe(WebDriver webDriver, String strFrameXpath)
	{
		String strStepDetails;
		boolean bResult = false;
		try
		{
			WebDriver Iframe = webDriver.switchTo().frame(strFrameXpath);
			System.out.println(Iframe.getTitle());
			strStepDetails = "SWITCH TO Iframe successfully !!!";
			//GG_Report(strStepDetails,"Pass", true);
			//System.out.println(strStepDetails);
			GG_Log(strStepDetails);
			bResult = true;
			//defaultWebDriver = false;
		}
		catch(Exception ex)
		{

			//System.err.println("Got an exception! ");
			ex.printStackTrace();
			GG_Log(ex.getMessage().toString());
		}
		return bResult;
	}
	/**********************************************************************************************************
	@author Skaur
	@doc  threeDaysAfter is used to pass date in calendar as the three next days to sys date.
	@return date string as dd/mm/yyyy
	@param 
	@Date 16Aug,2012
	*/	 
  
 
  public String GG_ThreeDaysAfter()
  {
	    String threeDaysAfter = "";
	    Date date = new Date();
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);

	    cal.add(Calendar.DAY_OF_YEAR, +3);
	    Date before = cal.getTime();
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	    threeDaysAfter = formatter.format(before);
	    
	    return threeDaysAfter;
	}
	public String GG_GenerateRandomName() 
	{ 
		//String characters="ABCDEFGYHUI";
		int length=5;
		String characters="abcdefghklmnopqrstuvwxyz";
		Random generator = new Random(); 
		char[] text = new char[length]; 
	    for (int i = 0; i < length; i++) 
	    { 
	        text[i] = characters.charAt(generator.nextInt(characters.length())); 
	    } 
	    text[0]= Character.toUpperCase(text[0]);
	    return new String(text); 
	   
	} 
	 boolean GG_ClickSpecificButton(WebDriver webDriver, String oObjectXpath, String strObjectName)
	    {
	    	String strStepDetails;
	    	boolean bResult = false;
	    	try
	    	{
	    	//	WebElement weelement = GG_FindElement(webDriver, By.className("ui-dialog-buttonpane"), intTimeOut);
	    		List <WebElement> lstButtons = webDriver.findElements(By.tagName("button"));

	    		//Click on Object
	    		for (WebElement weButton : lstButtons)
	    		{
					if(weButton.isDisplayed() == true)
					{
						String txtButtonName = weButton.getAttribute("innerHTML");
						if(txtButtonName.trim().contentEquals(oObjectXpath.trim()))
						{
							weButton.click();
							Thread.sleep(3000);

							strStepDetails = "Click on specific button: " + strObjectName + " successfully!!!";
							System.out.println(strStepDetails);
							//Log.info(strStepDetails);
							bResult = true;

							break;
						}
					}
				}

	    	}
	    	catch(Exception ex)
	    	{
	    		System.err.println("Got an exception! ");
	    		ex.printStackTrace();
	    	//	Log.info(ex.getMessage().toString());
	    	}
	    	return bResult;
	    }	
 public void GG_Wait()
 {
		 GG_Wait(5000);
 }
 public void GG_Wait(int i)
 {
		 try
		 {
			Thread.sleep(i);
		 }
		 catch (InterruptedException e) 
		 {
			
			e.printStackTrace();
		 }
 }
 public void GG_WaitAnElementPresent(WebDriver webDriver, By by, int iTimeOut)
	{
		int iTotal = 0;
		int iSleepTime = 3000;
		while(iTotal < iTimeOut)
		{
				
			//List<WebElement> oWebElements = webDriver.findElements(by);
			//if(oWebElements.size()>0)
			if(GG_DoesWebElementExist(webDriver, by, 3000))
				return;
			else
			{
				try
				{
					Thread.sleep(iSleepTime);
					iTotal = iTotal + iSleepTime;
					System.out.println(String.format("Waiting for %d milliseconds.[%s]", iTotal, by));          
				}
				catch(InterruptedException ex) 
				{
					System.out.println("Sorry could not find given element");
					//throw new RuntimeException(ex);
				}
			}
		}
	}
	public WebElement GG_FindElement(WebDriver webDriver, By by, int iTimeOut)
	{
		int iSleepTime = 3000;
		for(int i = 0; i < iTimeOut; i += iSleepTime)
		{
			List<WebElement> oWebElements = webDriver.findElements(by);
			if(oWebElements.size()>0)
			{
				return oWebElements.get(0);
			}
			else
			{
				try
				{
					Thread.sleep(iSleepTime);
					System.out.println(String.format("Waited for %d milliseconds.[%s]", i + iSleepTime, by));          
				}
				catch(InterruptedException ex) 
				{	
					throw new RuntimeException(ex);					
				}
				catch(NullPointerException ex) 
				{	
					System.out.println("Element does not found");
					ex.printStackTrace();
				}
			}
		}
		// Can't find 'by' element. Therefore throw an exception.
		String sException = String.format("Can't find %s after %d milliseconds.", by, iTimeOut);
		throw new RuntimeException(sException); 
		
	}
	public boolean GG_Input(WebDriver webDriver, By by, String strObjectName, String strData)
	{
		String strStepDetails;
		boolean bResult = false;
		try
		{			
			GG_WaitAnElementPresent(webDriver, by, intTimeOut);
			WebElement oInput = GG_FindElement(webDriver, by, intTimeOut);
			if(GG_DoesWebElementExist(webDriver, by, intTimeOut))
			//Input value to object
			{	
				oInput.clear();
				oInput.sendKeys(strData);
				
				strStepDetails = "Enter value to " + strObjectName + ": " + strData;
				//System.out.println(strStepDetails);			
				GG_Log(strStepDetails);
				bResult = true;
				GG_Report(strStepDetails, "Pass", true);
			}
			else
			{
				
				GG_Report("Element is not Present", "Fail", true);
				GG_captureScreen(webDriver, "input");
			}
		}
		catch(Exception ex)
		{
			
			//System.err.println("Got an exception! ");
			GG_Report("Fail to enter the value for "+strObjectName, "Fail", true);
			GG_captureScreen(webDriver, "input");
			ex.printStackTrace();
			GG_Log(ex.getMessage().toString());
		}
		return bResult;
	}
	
	public boolean GG_Click(WebDriver webDriver, By by, String strObjectName)
	{
		String strStepDetails;
		boolean bResult = false,bExist= false;
		try
		{
			GG_WaitAnElementPresent(webDriver, by, intTimeOut);
			bExist = GG_DoesWebElementExist(webDriver, by, intTimeOut);
			
			if(!bExist)
				{
					GG_captureScreen(webDriver, "Click");
					strStepDetails = strObjectName +" Not Found";
					GG_Log(strStepDetails);
					GG_Report(strStepDetails, "Fail", true);
					return bResult;
				}
			WebElement oClick = GG_FindElement(webDriver, by, 5000);
			//Click on Object
			//GG_Wait(2000); // tested and commented
			oClick.click();	
			
			

			strStepDetails = "Click on " + strObjectName;
			GG_Log(strStepDetails);
			bResult = true;
			GG_Report(strStepDetails, "Pass", true);
		}
		catch(Exception ex)
		{
			GG_captureScreen(webDriver, "click");
			strStepDetails = "Fail to click on " + strObjectName;
			System.err.println("Got an exception! ");
			ex.printStackTrace();
			GG_Log(ex.getMessage().toString());
			GG_Report(strStepDetails, "Fail", true);
		}
		return bResult;
	}
	/**********************************************************************************************************
	@author Skaur
	@doc  click fast
	@param WebDriver, name of the file
	@Date 3Oct,2012
	*/	 	
	public boolean GG_ClickFast(WebDriver webDriver, By by, String strObjectName)
	{
		String strStepDetails;
		boolean bResult = false,bExist= false;
		try
		{
			bExist = GG_DoesWebElementExist(webDriver, by, intTimeOut);
			
			if(!bExist)
				{
					GG_captureScreen(webDriver, "input");
					strStepDetails = strObjectName +" Not Found";
					GG_Log(strStepDetails);
					GG_Report(strStepDetails, "Fail", true);
					return bResult;
				}
			WebElement oClick = GG_FindElement(webDriver, by, 5000);
			oClick.click();	
			strStepDetails = "Click on " + strObjectName;
			GG_Log(strStepDetails);
			bResult = true;
			GG_Report(strStepDetails, "Pass", true);
		}
		catch(Exception ex)
		{
			GG_captureScreen(webDriver, "input");
			strStepDetails = "Fail to click on " + strObjectName;
			System.err.println("Got an exception! ");
			ex.printStackTrace();
			GG_Log(ex.getMessage().toString());
			GG_Report(strStepDetails, "Fail", true);
		}
		return bResult;
	}
	/**********************************************************************************************************
	@author Skaur
	@doc  take a screenshot
	@param WebDriver, name of the file
	@Date 3Oct,2012
	*/	 	
	public void GG_captureScreen(WebDriver driver,String name) {

	    String msg,path;
	    String res;
	    try
	    {
	    	
	        File source = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	        	path = "./ScreenShots/" +name+"_"+ source.getName();
	        FileUtils.copyFile(source, new File(path));
	        msg = "Screenshot captured at"+ path;
	    	res="Pass";
	    }
	    catch(IOException e) {
	        msg = "Failed to capture screenshot: " + e.getMessage();
	        res="Fail";
	    }
	    GG_Report(msg, res, true);
	    GG_Log(msg);
	}

	boolean hoverOnMenu(WebDriver webDriver, String oObjectXpath, String strObjectName)
	{
		String strStepDetails;
		boolean bResult = false;
		try
		{
			 
			Actions builder = new Actions(webDriver);    
			WebElement weMenu = webDriver.findElement(By.xpath(".//*[@id='menuItem417']/a"));//GG_FindElement"));//(webDriver, By.id("menuItem417"),intTimeOut);
			//builder.clickAndHold(weMenu).build().perform();
		//	driver.findElement(By.linkText("Awaiting Deployment")).click(); 
			
			//builder.moveToElement(toElement, xOffset, yOffset)
			builder.moveToElement(weMenu).build().perform();
			GG_Wait();
			//webDriver.findElement(By.linkText("Awaiting Deployment")).click(); 
		//	new WebDriverWait(webDriver, 50, 50).until(ExpectedConditions.elementToBeClickable(By.linkText("Awaiting Deployment")));
			webDriver.findElement(By.xpath(".//*[@id='menuItem417']/ul/li[2]/a")).click();
			
			//builder.release().perform();
			strStepDetails = "Mouseover on " + strObjectName;
			System.out.println(strStepDetails);
		
			bResult = true;
		}
		catch(Exception ex)
		{
			System.err.println("Got an exception! ");
			ex.printStackTrace();
			
		}
		return bResult;
	}

	/* cleans and converts amount displayed to double
	 * 
	 */
	public double GG_StringToDouble(String le)
	{
		try
		{
			boolean neg = false;
			if (le==null || le.equals("")|| le.equals("NA") )
			{
				return 0.0;
			}
			else 
			{
				//le.replaceAll("[^\\d-]+", "");
				if(le.contains("-"))
				{
					neg = true;
					le = le.replace("-", "");
				}
				le =le.replaceAll("\\s","");
				le = le.replaceAll(",","");
				le = le.replace("$"," ");
				le = le.trim();
				if(neg) le= "-"+le;
		
				//Double d = (double)Math.round(Double.parseDouble(le)* 100) / 100;
				return Double.parseDouble(le);
				
			}
			
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			return 0.0;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0.0;
		}
	}	
	
public static void main(String args[]) throws FileNotFoundException, IOException
{
	Keywords fg = new Keywords();
	double d =  fg.GG_StringToDouble("$- 7,8900.00");
	System.out.println(d);
	
	
}
}
