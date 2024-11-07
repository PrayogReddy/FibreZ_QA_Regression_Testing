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
public class TC042_CustomerLogin extends AppUtils {

    @Test
    public void customerLoginTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: customerLoginTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);
        
        // Perform the customer login action
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Performing customer login");
        customerLogin();
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Customer login action performed");

        // Define row and column numbers for test data and smoke testing results
        int testDataRowNum = 9;
        int testDataColNum = 3;
        int smokeTestRowNum = 48;
        int smokeTestColNum = 12;
        int testDataTimestampColNum = 4;
        int smokeTestTimestampColNum = 15;
        
        // Check if login is successful by verifying presence of "logout" text
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Verifying login success by checking for 'logout' text");
        WebElement verifyLoginSuccess = driver.findElement(By.xpath("//li[@class='text-sm cursor-pointer']"));
        boolean isLoginSuccessful = verifyLoginSuccess.getText().toLowerCase().equals("logout");
        Assert.assertTrue(isLoginSuccessful, "Login was not successful!");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Login was successful");

        if (isLoginSuccessful) {
            // Update the test data Excel file to mark the test as passed
            ExcelUtils.setCellData(testDataFilePath, testDataSheetName2, testDataRowNum, testDataColNum, "Pass");
            ExcelUtils.fillGreenColor(testDataFilePath, testDataSheetName2, testDataRowNum, testDataColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Test data Excel file updated with 'Pass' status");

            // Update the smoke testing Excel file to mark the test as passed
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Smoke testing Excel file updated with 'Pass' status");

            // Record the timestamp of the test execution in the Excel files
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(testDataFilePath, testDataSheetName2, testDataRowNum, testDataTimestampColNum, formattedNow);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Recorded timestamp in both Excel files: " + formattedNow);
        }

        // Log the end of the test case
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: customerLoginTest completed");
    }
}
