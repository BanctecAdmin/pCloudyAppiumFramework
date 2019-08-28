package pcloudy.testng;

import io.appium.java_client.android.AndroidDriver;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.exela.actions.ActionsCollection;
import com.exela.utils.Xls_Read;
import com.ssts.util.reporting.ExecutionResult;
import com.ssts.util.reporting.SingleRunReport;

public class DriverExecution extends TestSetUp {

	public Xls_Read suiteXLS; // common suite
	public static Properties CONFIG;
	public String scenariosID;
	public static String OType;
	public ActionsCollection actions;
	public Method method[];

	
	public DriverExecution() throws NoSuchMethodException, SecurityException
    {
    	try
    	{
    		this.actions = new ActionsCollection();
            this.method = actions.getClass().getMethods();
    	}
    	catch(Exception e)
    	{
    		
    	}
    }

	@Parameters({ "myDeviceContext" })
	@Test
	public void test1(String myDeviceContext) throws Exception 
	{
		DeviceContext myContext = Controller.allDeviceContexts.get(myDeviceContext);
		SingleRunReport report = myContext.report;
		AndroidDriver<WebElement> driver = myContext.driver;

		//System.out.println("Method test1 is called inside frameworkcalling class");
		try {

			report.beginTestcase("TestCase test1 :" + getClass().getName());
			report.addComment("--- Add your Test Scripts over here ---");

			FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+ "\\PropertiesFile\\config.properties");
			CONFIG = new Properties();
			CONFIG.load(fs);
			Path src = Paths.get(System.getProperty("user.dir") + "\\"+ "Suite\\" + CONFIG.getProperty("TestSuiteFile"));
			this.suiteXLS = new Xls_Read(src.toString());

			String scenariosID, RunMode, TCName;
			int rowCount = 0;

			rowCount = suiteXLS.getRowCount("TestSuite");
			//System.out.println("The rows count is " + rowCount);

			for (int i = 0; i < rowCount; i++) 
			{
				RunMode = suiteXLS.getCellData("TestSuite", "RunMode", i);
				if (RunMode.equalsIgnoreCase("Yes")) 
				{
					//System.out.println("Run Mode is matched");
					scenariosID = suiteXLS.getCellData("TestSuite", "TCID", i);
					TCName = suiteXLS.getCellData("TestSuite", "TestCaseName",i);
					Execution(myContext, report, driver, TCName, scenariosID);
				}
			}


		} catch (Exception e) {
			report.addStep("Exception Occured", null, e.getMessage(),takeScreenShot(myContext), ExecutionResult.Fail);
			throw e;
		}
	}

	public void Execution(DeviceContext myContext, SingleRunReport report,AndroidDriver<WebElement> driver, String TCName, String SCID)throws Exception 
	{
		String TCID;
		int datarowcount;
		//System.out.println("I am inside the Execution method & the test case name is "+ TCName);
		datarowcount = suiteXLS.getRowCount(TCName);
		//System.out.println("The value of data row count is : " + datarowcount);
		for (int i = 3; i <= datarowcount; i++) {
			TCID = suiteXLS.getCellData2(TCName, "TCID", i);
			//System.out.println("The value of TCID for row number " + i+ " is : " + TCID);
			//System.out.println("The value of SCID is : " + SCID);
			if (TCID.equalsIgnoreCase(SCID)) 
			{
				//System.out.println("Calling the getData method");
				getData(myContext, report, i, TCName, driver);
			}
		}
		// TData = suite.getCellData(sheetName, colNum, rowNum);
	}

	public Object[] getData(DeviceContext myContext, SingleRunReport report,int rowNumber, String TCName, AndroidDriver<WebElement> driver) throws Exception {
		
		DriverExecution aa = new DriverExecution();
		List<String> HeaderList=new ArrayList<String>();
		List<String> ValueList=new ArrayList<String>();
		List<String> ActionList=new ArrayList<String>();
		//System.out.println("I am inside the getData method");
		Row rwf=suiteXLS.getSheet(TCName).getRow(1);
		//System.out.println("rwf value = "+rwf);
		for(int k = rowNumber;k<=suiteXLS.getRowCount(TCName);k++)
		{
			//System.out.println("I am inside the first For Loop");
			String runMode = suiteXLS.getCellData2(TCName, "RunMode", k);
			if(runMode.equalsIgnoreCase("Yes"))
			{
				Row row=suiteXLS.getSheet(TCName).getRow(k-1);
				Row row2=suiteXLS.getSheet(TCName).getRow(0);
				for(int c=4;c<suiteXLS.getcolumnIndex(TCName, "Result");c++)
				{
					//System.out.println("I am inside the second For Loop");
					String Header=rwf.getCell(c).toString().trim();
					//System.out.println("Header : "+Header);
					String Value=row.getCell(c).toString().trim();
					//System.out.println("Value : "+Value);
					String actions = row2.getCell(c).toString().trim();
					//System.out.println("actions : "+actions);
					if(!(Value.isEmpty() || Value==null || Value.equalsIgnoreCase("")) )
					{
						ActionList.add(actions);
						HeaderList.add(Header);
						ValueList.add(Value);
					}
				}
			}
			//System.out.println("Calling the runAction method");
			aa.runAction(myContext, report, driver, ActionList, HeaderList,
					ValueList, rowNumber, TCName);
			break;
		}
		
		return new Object[]{HeaderList, ValueList, ActionList};
	}
		
		
		

	public void runAction(DeviceContext myContext, SingleRunReport report,AndroidDriver<WebElement> driver, List<String> actionList,List<String> headerList, List<String> valueList, int rowNumber,String tcName) throws Exception 
	{
		boolean objIsExist = false;
		//ActionsCollection ac = new ActionsCollection();
		Method method[] = this.actions.getClass().getMethods();
		String obnameget = "", loc = "", objType = "";
		Xls_Read propXLS = new Xls_Read(System.getProperty("user.dir") + "\\"+ "PropertiesFile\\" + CONFIG.getProperty("ORProperties"));
		//System.out.println("I am inside the runAction method");
		breakLoop: for (int i = 0; i < headerList.size(); i++) {
			try {
				//for (int r = 2; r <= suiteXLS.getRowCount("OBJECTREPOSITORY"); r++) {
				for(int r=2; r<=propXLS.getRowCount("OBJECTREPOSITORY");r++) {
					obnameget = propXLS.getCellData("OBJECTREPOSITORY","ObjectName", r);
					if (obnameget.equalsIgnoreCase(headerList.get(i))) {
						loc = propXLS.getCellData("OBJECTREPOSITORY","ObjectProperty", r);
						objType = propXLS.getCellData("OBJECTREPOSITORY","ObjectType", r);
						OType = objType;
						//ActionsCollection.Find_Element(driver, loc, objType);
						if (headerList.get(i).equalsIgnoreCase("PlusSymbol")
								|| headerList.get(i).equalsIgnoreCase("OpenDocument")
								|| headerList.get(i).equalsIgnoreCase("OpenDocReadMode")
								|| headerList.get(i).equalsIgnoreCase("LoginPopUp")
								|| headerList.get(i).equalsIgnoreCase("UserName")
								|| headerList.get(i).equalsIgnoreCase("Password")
								|| headerList.get(i).equalsIgnoreCase("LoginButton")
								|| headerList.get(i).equalsIgnoreCase("ContentPage")
								|| headerList.get(i).equalsIgnoreCase("DCNValue")) 
						{
							objIsExist = actions.webDriverWait(myContext,report,driver, loc,obnameget);
						}
						break;
					}
				}

				for (int j = 0; j < method.length; j++) {
					// condition to skip all steps if locator not found
					if ((headerList.get(i)).equalsIgnoreCase("PlusSymbol")
							|| headerList.get(i).equalsIgnoreCase("OpenDocument")
							|| headerList.get(i).equalsIgnoreCase("OpenDocReadMode")
							|| headerList.get(i).equalsIgnoreCase("LoginPopUp")
							|| headerList.get(i).equalsIgnoreCase("UserName")
							|| headerList.get(i).equalsIgnoreCase("Password")
							|| headerList.get(i).equalsIgnoreCase("LoginButton")
							|| headerList.get(i).equalsIgnoreCase("ContentPage")
							|| headerList.get(i).equalsIgnoreCase("DCNValue"))
					{
						if (objIsExist == true) 
						{
							if (method[j].getName().equalsIgnoreCase(actionList.get(i))) 
							{
								report.addComment("--- Add your Test Scripts over here ---");
								method[j].invoke(actions,myContext, report, driver,valueList.get(i), loc, obnameget);
								report.addComment("End of TestCase # ");
								break;
							}
						} 
						else 
						{
							Thread.sleep(1000);
							break breakLoop;
						}
					} else {
						if (method[j].getName().equalsIgnoreCase(actionList.get(i))) 
						{
							report.addComment("--- Add your Test Scripts over here ---");
							method[j].invoke(actions,myContext, report, driver,valueList.get(i), loc, obnameget);
							report.addComment("End of TestCase # ");
							break;
						}
					}
				}
			} catch (Exception e) {
				System.out.println("text");
				 report.addStep("End of TestCase # ", null, null, takeScreenShot(myContext), ExecutionResult.Fail);
				e.printStackTrace();
			}
		}
	}
}

