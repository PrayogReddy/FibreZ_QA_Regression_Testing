package TestCases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import Utilities.AppUtils;
import Utilities.ExcelUtils;
import Utilities.ExtentTestNGListener;

@Listeners(ExtentTestNGListener.class)
public class TC016_MsoCreatePricing extends AppUtils {

    @Test
    public void msoPricingPageTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: msoPricingPageTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform MSO login
        msoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "MSO login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 22;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Navigate to Pricing Page
        driver.findElement(By.xpath("//span[contains(text(),'▼')]")).click();
        driver.findElement(By.xpath("//span[normalize-space()='Pricing']")).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath("//button[normalize-space()='Create Pricing']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to Create Pricing Page");

        // Verify Create Pricing Page is visible
        WebElement createPricingPage = driver.findElement(By.xpath("//div[contains(text(),'Select Service')]"));
        boolean isCreatePricingPageVisible = createPricingPage.getText().toLowerCase().equals("select service");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking visibility of Create Pricing Page");
        System.out.println("Create Pricing page visibility: " + isCreatePricingPageVisible);
        System.out.println("Text found: " + createPricingPage.getText());
        Assert.assertTrue(isCreatePricingPageVisible, "Create Pricing Page was not visible");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Create Pricing Page is visible");

        // Verify Pricing Page is visible after clicking Go back button
        driver.findElement(By.xpath("//button[normalize-space()='Go Back']")).click();
        Thread.sleep(3000);
        WebElement pricingPage = driver.findElement(By.xpath("//p[contains(@class, 'text-base') and contains(text(), 'List of all Pricing')]"));
        boolean isPricingPageVisible = pricingPage.getText().toLowerCase().equals("list of all pricing");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking visibility of Pricing Page after going back");
        System.out.println("Pricing page visibility: " + isPricingPageVisible);
        System.out.println("Text found: " + pricingPage.getText());
        Assert.assertTrue(isPricingPageVisible, "Pricing Page was not visible");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Pricing Page is visible after going back");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isCreatePricingPageVisible && isPricingPageVisible) {
            // Update result as 'Pass' in the Excel sheet
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);

            // Log timestamp of the test execution
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test results updated in Excel with timestamp: " + formattedNow);
        }
    }
}
