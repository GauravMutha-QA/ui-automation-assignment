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

public class LogInHelper {
	private WebDriver driver;
	
	//localizing the driver passed onto here
	public LogInHelper(WebDriver driver) {
		this.driver=driver;
	}

	//the method returns success if no exception occurs
	public String logIn(String email,String password) {
		
		//filling login form
		WebElement emailElement = driver.findElement(By.xpath("/html//input[@id='email']"));
		emailElement.sendKeys(email);
		sleep(2);
		
		WebElement passwordElement = driver.findElement(By.xpath("/html//input[@id='pass']"));
		passwordElement.sendKeys(password);
		sleep(2);
		
		WebElement logInButton = driver.findElement(By.xpath("/html//form[@id='login-form']/fieldset[@class='fieldset login']//button[@name='send']/span[.='Sign In']"));
		logInButton.click();

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".header.panel > .header.links  span[role='button'] > .action.switch")));
			
			WebElement dropdownMenu=driver.findElement(By.cssSelector(".header.panel > .header.links  span[role='button'] > .action.switch"));
			dropdownMenu.click();
			
			
		    sleep(2); // Add a short sleep to allow the dropdown to open (consider using WebDriverWait instead)

		    // Check for the presence of "My Account" link
		    WebElement myAccountLink = driver.findElement(By.cssSelector(".header.panel > .header.links  .customer-menu > .header.links > li:nth-of-type(1) > a"));
		    Assert.assertTrue(myAccountLink.isDisplayed(), "My Account link is not displayed");
		    
		    sleep(2);
		    return "SUCCESS";
		} catch (Exception e) {
			  e.printStackTrace();			
			  return  "FAILURE";
		}
		
	}
	
	//sleep method
	private void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
}
