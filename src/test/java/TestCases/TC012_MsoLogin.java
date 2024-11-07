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
public class TC012_MsoLogin extends AppUtils {

    @Test
    public void msoLoginTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: msoLoginTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform MSO login
        msoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "MSO login performed");

        // Define row and column numbers for test data and smoke testing results
        int testDataRowNum = 3;
        int testDataColNum = 3;
        int smokeTestRowNum = 18;
        int smokeTestColNum = 12;
        int testDataTimestampColNum = 4;
        int smokeTestTimestampColNum = 15;

        // Check if login is successful
        WebElement verifyLoginSuccess = driver.findElement(By.xpath("//li[@class='text-sm cursor-pointer']"));
        boolean isLoginSuccessful = verifyLoginSuccess.getText().toLowerCase().equals("logout");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking login success by verifying 'Logout' text");
        System.out.println("Is Logout is visible after logged in: " + isLoginSuccessful);
        System.out.println("Text found: " + verifyLoginSuccess.getText());
        Assert.assertTrue(isLoginSuccessful, "Login was not successful!");

        // If login is successful, update results in Excel
        if (isLoginSuccessful) {
            // Update test data Excel sheet with 'Pass' status
            ExcelUtils.setCellData(testDataFilePath, testDataSheetName2, testDataRowNum, testDataColNum, "Pass");
            ExcelUtils.fillGreenColor(testDataFilePath, testDataSheetName2, testDataRowNum, testDataColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Login successful. Test data Excel updated with 'Pass' status");

            // Update smoke testing Excel sheet with 'Pass' status
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Login successful. Smoke testing Excel updated with 'Pass' status");

            // Log timestamp of test execution
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(testDataFilePath, testDataSheetName2, testDataRowNum, testDataTimestampColNum, formattedNow);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test results updated in Excel with timestamp: " + formattedNow);
        } else {
            // Log failure in Extent Reports if validation fails
            ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Login was not successful");
        }
    }
}
