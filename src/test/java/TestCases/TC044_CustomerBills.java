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
public class TC044_CustomerBills extends AppUtils {

    @Test
    public void CustomerBillsPageTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: CustomerBillsPageTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);
        
        // Perform Customer login
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Performing customer login");
        customerLogin();
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Customer login successful");
        
        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 50;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;
        
        // Click on the Bills link
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicking on 'Bills' link");
        driver.findElement(By.xpath("//span[normalize-space()='Bills']")).click();
        Thread.sleep(2000); // Wait for the page to load
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "'Bills' link clicked");

        // Verify Pay Bills is visible in Bills page
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Verifying 'Pay Bills' visibility");
        WebElement verifyPayBills = driver.findElement(By.xpath("//button[normalize-space()='Pay Bills']"));
        boolean isPayBillsVisible = verifyPayBills.getText().toLowerCase().equals("pay bills");
        System.out.println("Pay Bills visibility: " + isPayBillsVisible);
        System.out.println("Text found: " + verifyPayBills.getText());
        Assert.assertTrue(isPayBillsVisible, "Pay Bills tab was not visible after clicking Bills");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "'Pay Bills' tab is visible");

        // Verify Annual Summary is visible in Bills page
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Verifying 'Annual Summary' visibility");
        WebElement verifyAnnualSummary = driver.findElement(By.xpath("//button[normalize-space()='Annual Summary']"));
        boolean isAnnualSummaryVisible = verifyAnnualSummary.getText().toLowerCase().equals("annual summary");
        System.out.println("Annual Summary visibility: " + isAnnualSummaryVisible);
        System.out.println("Text found: " + verifyAnnualSummary.getText());
        Assert.assertTrue(isAnnualSummaryVisible, "Annual Summary tab was not visible after clicking Bills");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "'Annual Summary' tab is visible");

        // Verify Pay Now is visible in Pay Bills tab
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Verifying 'Pay Now' button visibility");
        WebElement verifyPayNow = driver.findElement(By.xpath("//button[normalize-space()='Pay Now']"));
        boolean isPayNowVisible = verifyPayNow.getText().toLowerCase().equals("pay now");
        System.out.println("Pay Now visibility: " + isPayNowVisible);
        System.out.println("Text found: " + verifyPayNow.getText());
        Assert.assertTrue(isPayNowVisible, "Pay Now button is not visible in Bills page to proceed with Payment options");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "'Pay Now' button is visible");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isPayBillsVisible && isAnnualSummaryVisible && isPayNowVisible) {
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

        // Close pop-up window
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Closing pop-up window");
        driver.findElement(By.xpath("//span[@class='absolute -inset-2.5']")).click();
        Thread.sleep(2000);
        
        // Save and Exit
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Saving and exiting");
        driver.findElement(By.xpath("//button[normalize-space()='Yes & Exit']")).click();        
        driver.findElement(By.xpath("//button[normalize-space()='Cancel']")).click();
        
        // Log the end of the test case
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: CustomerBillsPageTest completed");
    }
}
