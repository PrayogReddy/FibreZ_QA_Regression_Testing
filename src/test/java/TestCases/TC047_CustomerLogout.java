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
public class TC047_CustomerLogout extends AppUtils {

    @Test
    public void customerLogoutTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: customerLogoutTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);
        
        // Perform customer login
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Performing customer login");
        customerLogin();
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Customer login successful");

        // Perform logout action
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Performing logout action");
        Logout();
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Logout action performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 52;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;
        
        // Verify logout is successful by checking if 'Login' text is present
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Verifying logout success");
        WebElement verifyLogoutSuccess = driver.findElement(By.xpath("(//p[@class='hidden text-base text-[#666666] mr-[5px] md:inline'])[1]"));
        boolean islogoutSuccessful = verifyLogoutSuccess.getText().toLowerCase().equals("login");
        Assert.assertTrue(islogoutSuccessful, "Logout was not successful!");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Logout was successful");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (islogoutSuccessful) {
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
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: customerLogoutTest completed");
    }
}
