package com.exela.actions;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ssts.util.reporting.ExecutionResult;
import com.ssts.util.reporting.SingleRunReport;

import io.appium.java_client.android.AndroidDriver;
import pcloudy.testng.DeviceContext;
import pcloudy.testng.DriverExecution;
import pcloudy.testng.TestSetUp;
//import pcloudy.testng.frameworkcalling;


public class ActionsCollection extends TestSetUp {

	
	public ActionsCollection() throws NoSuchElementException, SecurityException
	{
		super();
		
	}
	
	public static AndroidDriver<?> element;
	
	
	public void LaunchUrl(DeviceContext myContext,SingleRunReport report,AndroidDriver<WebElement> driver, String TData, String loc, String ObjectName)
	{
		try
		{
			JavascriptExecutor js= (JavascriptExecutor) driver;
			String url = TData;
			String script = "window.location = \'"+url+"\'";
			js.executeScript(script);
			//driver.get(tData);
			checkPageIsReady(driver);
		}		
		catch(Exception e)
		{
			//launchBrowser2(suite, objName, tData, test, browser, testSeq, nodeUrl, platformType, parentWIndow);
		}
	}
	
	 public void checkPageIsReady(AndroidDriver<WebElement> driver) {
   	  
   	  JavascriptExecutor js = (JavascriptExecutor)driver;
   	  
   	  
   	  //Initially bellow given if condition will check ready state of page.
   	  if (js.executeScript("return document.readyState").toString().equals("complete")){ 
   	   System.out.println("Page Is loaded.");
   	   return; 
   	  } 
   	  
   	  //This loop will rotate for 25 times to check If page Is ready after every 1 second.
   	  //You can replace your value with 25 If you wants to Increase or decrease wait time.
   	  for (int i=0; i<25; i++){ 
   	   try {
   	    Thread.sleep(1000);
   	    }catch (InterruptedException e) {} 
   	   //To check page ready state.
   	   if (js.executeScript("return document.readyState").toString().equals("complete")){ 
   	    break; 
   	   }   
   	  }
   	 }
	 
	 public void setText(DeviceContext myContext,SingleRunReport report,AndroidDriver<WebElement> driver, String TData, String loc, String ObjectName)
	 {
		 try {
			 String msg = "Entered data for "+ObjectName;
			 webDriverWait(myContext,report,driver, loc, ObjectName);
			 WebElement ele = driver.findElement(By.xpath(loc));
			 ele.click();
			 ele.sendKeys(TData);
			 report.addStep(msg, null, null, takeScreenShot(myContext), ExecutionResult.Pass);
			 System.out.println(msg);
		 }
		 catch(Exception e)
		 {
			 String msg = "Unable to enter data into "+ObjectName;
			 report.addStep(msg, null, null, takeScreenShot(myContext), ExecutionResult.Fail);
			 System.out.println(msg);
			 e.printStackTrace();
		 }
		 
	 }
	 
	 public void click(DeviceContext myContext,SingleRunReport report,AndroidDriver<WebElement> driver, String TData, String loc, String ObjectName)
	 {
		 try
		 {
			String msg = "Clicked on "+ObjectName;
			webDriverWait(myContext,report,driver, loc, ObjectName);
			WebElement ele = driver.findElement(By.xpath(loc));
			//ele.click();
			Actions dummy = new Actions(driver);
			dummy.moveToElement(ele);
			dummy.click(ele).build().perform();	
				
			report.addStep(msg, null, null, takeScreenShot(myContext), ExecutionResult.Pass);
			System.out.println(msg);
		 }
		 catch(Exception e)
		 {
			 String msg = "Unable to click on "+ObjectName;
			 report.addStep(msg, null, null, takeScreenShot(myContext), ExecutionResult.Fail);
			 System.out.println(msg);
			 e.printStackTrace();
		 }
	 }
	 
	 
	 public void DropDownSelect(DeviceContext myContext,SingleRunReport report,AndroidDriver<WebElement> driver, String TData, String loc, String ObjectName)
	 {
		 try {
			 String msg = "Selected a dropdown value for "+ObjectName;
			 webDriverWait(myContext,report,driver, loc, ObjectName);
			 WebElement ele = driver.findElement(By.xpath(loc));
			 ele.click();
			 List<WebElement> elem = driver.findElements(By.tagName("option"));
			 for(int x=0;x<elem.size();x++)
			 {
				 String str = elem.get(x).getText().toString().toLowerCase();
				 if(str.equalsIgnoreCase(TData.toLowerCase()))
				 {
					 elem.get(x).click();
				 }
			 }
			 //Select oSelect = new Select(driver.findElement(By.xpath(loc)));
			 //oSelect.selectByVisibleText(TData);
			 report.addStep(msg, null, null, takeScreenShot(myContext), ExecutionResult.Pass);
			 System.out.println(msg);
		 }
		 catch(Exception e)
		 {
			 String msg = "Unable to Selected a dropdown value for "+ObjectName;
			 report.addStep(msg, null, null, takeScreenShot(myContext), ExecutionResult.Fail);
			 System.out.println(msg);
			 e.printStackTrace();
		 }
		 
	 }
	 
	 
	 public  void wait(DeviceContext myContext,SingleRunReport report,AndroidDriver<WebElement> driver, String TData, String loc, String ObjectName)
	    {
			System.out.print(" Wait");
			try
	    	{
			for(int i = 1 ; i <=((Integer.parseInt(TData))) ; i++)
			{
	    		System.out.print(" .");
				Thread.sleep(500);
				System.out.print(" .");
				Thread.sleep(500);
	    	}
	    	}
	    	catch(Exception e)
	    	{
	    	}
			System.out.println();
	    }
	 
	 public boolean webDriverWait(DeviceContext myContext,SingleRunReport report,AndroidDriver<WebElement> driver, String loc, String ObjectName)
	 {
		 boolean bValue = false;
		 //String ele1 = null;
		 for(int i=0;i<2;i++)
		 {
			 try
			 {
				// System.out.println("I am waiting");
				 String OT = DriverExecution.OType;
				 
				 Thread.sleep(1500);
				 WebDriverWait wait = new WebDriverWait(driver, Integer.parseInt(DriverExecution.CONFIG.getProperty("WaitTime")));
				 
				 switch (OT.toLowerCase()) {
					case "id":
						wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(By.id(loc))));
						 wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(loc)));
						 wait.until(ExpectedConditions.presenceOfElementLocated(By.id(loc)));
						break;
						
					case "xpath":
						wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(By.xpath(loc))));
						 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(loc)));
						 wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(loc)));
						break;
						
					case "name":
						wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(By.name(loc))));
						 wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(loc)));
						 wait.until(ExpectedConditions.presenceOfElementLocated(By.name(loc)));
						break;
						
					case "css":
						wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(loc))));
						 wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(loc)));
						 wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(loc)));
						break;
						
					case "className":
						wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(By.className(loc))));
						 wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(loc)));
						 wait.until(ExpectedConditions.presenceOfElementLocated(By.className(loc)));
						break;
						
					case "linkText":
						wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(By.linkText(loc))));
						 wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(loc)));
						 wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(loc)));
						break;
						
					case "tagName":
						wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(By.tagName(loc))));
						 wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(loc)));
						 wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName(loc)));
						break;
					
					}
				 
				 
				 
			 	 bValue = true;
			 	 break;
			 }
			 
			 catch(org.openqa.selenium.NoSuchElementException e)
		 	 {
				 String msg = "Unable to Locate Element for the object: "+ObjectName;
				 report.addStep(msg, null, null, takeScreenShot(myContext), ExecutionResult.Fail);
				 System.out.println("Unable to Locate Element for the object: " + ObjectName + " : " + loc + "----> " + (i+1));
		 	 }
			 
			 catch(StaleElementReferenceException e)
		 	 {
				 String msg = "Unable to Locate Element for the object: "+ObjectName;
				 report.addStep(msg, null, null, takeScreenShot(myContext), ExecutionResult.Fail);
				 System.out.println("Unable to Locate Element for the object: " + ObjectName + " : " + loc + "----> " + (i+1)); 
		 	 }
			 
			 catch(WebDriverException e)
		 	 {
				 String msg = "Unable to Locate Element for the object: "+ObjectName;
				 report.addStep(msg, null, null, takeScreenShot(myContext), ExecutionResult.Fail);
				 System.out.println("Unable to Locate Element for the object: " + ObjectName + " : " + loc + "----> " + (i+1));
		 	 }
			 
			 catch(Exception e)
		 	 {
				 String msg = "Unable to Locate Element for the object: "+ObjectName;
				 report.addStep(msg, null, null, takeScreenShot(myContext), ExecutionResult.Fail);
				 System.out.println("Unable to Locate Element for the object: " + ObjectName + " : " + loc + "----> " + (i+1));
		 	 }
		 }
		 
		 return bValue;
	 }
	 
	 @SuppressWarnings("unchecked")
	public static AndroidDriver<WebElement> Find_Element(AndroidDriver<WebElement> driver, String Excellocator, String locatorType) throws Exception {
			//String locator = null;
			try {
				
					switch (locatorType.toLowerCase()) {
					case "id":
						element =  (AndroidDriver<?>) driver.findElementById(Excellocator);
						break;
					case "xpath":
						element = (AndroidDriver<?>) driver.findElement(By.xpath(Excellocator));
						System.out.println("xpath : "+element);
						break;
					case "name":
						element = (AndroidDriver<?>) driver.findElementByName(Excellocator);
						break;
					case "css":
						element = (AndroidDriver<?>) driver.findElementByCssSelector(Excellocator);
						break;
					case "className":
						element = (AndroidDriver<?>) driver.findElementByClassName(Excellocator);
						break;
					case "linkText":
						element = (AndroidDriver<?>) driver.findElementByLinkText(Excellocator);
						break;
					case "tagName":
						element = (AndroidDriver<?>) driver.findElementByTagName(Excellocator);
						break;
					
					}
				
				
			} catch (NullPointerException e) {
				System.out.println("It seems Locator string is not present or need to correct in or.properties " + Excellocator);
			} catch (NoSuchElementException p) {
				System.out.println("Unable to find the locator " + Excellocator);
			} catch (StaleElementReferenceException p) {
				System.out.println("State Element Reference Exception");
			} catch (Exception p) {
				System.out.println("Issue in Find Element method in find element");
				p.printStackTrace();
				
			}
			return (AndroidDriver<WebElement>) element;
		}
	    
}
