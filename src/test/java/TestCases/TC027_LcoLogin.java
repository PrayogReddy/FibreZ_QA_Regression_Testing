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
public class TC027_LcoLogin extends AppUtils {

    @Test
    public void LcoLoginTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: LcoLoginTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform LCO login
        lcoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "LCO login performed");

        // Define row and column numbers for test data and smoke testing results
        int testDataRowNum = 5;
        int testDataColNum = 3;
        int smokeTestRowNum = 33;
        int smokeTestColNum = 12;
        int testDataTimestampColNum = 4;
        int smokeTestTimestampColNum = 15;

        // Check if login is successful
        WebElement verifyLoginSuccess = driver.findElement(By.xpath("//li[@class='text-sm cursor-pointer']"));
        boolean isLoginSuccessful = verifyLoginSuccess.getText().toLowerCase().equals("logout");
        Assert.assertTrue(isLoginSuccessful, "Login was not successful!");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Login successful: 'Logout' text is visible");

        // If login is successful, update results in Excel
        if (isLoginSuccessful) {
            // Update test data Excel sheet with 'Pass' status
            ExcelUtils.setCellData(testDataFilePath, testDataSheetName2, testDataRowNum, testDataColNum, "Pass");
            ExcelUtils.fillGreenColor(testDataFilePath, testDataSheetName2, testDataRowNum, testDataColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Test data updated with 'Pass' status in Excel");

            // Update smoke testing Excel sheet with 'Pass' status
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Smoke test results updated with 'Pass' status in Excel");

            // Log timestamp of test execution
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(testDataFilePath, testDataSheetName2, testDataRowNum, testDataTimestampColNum, formattedNow);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test execution timestamp updated in Excel: " + formattedNow);
        } else {
            ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Login verification failed");
        }
    }
}
