package TestCases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import Utilities.AppUtils;
import Utilities.ExcelUtils;
import Utilities.ExtentTestNGListener;

@Listeners(ExtentTestNGListener.class)
public class TC001_LaunchApplication extends AppUtils {

    @Test
    public void checkUrlTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: checkUrlTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);
        
        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 7;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Get the current URL
        String currentUrl = driver.getCurrentUrl();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Current URL retrieved: " + currentUrl);

        // Compare the current URL to the expected URL
        boolean isUrlCorrect = currentUrl.equals(url);

        // Print details of the comparison
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Expected URL: " + url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Is URL Correct: " + isUrlCorrect);

        // Assertion to check if the URL is correct
        Assert.assertTrue(isUrlCorrect, "URL was not visible after opening application");

        // Update Excel and report based on the assertion result
        if (isUrlCorrect) {
            // Update the result to "Pass" in the Excel sheet
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "URL verification passed and result updated in Excel");
        } else {
            ExtentTestNGListener.getExtentTest().log(Status.FAIL, "URL verification failed");
        }

        // Record the timestamp of the test execution in the Excel files
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedNow = now.format(formatter);
        ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test results updated in Excel with timestamp: " + formattedNow);
    }
}
