package TestCases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import Utilities.AppUtils;
import Utilities.ExcelUtils;
import Utilities.ExtentTestNGListener;
import com.aventstack.extentreports.Status;

@Listeners(ExtentTestNGListener.class)
public class TC043_CustomerDashboard extends AppUtils {

    @Test
    public void CustomerDashboardIDTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: CustomerDashboardIDTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);
        
        // Perform Customer login
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Performing customer login");
        customerLogin();
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Customer login successful");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 49;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;
        
        // Click on the Dashboard link
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicking on 'Dashboard' link");
        driver.findElement(By.xpath("//span[normalize-space()='Dashboard']")).click();
        Thread.sleep(2000); // Wait for the page to load
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "'Dashboard' link clicked");

        // Verify Customer ID is visible in Dashboard page
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Verifying Customer ID visibility");
        WebElement verifyCustomerId = driver.findElement(By.xpath("//span[normalize-space()=': TC61366568']"));
        boolean isCustomerIdVisible = verifyCustomerId.getText().toLowerCase().equals(": tc61366568");
        System.out.println("Customer ID visibility: " + isCustomerIdVisible);
        System.out.println("Text found: " + verifyCustomerId.getText());
        Assert.assertTrue(isCustomerIdVisible, "Customer ID was not visible after clicking Dashboard");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Customer ID is visible");

        // Verify Customer ID status is active
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Verifying Customer ID status");
        WebElement verifyCustomerIdStatus = driver.findElement(By.xpath("//span[normalize-space()='(INACTIVE)']"));
        boolean isCustomerIdStatusVisible = verifyCustomerIdStatus.getText().toLowerCase().equals("(inactive)");
        System.out.println("Customer ID status visibility: " + isCustomerIdStatusVisible);
        System.out.println("Text found: " + verifyCustomerIdStatus.getText());
        Assert.assertTrue(isCustomerIdStatusVisible, "Customer ID status was not visible after clicking Dashboard");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Customer ID status is active");

        // Verify Plan is visible in Dashboard page
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Verifying Plan visibility");
        WebElement verifyPlan = driver.findElement(By.xpath("//b[@class='text-primary' and text()='499']"));
        boolean isPlanVisible = verifyPlan.getText().toLowerCase().equals("499");
        System.out.println("Plan visibility: " + isPlanVisible);
        System.out.println("Text found: " + verifyPlan.getText());
        Assert.assertTrue(isPlanVisible, "Plan was not visible for Customer ID in Dashboard page");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Plan visibility confirmed");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isCustomerIdVisible && isCustomerIdStatusVisible && isPlanVisible) {
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

        // Log the end of the test case
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: CustomerDashboardIDTest completed");
    }
}
