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

public class WishlistTests {
	private WebDriver driver;

	//SELECTING BROWSER AND OPENING PAGE
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

	
	//NEGATIVE TEST
	@Parameters({ "email", "password" })
	@Test(priority = 1, groups = { "positiveTests" })
	public void positiveWishlistTest(String email, String password) {

		//LOGGING IN
		LogInHelper logInHelper = new LogInHelper(driver);
		String logInResult = logInHelper.logIn(email, password);

		//CHECKING IF LOGIN ITSELF IS SUCCESSFUL OR NOT
		Assert.assertEquals(logInResult,"SUCCESS", "Login was not successful!");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Click 'MEN' link
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector("li:nth-of-type(3) > a[role='menuitem'] > span:nth-of-type(2)")))
				.click();

		// Click JACKETS link
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector(".categories-menu > ul:nth-of-type(1) > li:nth-of-type(2) > a")))
				.click();


		// Assume you are on the Jackets page with a list of items

		// Click on the item card
		WebElement itemCard = wait
				.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li:nth-of-type(1) > .product-item-info .product-item-link")));
		itemCard.click();

		// Get the header name of item card
		String itemNameOnItemPage = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html//main[@id='maincontent']/div[@class='columns']//span[@class='base']")))
				.getText();
		
		// Check the URL of the item page
		String expectedItemPageUrl = "https://magento.softwaretestingboard.com/" + makeUrlFriendly(itemNameOnItemPage)
				+ ".html";
		Assert.assertEquals(driver.getCurrentUrl(), expectedItemPageUrl, "URL of the item page is not as expected");

		// Click the add to wishlist icon
		WebElement addToWishlistButtonOnItemPage = wait
				.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".product-addto-links > .action.towishlist > span")));
		addToWishlistButtonOnItemPage.click();



		// Check the success message for the item name
		WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']//div//div")));
		String expectedMessage = itemNameOnItemPage + " has been added to your Wish List.";
		String actualMessage = successMessage.getText();
		Assert.assertTrue(actualMessage.contains(expectedMessage),
		        "Actual success message does not contain expected message: " + 
		        "\nExpected Message: " + expectedMessage + 
		        "\nActual Message: " + actualMessage);
		

	}

	// to convert item name to url
	private String makeUrlFriendly(String itemName) {
		return itemName.toLowerCase().replaceAll("[^a-zA-Z0-9-]", "-");
	}

	//close the browser
	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		driver.close();
		System.out.println("Test Finished");
	}

}
