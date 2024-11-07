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
public class TC046_CustomerSupport extends AppUtils {

    @Test
    public void CustomerSupportTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: CustomerSupportTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);
        
        // Perform Customer login
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Performing customer login");
        customerLogin();
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Customer login successful");
        
        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 52;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;
        
        // Click on the Support link
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicking on 'Support' link");
        driver.findElement(By.xpath("//span[normalize-space()='Support']")).click();
        Thread.sleep(2000); // Wait for the page to load
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "'Support' link clicked");

        // Verify New Requests is visible in Support page
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Verifying New Requests button visibility");
        WebElement verifyNewRequest = driver.findElement(By.xpath("//button[normalize-space()='New requests']"));
        boolean isNewRequestVisible = verifyNewRequest.getText().toLowerCase().equals("new requests");
        System.out.println("New Request button visibility: " + isNewRequestVisible);
        System.out.println("Text found: " + verifyNewRequest.getText());
        Assert.assertTrue(isNewRequestVisible, "New Requests button was not visible in Support page");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "New Requests button is visible");

        // Verify Create New Request screen is visible after clicking New requests
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicking on New Requests button");
        driver.findElement(By.xpath("//button[normalize-space()='New requests']")).click();
        WebElement verifyCreateNewRequest = driver.findElement(By.xpath("//h5[normalize-space()='Create new request']"));
        boolean isCreateNewRequestVisible = verifyCreateNewRequest.getText().toLowerCase().equals("create new request");
        System.out.println("Create New Request screen visibility: " + isCreateNewRequestVisible);
        System.out.println("Text found: " + verifyCreateNewRequest.getText());
        Assert.assertTrue(isCreateNewRequestVisible, "Create New Request screen was not visible after clicking New requests button");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Create New Request screen is visible");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isNewRequestVisible && isCreateNewRequestVisible) {
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
        driver.findElement(By.xpath("//button[normalize-space()='Cancel']")).click();
        Thread.sleep(1000);

        // Log the end of the test case
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: CustomerSupportTest completed");
    }
}
