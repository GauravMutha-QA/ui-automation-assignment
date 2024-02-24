package com.softwaretestingboard.magento;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class MultipleCheckoutTests {
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

	@Parameters({ "email", "password" })
	@Test(priority = 1, groups = { "positiveTests" })
	public void positiveMultipleCheckoutTest(String email, String password) {

		LogInHelper logInHelper = new LogInHelper(driver);
		String logInResult = logInHelper.logIn(email, password);

		Assert.assertEquals(logInResult, "SUCCESS", "Login was not successful!");

		//CLOSING THE OPENED DROPDOWN
		WebElement dropdownMenu = driver
				.findElement(By.cssSelector(".header.panel > .header.links  span[role='button'] > .action.switch"));
		dropdownMenu.click();

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


		
		WebElement quantityInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("qty")));

		quantityInput.clear();
		quantityInput.sendKeys("3");


		// Click the add to add to cart button
		WebElement addToCartButtonOnItemPage = wait
				.until(ExpectedConditions.elementToBeClickable(By.id("product-addtocart-button")));
		addToCartButtonOnItemPage.click();

		//click shopping cart link
		WebElement cartLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//main[@id='maincontent']//div[@role='alert']/div//a")));
		cartLink.click();

		// Click the multiple checkout button
		WebElement multipleCheckoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"//main[@id='maincontent']//div[@class='cart-container']/div[1]/ul//a[@href='https://magento.softwaretestingboard.com/multishipping/checkout/']/span[.='Check Out with Multiple Addresses']")));
		multipleCheckoutButton.click();
		
		//add new address
		WebElement enterAddress=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//form[@id='checkout_multishipping_form']//button[2]")));
		enterAddress.click();
		
		// Fill the form fields
		WebElement firstNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='firstname']")));
		firstNameField.sendKeys("John");

		WebElement lastNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='lastname']")));
		lastNameField.sendKeys("Doe");

		WebElement telephoneField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='telephone']")));
		telephoneField.sendKeys("1234567890");

		WebElement streetAddress1Field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='form-validate']/fieldset[2]/div[1]/div[@class='control']/input[@name='street[]']")));
		streetAddress1Field.sendKeys("123 Street");

		WebElement streetAddress2Field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='form-validate']/fieldset[2]/div[1]//div[@class='nested']/div[1]/div[@class='control']/input[@name='street[]']")));
		streetAddress2Field.sendKeys("Apt 456");

		WebElement streetAddress3Field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='form-validate']/fieldset[2]/div[1]//div[@class='nested']/div[2]/div[@class='control']/input[@name='street[]']")));
		streetAddress3Field.sendKeys("Near Park");

		String regionXpath = "//select[@id='region_id']";

		
		WebElement regionDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(regionXpath)));

		
		Select regionSelect = new Select(regionDropdown);

		
		regionSelect.selectByVisibleText("Alaska");

		WebElement cityField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='city']")));
		cityField.sendKeys("New York");

		WebElement zipField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='zip']")));
		zipField.sendKeys("12345");
		
		WebElement saveAddressButton=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//form[@id='form-validate']//button")));
		saveAddressButton.click();
		
		//ADD ADDRESSES BY SELECTING IN DROPDOWN
		List<WebElement> rows = driver.findElements(By.xpath("/html//table[@id='multiship-addresses-table']/tbody/tr"));

		for (int i = 0; i < rows.size(); i++) {
			int index = i % 2;
			String dropdownXPath = String
					.format("/html//table[@id='multiship-addresses-table']/tbody/tr[%d]/td[3]/div/div/select", i + 1);
			WebElement dropdown = driver.findElement(By.xpath(dropdownXPath));

			Select addressSelect = new Select(dropdown);
			addressSelect.selectByIndex(index);
			sleep(2);
		}

		//SELECTING THE FURTHER FIELDS
		WebElement proceed1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//form[@id='checkout_multishipping_form']//button[@title='Go to Shipping Information']")));
		proceed1.click();

		WebElement radio1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//form[@id='shipping_method_form']/div[1]/div[@class='block-content']/div[@class='box box-shipping-method']//input")));
		radio1.click();

		WebElement radio2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//form[@id='shipping_method_form']/div[2]/div[@class='block-content']/div[@class='box box-shipping-method']//dl/dd[1]/fieldset[@class='fieldset']/div/div[@class='control']/input")));
		radio2.click();

		WebElement proceed2 = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//form[@id='shipping_method_form']//button[@type='submit']")));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", proceed2);

		WebElement proceed3 = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='payment-continue']")));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", proceed3);

		WebElement proceed4 = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='review-button']")));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", proceed4);

		//VERIFYING SUCCESS MESSAGE
		WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"/html//main[@id='maincontent']//span[@class='base'][contains(., 'Thank you for your purchase!')]")));
		String expectedSuccessMessage = "Thank you for your purchase!";
		System.out.println(successMessage.getText() + " " + expectedSuccessMessage);
		Assert.assertTrue(successMessage.getText().contains(expectedSuccessMessage), "Success message not as expected");

	}
	
	//SLEEP

	private void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//CLOSE BROWSER
	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		driver.close();
		System.out.println("Test Finished");
	}
}
