package com.softwaretestingboard.magento;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CheckoutTests {
	private WebDriver driver;

	//OPRNING THE BROWSER AND WEBSITE
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
	public void positiveCheckoutTest(String email, String password) {

		//USING LOGIN UTIL
		LogInHelper logInHelper = new LogInHelper(driver);
		String logInResult = logInHelper.logIn(email, password);

		Assert.assertEquals(logInResult, "SUCCESS", "Login was not successful!");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		// Click Gear
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector("li:nth-of-type(4) > a[role='menuitem'] > span:nth-of-type(2)"))).click();

		// Click Bags
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
				".categories-menu [href='https\\:\\/\\/magento\\.softwaretestingboard\\.com\\/gear\\/bags\\.html']")))
				.click();

		

		// Click on the item
		WebElement itemCard = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("/html//main[@id='maincontent']//div[@class='column main']/div[4]/ol/li[2]")));
		itemCard.click();

		// Get the name of the item
		String itemNameOnItemPage = wait
				.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("/html//main[@id='maincontent']/div[@class='columns']//h1[@class='page-title']")))
				.getText();

		System.out.println(itemNameOnItemPage);
		
		
		
		//SENDING ARBITRARY VALYUE FOR QUANTITY - 3
		WebElement quantityInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("qty")));


		quantityInput.clear();
		quantityInput.sendKeys("3");

		
		// Click the add to add to cart button
		WebElement addToCartButtonOnItemPage = wait
				.until(ExpectedConditions.elementToBeClickable(By.id("product-addtocart-button")));
		addToCartButtonOnItemPage.click();

		//CLCIKING CART LINK
		WebElement cartLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//div[@role='alert']//div[@class='message-success success message']/div/a")));

		cartLink.click();
		
		
		//EXPANDING THE TAX AND ZIP DIV IF NEEDED
		WebElement blockShippingDiv = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("block-shipping")));

		// Check if the block-shipping div has the class 'active'
		if (!blockShippingDiv.getAttribute("class").contains("active")) {
			blockShippingDiv.click();
		}
		
		//SELECTING COUNTRY FROM DROPDOWN
		String countryDropdownXPath = "//form[@id='shipping-zip-form']//div[@name='shippingAddress.country_id']//select[@name='country_id']";

		// Locate the dropdown element using the XPath
		WebElement countryDropdown = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(countryDropdownXPath)));

		// Create a Select object
		Select countrySelect = new Select(countryDropdown);

		// Select the option
		countrySelect.selectByVisibleText("India");
		
		
		//SELECTING THE REGION SIMILARLY
		String regionXpath = "//form[@id='shipping-zip-form']//div[@name='shippingAddress.region_id']//select[@name='region_id']";

		// Locate the dropdown element using the XPath
		WebElement regionDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(regionXpath)));

		// Create a Select object
		Select regionSelect = new Select(regionDropdown);

		// Select the option
		regionSelect.selectByVisibleText("Rajasthan");

		
		//ENETRING ZIP CODE ARBITRARILY
		WebElement zipCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//form[@id='shipping-zip-form']//div[@name='shippingAddress.postcode']//input[@name='postcode']")));

		// Clear the existing value (if any) and enter the desired quantity (e.g., 2)
		zipCode.clear();
		zipCode.sendKeys("43210");

		//SELECTING RADIO BUTTON FOR SELECTING TAX RATE
		WebElement radioButton1 = wait.until(
				ExpectedConditions.elementToBeClickable(By.xpath("/html//input[@id='s_method_flatrate_flatrate']")));
		radioButton1.click();

		// Get the total cost of the item
		String totalCost = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//div[@id='cart-totals']/div[@class='table-wrapper']/table[@class='data table totals']//tr[@class='grand totals']/td/strong/span")))
				.getText();

		

		WebElement checkoutButton1 = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//main[@id='maincontent']//div[@class='cart-container']/div[1]/ul//button")));
		checkoutButton1.click();
		// Fill the form for country, state, zip code IF THERE IS NO SAVED ADDRESS
		try {
			WebElement address1 = wait
					.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[name='street[0]']")));
			address1.sendKeys("India");

			WebElement address2 = wait
					.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[name='street[1]']")));
			address2.sendKeys("Rajasthan");

			WebElement address3 = wait
					.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[name='street[2]']")));
			address3.sendKeys("43210");

			WebElement city = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"//div[@id='shipping-new-address-form']/div[@name='shippingAddress.city']//input[@name='city']")));
			city.sendKeys("blue");

			WebElement phone = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"//div[@id='shipping-new-address-form']/div[@name='shippingAddress.telephone']//input[@name='telephone']")));
			phone.sendKeys("123456");
		} catch (Exception e) {
			System.out.println("There is a saved address");
		} finally {
			
			//IF THERE IS SAVED ADDRESS WE PROCEED TO FILL OTHER FIELDS
			WebElement checkoutButton2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[@id='shipping-method-buttons-container']//button[@type='submit']")));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkoutButton2);

			WebElement checkoutButton3 = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.cssSelector("button[title='Place Order'] > span")));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkoutButton3);

			// Verify success message
			WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
					"/html//main[@id='maincontent']//span[@class='base'][contains(., 'Thank you for your purchase!')]")));
			String expectedSuccessMessage = "Thank you for your purchase!";
			System.out.println(successMessage.getText() + " " + expectedSuccessMessage);
			Assert.assertTrue(successMessage.getText().contains(expectedSuccessMessage),
					"Success message not as expected");

		}
	}


	//CLOSING BROWSER

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		driver.close();
		System.out.println("Test Finished");
	}

}
