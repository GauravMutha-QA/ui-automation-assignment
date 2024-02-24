package com.softwaretestingboard.magento;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SignUpTests {
	private WebDriver driver;

	//DECIDES THE BROWSER AND URL
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
		String url = "https://magento.softwaretestingboard.com/customer/account/create/";
		driver.get(url);

		driver.manage().window().maximize();

		System.out.println("Browser opened");

	}
	
	//TEST METHOD - POSITIVE
	
	@Parameters({ "firstname", "lastname", "email" ,"password","confirmPassword"})
	@Test(priority = 1, groups = { "positiveTests"})
	public void positiveSignUpTest(String firstname, String lastname, String email,String password,String confirmPassword) {

		//SETTING FIELDS
		WebElement firstNameElement = driver.findElement(By.xpath("/html//input[@id='firstname']"));
		firstNameElement.sendKeys(firstname);
		sleep(2);
		WebElement lastNameElement = driver.findElement(By.xpath("/html//input[@id='lastname']"));
		lastNameElement.sendKeys(lastname);
		sleep(2);
		WebElement emailElement = driver.findElement(By.xpath("/html//input[@id='email_address']"));
		emailElement.sendKeys(email);
		sleep(2);
		WebElement passwordElement = driver.findElement(By.xpath("/html//input[@id='password']"));
		passwordElement.sendKeys(password);
		sleep(2);
		WebElement confirmPasswordElement = driver.findElement(By.xpath("/html//input[@id='password-confirmation']"));
		confirmPasswordElement.sendKeys(confirmPassword);
		sleep(2);


		//CLICKS THE BUTTON
		WebElement createAccountButton = driver.findElement(By.xpath("//form[@id='form-validate']//button[@title='Create an Account']/span[.='Create an Account']"));
		createAccountButton.click();
		sleep(2);
		
		//VERIFICATION OF URL
		String expectedUrl = "https://magento.softwaretestingboard.com/customer/account/";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl, "Actual page url is not as expected");
		sleep(2);
		
		//VERIFICATION OF MESSAGE
		WebElement successMessage = driver.findElement(By.xpath("//main[@id='maincontent']//div[@role='alert']/div/div[.='Thank you for registering with Main Website Store.']"));
		String expectedMessage = "Thank you for registering with Main Website Store.";
		String actualMessage = successMessage.getText();
		Assert.assertTrue(actualMessage.contains(expectedMessage),
				"Actual success message does not contain expected message");

		sleep(2);

	}
	
	//TEST METHOD - NEGATIVE - invalid email
	
		@Parameters({ "firstname", "lastname", "email" ,"password","confirmPassword"})
		@Test(priority = 2, groups = { "negativeTests"})
		public void negativeSignUpTest(String firstname, String lastname, String email,String password,String confirmPassword) {

			//SETTING FIELDS
			WebElement firstNameElement = driver.findElement(By.xpath("/html//input[@id='firstname']"));
			firstNameElement.sendKeys(firstname);
			sleep(2);
			WebElement lastNameElement = driver.findElement(By.xpath("/html//input[@id='lastname']"));
			lastNameElement.sendKeys(lastname);
			sleep(2);
			
			//Already registered email should give failed sign up//
			email="kanoukeiprubra-6526@yopmail.com";
			WebElement emailElement = driver.findElement(By.xpath("/html//input[@id='email_address']"));
			emailElement.sendKeys(email);
			
			
			sleep(2);
			WebElement passwordElement = driver.findElement(By.xpath("/html//input[@id='password']"));
			passwordElement.sendKeys(password);
			sleep(2);
			WebElement confirmPasswordElement = driver.findElement(By.xpath("/html//input[@id='password-confirmation']"));
			confirmPasswordElement.sendKeys(confirmPassword);
			sleep(2);


			//CLICKS THE BUTTON
			WebElement createAccountButton = driver.findElement(By.xpath("//form[@id='form-validate']//button[@title='Create an Account']/span[.='Create an Account']"));
			createAccountButton.click();
			sleep(2);
			
			//VERIFICATION OF URL
			String expectedUrl = "https://magento.softwaretestingboard.com/customer/account/create/";
			String actualUrl = driver.getCurrentUrl();
			Assert.assertEquals(actualUrl, expectedUrl, "Actual page url is not as expected");
			sleep(2);
			
			//VERIFICATION OF MESSAGE
			WebElement failMessage = driver.findElement(By.xpath("//main[@id='maincontent']//div[@role='alert']/div/div"));
			String expectedMessage = "There is already an account with this email address. If you are sure that it is your email address, click here to get your password and access your account.";
			String actualMessage = failMessage.getText();
			
			Assert.assertTrue(actualMessage.contains(expectedMessage),
					"Actual failure message does not contain expected failure message");

			sleep(2);

		}
	
	//SLEEP METHOD
	private void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//CLOSING THR BROWSER
	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		driver.close();
		System.out.println("Test Finished");
	}
	
	
}
