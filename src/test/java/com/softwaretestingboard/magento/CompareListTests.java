package com.softwaretestingboard.magento;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CompareListTests {
	private WebDriver driver;

	//selecting browser and opening the website
	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	private void setUp(@Optional("chrome") String browser) {
		// create driver
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
	@Parameters({ "email", "password" })
	@Test(priority = 1, groups = { "positiveTests" })
	public void positiveCompareListTest(String email, String password) {

		//USING LOGIN UTIL
		LogInHelper logInHelper = new LogInHelper(driver);
		String logInResult = logInHelper.logIn(email, password);

		Assert.assertEquals(logInResult,"SUCCESS", "Login was not successful!");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Click MEN
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector("li:nth-of-type(3) > a[role='menuitem'] > span:nth-of-type(2)")))
				.click();

		// Click JACKETS
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector(".categories-menu > ul:nth-of-type(1) > li:nth-of-type(2) > a")))
				.click();
		
		//USING ACTIONS  OBJECT TO PERFORM HOVER ON ITEM CARD
		Actions actions = new Actions(driver);
		
		//SELECTING FIRST ITEM
		WebElement firstItemCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//main[@id='maincontent']//div[@class='column main']/div[4]/ol/li[1]/div[@class='product-item-info']")));
		actions.moveToElement(firstItemCard).perform();
		WebElement firstItemElement=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html//main[@id='maincontent']//div[@class='column main']/div[4]/ol/li[1]/div[@class='product-item-info']//strong/a")));
		String firstItemLink=firstItemElement.getAttribute("href");
		String firstItemName=firstItemElement.getText();
		WebElement compareButton1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html//main[@id='maincontent']/div[@class='columns']/div[@class='column main']/div[4]/ol/li[1]/div[@class='product-item-info']//div[@class='actions-secondary']/a[2]")));
		compareButton1.click();
		
		//VERIFYING SUCCESS MESSAGE FOR FIRST ITEM
		WebElement successMessage1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']//div[@class='message-success success message']")));
		String expectedMessage1 = "You added product " + firstItemName + " to the comparison list.";
		String actualMessage1 = (String)((JavascriptExecutor)driver).executeScript("return arguments[0].innerText", successMessage1);
		Assert.assertEquals(actualMessage1, expectedMessage1, "Actual success message does not contain expected message");

		//SELECTING SECOND MESSAGE
		WebElement secondItemCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//main[@id='maincontent']//div[@class='column main']/div[4]/ol/li[2]/div[@class='product-item-info']")));
		actions.moveToElement(secondItemCard).perform();
		WebElement secondItemElement=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html//main[@id='maincontent']//div[@class='column main']/div[4]/ol/li[2]/div[@class='product-item-info']//strong/a")));
		String secondItemLink=secondItemElement.getAttribute("href");
		String secondItemName=secondItemElement.getText();
		WebElement compareButton2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html//main[@id='maincontent']/div[@class='columns']/div[@class='column main']/div[4]/ol/li[2]/div[@class='product-item-info']//div[@class='actions-secondary']/a[2]")));
		compareButton2.click();
		
		//VERIFYING SUCCESS MESSSAGE
		WebElement successMessage2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']//div[@class='message-success success message']")));
		String expectedMessage2 = "You added product " + secondItemName + " to the comparison list.";
		String actualMessage2 = (String)((JavascriptExecutor)driver).executeScript("return arguments[0].innerText", successMessage2);
		Assert.assertEquals(actualMessage2, expectedMessage2, "Actual success message does not contain expected message");
		
		WebElement compareListLink = driver.findElement(By.xpath("//div[@role='alert']//div[@class='message-success success message']/div/a"));
		compareListLink.click();

		// Check the presence of the first link string
		boolean firstLinkPresent = !driver.findElements(By.xpath("/html//table[@id='product-comparison']//tbody[1]//tr[1]//td/a[contains(@href, '" + firstItemLink + "')]")).isEmpty();
		
		// Check the presence of the second link string
		boolean secondLinkPresent = !driver.findElements(By.xpath("/html//table[@id='product-comparison']//tbody[1]//tr[1]//td/a[contains(@href, '" + secondItemLink + "')]")).isEmpty();
		
		//ASSERTIONS IF ITEM LNKS ARE PRESENT IN COMPARE LIST
		Assert.assertTrue(firstLinkPresent, "First item link not found in the comparison list");
		Assert.assertTrue(secondLinkPresent, "Second item link not found in the comparison list");
	}

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		driver.close();
		System.out.println("Test Finished");
	}

}
