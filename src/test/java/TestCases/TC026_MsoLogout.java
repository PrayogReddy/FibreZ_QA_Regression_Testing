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
public class TC026_MsoLogout extends AppUtils {

    @Test
    public void msoLogoutTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: msoLogoutTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform MSO login
        msoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "MSO login performed");

        // Perform logout action
        Logout();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "MSO logout performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 32;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Verify logout is successful by checking if 'Login' text is present
        WebElement verifyLogoutSuccess = driver.findElement(By.xpath("(//p[@class='hidden text-base text-[#666666] mr-[5px] md:inline'])[1]"));
        boolean islogoutSuccessful = verifyLogoutSuccess.getText().toLowerCase().equals("login");
        Assert.assertTrue(islogoutSuccessful, "Logout was not successful!");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Logout successful: 'Login' text is visible");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (islogoutSuccessful) {
            // Update result as 'Pass' in the Excel sheet
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Test result updated in Excel with 'Pass' status");

            // Log timestamp of the test execution
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test execution timestamp updated in Excel: " + formattedNow);
        } else {
            ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Logout verification failed");
        }
    }
}
