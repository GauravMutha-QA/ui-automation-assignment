package com.softwaretestingboard.magento;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LogInTests {
	private WebDriver driver;
	
	
	//selecting browser and opening the url
	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	private void setUp(@Optional("chrome") String browser) {
		switch (browser) {
		case "chrome":
			driver = new ChromeDriver();
			break;

		case "firefox":
			driver = new FirefoxDriver();
			break;

		default:
			System.out.println("Do not know how to start " + browser + " ,starting chrome instead");
			driver = new ChromeDriver();
			break;
		}

		System.out.println("Browser started");

		// open test page
		String url = "https://magento.softwaretestingboard.com/customer/account/login/referer/aHR0cHM6Ly9tYWdlbnRvLnNvZnR3YXJldGVzdGluZ2JvYXJkLmNvbS8%2C/";
		driver.get(url);

		driver.manage().window().maximize();

		System.out.println("Browser opened");

	}
	
	//POSITIVE TEST
	@Parameters({"email" ,"password"})
@Test(priority = 1, groups = { "positiveTests"})
	public void positiveLogInTest(String email,String password) {
		// 	using loginhelp util

		LogInHelper logInHelper= new LogInHelper(driver);
		String logInResult=logInHelper.logIn(email,password);

		//assertion
		Assert.assertTrue("SUCCESS".equals(logInResult), "Login was not successful!");

		
	}
	
	//NEGATIVE TEST
	@Parameters({"email" ,"password"})
	@Test(priority = 1, groups = { "negativeTests"})
		public void negativeLogInTest(String email,String password) {
			// using login helper utils

			LogInHelper logInHelper= new LogInHelper(driver);
			
			//sending invalid email
			email="abc@whatever.com"; //non registered email
			String logInResult=logInHelper.logIn(email,password);

			//assertion
			Assert.assertTrue("FAILURE".equals(logInResult), "Login successful but it was not expected!");

			
		}
	
	//closing the browser
	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		driver.close();
		System.out.println("Test Finished");
	}
	
	
}
