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
public class TC011_AdminLogout extends AppUtils {

    @Test
    public void adminLogoutTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: adminLogoutTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform admin login
        adminLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Admin login performed");

        // Perform logout action
        Logout();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Logout action performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 17;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Verify logout is successful by checking if 'Login' text is present
        WebElement verifyLogoutSuccess = driver.findElement(By.xpath("(//p[@class='hidden text-base text-[#666666] mr-[5px] md:inline'])[1]"));
        boolean isLogoutSuccessful = verifyLogoutSuccess.getText().toLowerCase().equals("login");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking logout success by verifying 'Login' text");
        System.out.println("Is Login is visible after logged out: " + isLogoutSuccessful);
        System.out.println("Text found: " + verifyLogoutSuccess.getText());
        Assert.assertTrue(isLogoutSuccessful, "Logout was not successful!");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isLogoutSuccessful) {
            // Update result as 'Pass' in the Excel sheet
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Logout was successful and results updated in Excel with 'Pass' status");

            // Log timestamp of the test execution
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test results updated in Excel with timestamp: " + formattedNow);
        } else {
            // Log failure in Extent Reports if validation fails
            ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Logout was not successful");
        }
    }
}
