package com.softwaretestingboard.magento;

import java.time.Duration;

import org.openqa.selenium.By;
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

public class EditWishlistTests {
	private WebDriver driver;

	//CHOOSING THE BROWSER AND OPENING THE WEBSITE
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
	public void positiveEditWishlistTest(String email, String password) {

		//USING LOGIN UTIL
		LogInHelper logInHelper = new LogInHelper(driver);
		String logInResult = logInHelper.logIn(email, password);

		//ASSERTION FOR SUCCESSFUL LOGIN
		Assert.assertEquals(logInResult,"SUCCESS", "Login was not successful!");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// WAITING FOR THE ITEM CARD TO APPEAR
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector(".header.panel > .header.links  .customer-menu > .header.links > .link.wishlist  .counter.qty")))
				.click();
		
		
		// Perform hover action on the first item card
		Actions actions = new Actions(driver);
		WebElement firstItemCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='wishlist-view-form']//ol[@class='product-items']/li[1]/div[1]")));
		actions.moveToElement(firstItemCard).perform();

		// Click the Edit button
		WebElement editButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-items > li:nth-of-type(1) .action.edit")));
		editButton.click();
		
		// After clicking the Edit button, you are on the item's page
		WebElement itemNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html//main[@id='maincontent']/div[@class='columns']//span[@class='base']")));

		// Get the text from the element
		String itemName = itemNameElement.getText();

		

		// Click the size catalog button
		WebElement sizeCatalogButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("option-label-size-143-item-167")));
		sizeCatalogButton.click();

		

		

		// Click the color catalog button
		WebElement colorCatalogButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='product-options-wrapper']//div[@class='swatch-opt']/div[2]/div[@role='listbox']/div[1]")));
		colorCatalogButton.click();

		

		
		// Locate the quantity input box
		WebElement quantityInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("qty")));

		// Clear the existing value and sending arbitrarily 2
		quantityInput.clear();
		quantityInput.sendKeys("2");

		
		//clicking update wishlist button
		WebElement updateWishlistButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".action.towishlist.updated > span")));
		updateWishlistButton.click();
		
		
		//assertion for success message
		WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//main[@id='maincontent']//div[@role='alert']/div/div")));

		// Expected success message
		String expectedSuccessMessage = itemName+" has been updated in your Wish List.";

		// Get the actual success message
		String actualSuccessMessage = successMessage.getText();

		// Verify if the actual message contains the expected message
		Assert.assertTrue(actualSuccessMessage.contains(expectedSuccessMessage),
		        "Actual success message does not contain expected message: " +
		        "\nExpected Message: " + expectedSuccessMessage +
		        "\nActual Message: " + actualSuccessMessage);
		

	}

//closing the browser
	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		driver.close();
		System.out.println("Test Finished");
	}

}
