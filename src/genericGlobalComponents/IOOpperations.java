package genericGlobalComponents;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class IOOpperations {

	public FileInputStream fs;

	// private boolean defaultWebDriver = true;

	// for reporting function
	private boolean new_file;
	private boolean new_file1;
	private int row = 0;
	private static WritableCellFormat timesBoldUnderline, times, pass, fail, function;
	public static final String DATE_FORMAT_NOW = "dd/MM/yyyy   HH:mm:ss";
	public static final String DATE_FORMAT_NOW1 = "dd/MM/yyyy";
	public static String dt;
	public String strBrowser, strOS;
	private static String sTestRunID = "1";
	// for Data input file
	private String strDataInputPath;
	ClassLoader classLoader;
	private Properties prop;

	public IOOpperations() {
		prop = new Properties();
		try {
			/*
			 * InputStream stream =
			 * IOOpperations.class.getResourceAsStream("/Configuration.properties"
			 * ); System.out.println(stream != null); stream =
			 * IOOpperations.class.getClassLoader()
			 * .getResourceAsStream("Configuration.properties");
			 * System.out.println(stream != null);
			 */
			String url = this.getClass().getResource("/Configuration.properties").getPath();
			System.out.println(url + "hello");
			URL resource = this.getClass().getResource("/Configuration.properties");
			File file = new File(resource.toURI());
			prop.load(new FileInputStream(file));
			strBrowser = prop.getProperty("strBrowser");

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*********************************************************************************************************************************
	 * FUNCTIONS FOR INPUT OUTPUT OPERATIONS
	 ***********************************************************************************************************************************/
	/**********************************************************************************************************
	 * ' Function Name: TB_GG_readXL ' Purpose: read function for reading value
	 * from given row number and a column value to match ' Inputs Parameters: 1
	 * -Row Number , "username" - Column Name ' Returns: value for given row and
	 * calculated column ' Author : Skaur ' Creation Date: 07/08/2012 /
	 **********************************************************************************************************/

	public String GG_ReadXL(int row, String column, String strFilePath) {
		Cell c = null;
		int reqCol = 0;
		WorkbookSettings ws = null;
		Workbook workbook = null;
		Sheet sheet = null;
		strFilePath = GG_GetDataInputPath() + "/" + strFilePath;
		// value used to return value

		try {
			URL resource = this.getClass().getResource("/" + strFilePath);
			File file = new File(resource.toURI());
			fs = new FileInputStream(file);
			ws = new WorkbookSettings();
			ws.setLocale(new Locale("en", "EN"));

			// opening the work book and sheet for reading y's
			workbook = Workbook.getWorkbook(fs, ws);
			sheet = workbook.getSheet(0);

			// Sanitize given data
			String col = column.trim();
			// loop for going through the given row
			for (int j = 0; j < sheet.getColumns(); j++) {
				Cell cell = sheet.getCell(j, 0);
				if ((cell.getContents().trim()).equalsIgnoreCase(col)) {
					// System.out.println(cell.getContents());
					reqCol = cell.getColumn();
					// System.out.println("column No:"+reqCol);
					c = sheet.getCell(reqCol, row);
					fs.close();
					// System.out.println(c.getContents());
					return c.getContents();
				}

			}

		} catch (BiffException be) {

			GG_Log(be.getMessage().toString());
			GG_Log("The given file should have .xls extension.");
			System.out.println("The given file should have .xls extension.");
		} catch (Exception e) {
			e.printStackTrace();
			GG_Log(e.getMessage().toString());
		}
		GG_Log("NO MATCH FOUND IN GIVEN FILE: PROBLEM IS COMING FROM DATA FILE");
		return null;
	}

	public void GG_ReportSp(int col, int row, String... stepName) {
		Workbook workbook1;
		WritableWorkbook workbook;
		// /Test_ToolBoxV0.1

		File file = new File("./Results/StampDudtyCalculator.xls");
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		try {
			if (file.exists()) {
				workbook1 = Workbook.getWorkbook(file, wbSettings);
				workbook = Workbook.createWorkbook(file, workbook1);
				new_file = false;
			} else {

				workbook = Workbook.createWorkbook(file, wbSettings);
				workbook.createSheet("Report", 0);
				System.out.println("Creating file" + workbook.toString());// if(workbook.equals(null))
				new_file = true;
			}
			WritableSheet excelSheet = workbook.getSheet(0);
			// createLabel(excelSheet);
			// if the file has been created first time
			// this.row = ((Sheet)excelSheet).getRows();
			// Cell c = excelSheet.getCell("nkkjjj");
			// int row = c.getRow();
			if (new_file) {
				addCaption(excelSheet, 0, 0, "Sprint No");
				addCaption(excelSheet, 1, 0, "Test Case");
				addCaption(excelSheet, 2, 0, "Purchase Property State");
				addCaption(excelSheet, 3, 0, "First Home Buyer");
				addCaption(excelSheet, 4, 0, "Owner Occupied");
				addCaption(excelSheet, 5, 0, "Purchase Price");
				addCaption(excelSheet, 6, 0, "Primary Loan Reason");
				addCaption(excelSheet, 7, 0, "Expected Stamp Duty");
				addCaption(excelSheet, 8, 0, "Actual Stamp Duty");
				addCaption(excelSheet, 9, 0, "Stamp Duty Result");
				addCaption(excelSheet, 10, 0, "Expected Transfer and Registration Fee");
				addCaption(excelSheet, 11, 0, "Actual Transfer and Registration Fee");
				addCaption(excelSheet, 12, 0, "Transfer and Registration FeeResult");

				for (int i = 13; i < 19; i++) {
					addCaption(excelSheet, i, 0, "");

				}
			} else {
				int intNo = col;
				for (String s : stepName) {
					addLabel1(excelSheet, intNo, row, s);
					intNo++;
				}

			}

			workbook.write();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
			GG_Log(e.toString());
		}
	}

	/***
	 * NEW FUNCTIONS FOR REPORTING
	 */
	public void GG_Report(String stepName, String result, boolean bIsStep) {
		GG_Report(stepName, result, bIsStep, false);

	}

	/**********************************************************************************************************
	 * ' @ Function Name: GG_Report ' @doc Depending upon the given
	 * configuration it calls the web or only XL reporting ' @author Surinder '
	 * Date: 10/05/2013 /
	 **********************************************************************************************************/

	public void GG_Report(String stepName, String result, boolean bIsStep, boolean bIsImage) {
		String configReports = prop.getProperty("WebReports");
		if (configReports.equalsIgnoreCase("true"))
			GG_DBandXLReport(stepName, result, bIsStep, bIsImage);
		else
			GG_XLReport(stepName, result, bIsStep);
	}

	/**********************************************************************************************************
	 * ' @ Function Name: GG_Report ' @doc web reporting ' @author Surinder '
	 * Date: 10/05/2013 /
	 **********************************************************************************************************/
	public void GG_DBandXLReport(String stepName, String result, boolean bIsStep, boolean bIsImage) {
		String strExactBrowser = "";
		strOS = prop.getProperty("OS");
		String strBuildNo = prop.getProperty("BuildNo");
		String strScriptName = "", sStepRunID = "1";
		if (bIsStep)
			strScriptName = "";
		else
			strScriptName = stepName;

		if (strBrowser != null && strBrowser.equalsIgnoreCase("firefox")) {
			strExactBrowser = "Firefox";
		} else if (strBrowser != null && strBrowser.equalsIgnoreCase("iexplore")) {
			strExactBrowser = "IE";
		} else if (strBrowser != null && strBrowser.equalsIgnoreCase("chrome")) {
			strExactBrowser = "Chrome";
		} else if (strBrowser != null && strBrowser.equalsIgnoreCase("safari")) {
			strExactBrowser = "Safari";
		}

		Workbook workbook1;
		WritableWorkbook workbook;
		File file = new File("./Results/Results.xls");
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		try {
			if (file.exists()) {
				workbook1 = Workbook.getWorkbook(file, wbSettings);
				workbook = Workbook.createWorkbook(file, workbook1);
				new_file = false;
			} else {

				workbook = Workbook.createWorkbook(file, wbSettings);
				workbook.createSheet("Report", 0);
				System.out.println("Creating file" + workbook.toString());// if(workbook.equals(null))
				new_file = true;
			}

			WritableSheet excelSheet = workbook.getSheet(0);
			createLabel(excelSheet);
			// if the file has been created first time
			this.row = ((Sheet) excelSheet).getRows();
			// addNumber(excelSheet, 0, row, row );

			if (bIsStep) {

				// addLabel(excelSheet, 1, row, strScriptName);
				addLabel(excelSheet, 2, row, stepName);
				addLabel(excelSheet, 3, row, result);
				addLabel(excelSheet, 4, row, DateTime());

			} else {

				// addSuite(excelSheet, 0, row, "" );
				addSuite(excelSheet, 0, row, Integer.parseInt(prop.getProperty("BuildNo")) + "");
				// Second column
				addSuite(excelSheet, 1, row, strScriptName);

				if (result.equals(""))
					addSuite(excelSheet, 2, row, stepName);
				else
					addSuite(excelSheet, 2, row, stepName);

				addSuite(excelSheet, 3, row, result);

				// further columns
				for (int i = 4; i < 15; i++) {
					addSuite(excelSheet, i, row, "");

				}

			}

			workbook.write();
			workbook.close();

			// For DataBase Writing

			if (!bIsStep) {
				// Check if result field is blank or not. If yes, a new runID is
				// created

				if (result != null && result.equalsIgnoreCase(""))

				{
					result = "Not Completed";

					int maxnumR = 0;

					System.out.println("MySQL Connect Example step 1.");
					Connection conn = null;
					String url = "jdbc:mysql://localhost/";
					String dbName = "TestAutomation";
					String dbdriver = "com.mysql.jdbc.Driver";
					String userName = "root";
					String password = "";

					try {
						Class.forName(dbdriver).newInstance();
						conn = DriverManager.getConnection(url + dbName, userName, password);
						Statement st = conn.createStatement();

						System.out.println("Connected to the database for RUN ID creation");
						String sDateTime = DateTime();

						String SQL = "INSERT INTO mastertest VALUES (null " + ",'" + result + "','" + strExactBrowser
								+ "','" + strScriptName + "','" + strOS + "','" + strBuildNo + "'," + null + ",'"
								+ stepName + "');";
						// String UpdateSQL = "UPDATE mastertest SET Status ='"
						// + result + "' WHERE TestRunID = '" +sTestRunID +
						// ");";

						st.executeUpdate(SQL);
						System.out.println("Get run ID values");

						String req1 = "SELECT max(TestRunID) FROM mastertest";
						Statement st2 = conn.createStatement();
						ResultSet r1 = st2.executeQuery(req1);
						System.out.println("Query executed");
						while (r1.next()) {
							maxnumR = r1.getInt("MAX(TestRunID)");
							this.sTestRunID = String.valueOf(maxnumR);
							// System.out.println("MAX(TestRunID)="+r1.getInt("MAX(TestRunID)"));

							System.out.println("MAX(TestRunID)=" + sTestRunID);
						}

						System.out.println("Test Run ID generated is = " + sTestRunID);
						conn.close();
						System.out.println("Disconnected from database for RUN ID creation");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else

				{
					int maxnumR = 0;

					System.out.println("MySQL Connect Example. step 2");
					Connection conn = null;
					String url = "jdbc:mysql://localhost/";
					String dbName = "TestAutomation";
					String dbdriver = "com.mysql.jdbc.Driver";
					String userName = "root";
					String password = "";

					try {
						Class.forName(dbdriver).newInstance();
						conn = DriverManager.getConnection(url + dbName, userName, password);
						Statement st = conn.createStatement();
						// Scanner first = new Scanner(System.in);
						// System.out.println(first.nextLine());
						System.out.println("Connected to the database for mastertable for updation");

						String UpdateSQL = "UPDATE mastertest SET Status ='" + result + "' WHERE TestRunID = '"
								+ sTestRunID + "';";
						st.executeUpdate(UpdateSQL);

						conn.close();
						System.out.println("Disconnected from database for mastertable updation");
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}

			else {

				System.out.println("MySQL Connect Example.step 3");
				Connection conn = null;
				String url = "jdbc:mysql://localhost/";
				String dbName = "TestAutomation";
				String dbdriver = "com.mysql.jdbc.Driver";
				String userName = "root";
				String password = "";
				String strImage;
				try {
					Class.forName(dbdriver).newInstance();
					conn = DriverManager.getConnection(url + dbName, userName, password);
					Statement st = conn.createStatement();
					// Scanner first = new Scanner(System.in);
					// System.out.println(first.nextLine());
					System.out.println("Inserting into TEststep table");

					if (bIsImage) {
						strImage = "Y";
					}

					else {
						strImage = "N";
					}
					// String SQL =
					// "INSERT INTO teststepdetails VALUES ('Step Details', 'Pass',null, 7 )";
					String SQL = "INSERT INTO teststepdetails VALUES (null" + ",'" + strImage + "','" + stepName
							+ "','" + result + "'," + null + ",'" + sTestRunID + "'" + ");";
					st.executeUpdate(SQL);

					System.out.println("Test Run ID inserted =" + sTestRunID);

					// Get max Step Run ID

					int maxStep;
					String req2 = "SELECT max(StepRunID) FROM teststepdetails";
					Statement st3 = conn.createStatement();
					ResultSet r2 = st3.executeQuery(req2);
					System.out.println("Query executed");
					while (r2.next()) {
						maxStep = r2.getInt("MAX(StepRunID)");
						sStepRunID = String.valueOf(maxStep);
						// System.out.println("MAX(TestRunID)="+r1.getInt("MAX(TestRunID)"));
					}

					System.out.println("Test Step Run ID inserted =" + sStepRunID);

					if (bIsImage) {
						// GG_CaptureSnapshot(driver, sPath + sStepRunID +".png"
						// );
					}

					// System.out.println("Test Run ID generated is = " +
					// sTestRunID);
					conn.close();
					System.out.println("Disconnected from TestStep Table database");
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			GG_Log(e.toString());
		}
	}

	/*
	 * public static void GG_CaptureSnapshot(WebDriver webDriver, String
	 * sPath)throws Exception 
	 * { File scrFile =
	 * ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE); // Now you
	 * can do whatever you need to do with it, for example copy somewhere
	 * FileUtils.copyFile(scrFile, new File(sPath)); scrFile = null;
	 * 
	 * }
	 */

	public void GG_XLReport(String stepName, String result, boolean b) {
		Workbook workbook1;
		WritableWorkbook workbook;
		String strFilePath;
		strFilePath = "./Results/Results.xls";
		try {
			URL resource = this.getClass().getResource("/Results.xls");
			File file;

			// jar File file = new File(strFilePath);
			WorkbookSettings wbSettings = new WorkbookSettings();
			wbSettings.setLocale(new Locale("en", "EN"));
			// if(file.exists())
			if (resource != null) {
				file = new File(resource.toURI());
				workbook1 = Workbook.getWorkbook(file, wbSettings);
				workbook = Workbook.createWorkbook(file, workbook1);
				new_file = false;
			} else {
				file = new File(strFilePath);
				workbook = Workbook.createWorkbook(file, wbSettings);
				workbook.createSheet("Report", 0);
				System.out.println("Creating file" + workbook.toString());// if(workbook.equals(null))
				new_file = true;
			}

			WritableSheet excelSheet = workbook.getSheet(0);
			createLabel(excelSheet);
			// if the file has been created first time
			this.row = ((Sheet) excelSheet).getRows();
			// addNumber(excelSheet, 0, row, row );

			if (b) {

				if (!(result.equals(""))) {
					addLabel(excelSheet, 1, row, stepName);
					addLabel(excelSheet, 2, row, result);
				} else {
					addLabel(excelSheet, 2, row, "Function");
					addLabel(excelSheet, 1, row, stepName);
				}

				if (!(stepName.equals("")))
					addLabel(excelSheet, 3, row, DateTime());
				else
					addLabel(excelSheet, 3, row, "");

			} else {

				// addSuite(excelSheet, 0, row, "" );
				addSuite(excelSheet, 0, row, prop.getProperty("BuildNo") + "");
				// Second column
				if (result.equals("")) {
					addSuite(excelSheet, 1, row, stepName);
					addSuite(excelSheet, 2, row, "");
				} else
				/*
				 * addSuite(excelSheet, 1, row, stepName+" = "+result);
				 * addSuite(excelSheet, 2, row, "");
				 */
				{
					addSuite(excelSheet, 1, row, stepName);
					addSuite(excelSheet, 2, row, result);
				}

				// further columns
				for (int i = 3; i < 15; i++) {
					addSuite(excelSheet, i, row, "");

				}

			}

			workbook.write();
			workbook.close();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			GG_Log(e1.toString());
		} catch (Exception e) {
			e.printStackTrace();
			GG_Log(e.toString());
		}

	}

	/**********************************************************************************************************
	 * @category Helper
	 ***********************************************************************************************************/
	private void createLabel(WritableSheet sheet) {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
		// Create create a bold font with underlines
		WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);

		try {
			// Define the cell format
			Colour c = Colour.WHITE;
			Colour cy = Colour.ICE_BLUE;
			Colour cpass = Colour.GREEN;
			Colour cfail = Colour.RED;
			Colour cfunction = Colour.GRAY_25;

			times = new WritableCellFormat(times10pt);
			pass = new WritableCellFormat(times10pt);
			fail = new WritableCellFormat(times10pt);
			function = new WritableCellFormat(times10pt);

			// Lets automatically wrap the cells
			/*
			 * times.setWrap(true); pass.setWrap(true); fail.setWrap(true);
			 * function.setWrap(true);
			 */
			times.setBackground(cy);
			pass.setBackground(cpass);
			fail.setBackground(cfail);
			function.setBackground(cfunction);

			// new WritableFont(WritableFont.BOLD,12,false);
			// .TIMES, 10, WritableFont.BOLD, false,
			// UnderlineStyle.SINGLE, c);

			times10ptBoldUnderline.setColour(c);
			timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
			// Lets automatically wrap the cells
			timesBoldUnderline.setWrap(true);
			// timesBoldUnderline.setAlignment(a)
			timesBoldUnderline.setBackground(Colour.VIOLET);

			/*
			 * CellView cv = new CellView(); cv.setFormat(times);
			 * cv.setFormat(timesBoldUnderline);
			 */

			// CellFormat cf=new CellFormat(times);
			// cv.setFormat(cf);
			// cv.setAutosize(true);

			// Write a few headers
			if (new_file) {

				addCaption(sheet, 0, 0, "Sprint No");
				addCaption(sheet, 1, 0, "BP Details");
				addCaption(sheet, 2, 0, "Step Status");
				addCaption(sheet, 3, 0, "Execution Date-Time");

				for (int i = 4; i < 15; i++) {
					addCaption(sheet, i, row, "");

				}

			}
			if (new_file1) {

				addCaption(sheet, 0, 0, "Sprint No");
				addCaption(sheet, 1, 0, "BP ID");
				addCaption(sheet, 2, 0, "Application Number");
			}
		} catch (WriteException e) {
			e.printStackTrace();
			GG_Log("GG_CreateLable fail" + e.toString());
		}

	}

	/**********************************************************************************************************
	 * @category Helper
	 ***********************************************************************************************************/

	private void addCaption(WritableSheet sheet, int column, int row, String s) throws RowsExceededException,
			WriteException {
		Label label;
		label = new Label(column, row, s, timesBoldUnderline);
		sheet.addCell(label);
	}

	/**********************************************************************************************************
	 * @category Helper
	 ***********************************************************************************************************/
	boolean dfun = false;

	private void addLabel(WritableSheet sheet, int column, int row, String s) throws WriteException,
			RowsExceededException {
		Label label;

		if (s.equalsIgnoreCase("Pass")) {
			label = new Label(column, row, s, pass);
			sheet.addCell(label);
		} else if (s.equalsIgnoreCase("Fail")) {
			label = new Label(column, row, s, fail);
			sheet.addCell(label);
		} else if (s.equalsIgnoreCase("Function")) {

			label = new Label(column, row, "", function);
			sheet.addCell(label);
			dfun = true;
		} else if (dfun) {

			label = new Label(column, row, s, function);
			sheet.addCell(label);
			dfun = false;
		} else {
			label = new Label(column, row, s);
			sheet.addCell(label);
		}

	}

	/**********************************************************************************************************
	 * @category Helper
	 ***********************************************************************************************************/
	private void addLabel1(WritableSheet sheet, int column, int row, String s) throws WriteException,
			RowsExceededException {
		Label label;
		label = new Label(column, row, s);
		sheet.addCell(label);
	}

	/**********************************************************************************************************
	 * @category Helper
	 ***********************************************************************************************************/
	private void addSuite(WritableSheet sheet, int column, int row, String s) throws WriteException,
			RowsExceededException {
		Label label;
		if (s.equalsIgnoreCase("Pass")) {
			label = new Label(column, row, s, pass);
			sheet.addCell(label);
		} else if (s.equalsIgnoreCase("Fail")) {
			label = new Label(column, row, s, fail);
			sheet.addCell(label);
		} else {
			label = new Label(column, row, s, times);
			sheet.addCell(label);
		}

	}

	/**********************************************************************************************************
	 * @category Helper
	 ***********************************************************************************************************/
	public static String DateTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	/*
	 * public void GG_Report(String stepName,String result, boolean b) {
	 * Workbook workbook1; WritableWorkbook workbook; String strFilePath ;
	 * 
	 * strFilePath = "./Results/Results.xls"; File file = new File(strFilePath);
	 * WorkbookSettings wbSettings = new WorkbookSettings();
	 * wbSettings.setLocale(new Locale("en", "EN")); try { if(file.exists()) {
	 * workbook1 = Workbook.getWorkbook(file, wbSettings); workbook =
	 * Workbook.createWorkbook(file, workbook1); new_file= false; } else {
	 * 
	 * workbook = Workbook.createWorkbook(file, wbSettings);
	 * workbook.createSheet("Report", 0); System.out.println("Creating file" +
	 * workbook.toString());//if(workbook.equals(null)) new_file= true; }
	 * 
	 * WritableSheet excelSheet = workbook.getSheet(0); createLabel(excelSheet);
	 * // if the file has been created first time this.row =
	 * ((Sheet)excelSheet).getRows(); //addNumber(excelSheet, 0, row, row );
	 * 
	 * if(b) {
	 * 
	 * if(!(result.equals(""))) { addLabel(excelSheet, 1, row, stepName);
	 * addLabel(excelSheet, 2, row, result); } else { addLabel(excelSheet, 2,
	 * row, "Function"); addLabel(excelSheet, 1, row, stepName); }
	 * 
	 * if(!(stepName.equals(""))) addLabel(excelSheet, 3, row, DateTime()); else
	 * addLabel(excelSheet, 3, row, "");
	 * 
	 * } else {
	 * 
	 * //addSuite(excelSheet, 0, row, "" ); addSuite(excelSheet, 0, row,
	 * Integer.parseInt(prop.getProperty("Sprint"))+"" ); // Second column
	 * if(result.equals("")) { addSuite(excelSheet, 1, row, stepName);
	 * addSuite(excelSheet, 2, row, ""); } else // addSuite(excelSheet, 1, row,
	 * stepName+" = "+result); //addSuite(excelSheet, 2, row, ""); {
	 * addSuite(excelSheet, 1, row,stepName); addSuite(excelSheet, 2, row,
	 * result); }
	 * 
	 * //further columns for(int i=3; i<15; i++ ) { addSuite(excelSheet, i, row,
	 * "" );
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * workbook.write(); workbook.close(); } catch(Exception e) {
	 * e.printStackTrace(); GG_Log(e.toString()); } }
	 */

	/**********************************************************************************************************
	 * /************************************************************************
	 * ********************************** ' Function Name: TB_GG_log ' Purpose:
	 * Log function is for reading TestSiute to decide the execution of a given
	 * test/function ' Inputs Parameters: Text to be added to log ' Returns:
	 * 
	 * @author Skaur ' Creation Date: 07/08/2012 /
	 **********************************************************************************************************/

	public void GG_Log(String data) {
		File file;
		FileWriter fileWritter;
		BufferedWriter bufferWritter;
		try {
			file = new File("Logfile.log");
			// if file doesn't exists, then create it
			if (!file.exists()) {
				System.out.println("Log file Created");
				file.createNewFile();
			}
			// true = append file
			else {
				fileWritter = new FileWriter(file.getName(), true);
				bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(System.currentTimeMillis() + "[class name]  ");
				bufferWritter.write("INFO:   " + data);
				bufferWritter.newLine();
				bufferWritter.close();
				System.out.println(data);
				// System.out.println("Done");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**********************************************************************************************************
	 * ' Function Name: GG_SetInputFile ' Purpose: SetInputFile function is for
	 * setting input data files for current operations ' Inputs Parameters:
	 * file-name.xls ' Returns: complete path to file ' Author : Surinder '
	 * Creation Date: 14/08/2012 /
	 **********************************************************************************************************/
	public String GG_SetInputFile(String dataFile) {
		String strPath = "";

		try {
			strPath = prop.getProperty("strTestDataPath");

			// initializing class variable for data input file
			String strDataInput = strPath.trim() + "/" + dataFile.trim();
			this.GG_SetDataInputPath(strDataInput);
			// this.backupDataFile = strPath + "backup" + dataFile;
			return strDataInput;
		} catch (Exception ex) {
			System.err.println("Got an exception setting Data input file! ");
			ex.printStackTrace();
			GG_Log("Got an exception setting Data input file! " + ex.getMessage().toString());
		}

		GG_Log("Could not set input data file for:" + strPath);
		return null;
	}

	/**********************************************************************************************************
	 * @category Helper
	 * @param path
	 * @author Surinder
	 ***********************************************************************************************************/

	public void GG_SetDataInputPath(String path) {
		this.strDataInputPath = path;
	}

	/**********************************************************************************************************
	 * @category Helper
	 * @author Surinder
	 ***********************************************************************************************************/

	public String GG_GetDataInputPath() {
		return this.strDataInputPath;
	}

	/**********************************************************************************************************
	 * ' Function Name: TB_GG_GetExecutionRow ' @ Purpose: GetExecutionRow
	 * function is for setting input data row for current file ' @ return row
	 * number ' @author Surinder ' Creation Date: 14/08/2012 /
	 **********************************************************************************************************/

	public int GG_GetExecutionRow() {
		String strFile = this.GG_GetDataInputPath();

		try {
			String row = this.GG_ReadXL(1, "Execution_Rowid", this.GG_GetDataInputPath());
			// String row = this.TB_GG_ReadXL(1, "Execution_Rowid",
			// prop.getProperty("strTestSuiteFile"));
			return Integer.parseInt(row);
		} catch (NumberFormatException e) {
			GG_Log("Got an exception getting the execution row for given File: " + strFile
					+ " Please pass TC_Id in marked cell of the row you want to execute the test for! "
					+ e.getMessage().toString());
		} catch (Exception ex) {
			System.err.println("Got an exception getting the execution row for given File: " + strFile);
			ex.printStackTrace();
			GG_Log("Got an exception getting the execution row for given File: " + strFile + ex.getMessage().toString());
		}

		GG_Log("Could not set Execution Row for:" + strFile);
		return (Integer) null;
	}

	/**********************************************************************************************************
	 * ' Function Name: GG_VerifyExecution ' Purpose: Called to verify the
	 * execution status Y/N from ./DataPool/TestSuites.xls ' Inputs Parameters:
	 * Login - pass the name of method/test given in execution control file:
	 * TestSuites ' Returns: true if gets Y ' @author Skaur ' Creation Date:
	 * 07/08/2012 /
	 **********************************************************************************************************/
	public boolean GG_VerifyExecution(String method) {
		// boolean exitvar = false;
		WorkbookSettings ws = null;
		Workbook workbook = null;
		Sheet sheet = null;
		String strTestSuiteFile;

		strTestSuiteFile = GG_GetDataInputPath() + "/TB_DSL_TestSuites.xls";
		int rowCount = '0';
		int columnCount = '0';

		boolean y = false;
		try {
			URL resource = this.getClass().getResource(strTestSuiteFile);
			File file = new File(resource.toURI());
			fs = new FileInputStream(file);
			// fs = new FileInputStream(new File(strTestSuiteFile))
			ws = new WorkbookSettings();
			ws.setLocale(new Locale("en", "EN"));
			// opening the work book and sheet for reading y's
			workbook = Workbook.getWorkbook(fs, ws);
			sheet = workbook.getSheet(0);

			// System.out.println("Total Rows inside Sheet:" + sheet.getRows());
			rowCount = sheet.getRows();

			// System.out.println("Total Column inside Sheet:" +
			// sheet.getColumns());
			columnCount = sheet.getColumns();
			String meth_name = method.trim();
			for (int i = 1; i < rowCount; i++) {
				// Get Individual Row starting from second
				// for (int j=1;j<columnCount;j++)
				{
					Cell cell = sheet.getCell(2, i);
					if (cell.getContents().equalsIgnoreCase(meth_name)) {
						Cell cell1 = sheet.getCell(2 + 1, i);
						if (cell1 != null) {
							if (cell1.getContents().equalsIgnoreCase("Y")) {
								y = true;
								GG_Log("Adding " + method + " to execution list");
							} else if (cell1.getContents().equalsIgnoreCase("N")) {
								y = false;
							} else {
								y = false;
								// System.out.println("NO VALUE GIVEN FOR "+method);
								// TB_GG_Log("NO VALUE GIVEN FOR "+method);
							}
						}
					}
					if (cell.getContents().equalsIgnoreCase("END")) {
						// exitvar=true;
						break;
					}

				}
			}// outer for
			fs.close();
		} catch (FileNotFoundException f) {
			GG_Log(f.getMessage().toString());
			GG_Log("The given TestSuites.xls does not exist");
			System.err.println("The given TestSuites.xls does not exist");
		} catch (BiffException be) {

			be.printStackTrace();
			GG_Log(be.getMessage().toString());
			GG_Log("The given file should have .xls extension.");
			System.out.println("The given file should have .xls extension.");
		} catch (Exception e) {
			// GG_Log(e.getMessage().toString());
			e.printStackTrace();
		}
		// fs.close();
		return y;
	}

}
