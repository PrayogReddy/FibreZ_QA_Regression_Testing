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
public class TC010_CreateRole extends AppUtils {

    @Test
    public void createRoleButtonTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: createRoleButtonTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform admin login
        adminLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Admin login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 16;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Navigate to the Roles page
        driver.findElement(By.xpath("//span[normalize-space()='Roles']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to Roles page");
        Thread.sleep(3000); // Consider using WebDriverWait instead
        driver.findElement(By.xpath("//button[normalize-space()='Create Role']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on Create Role button");

        // Verify if the 'Cancel' button is present to ensure the 'Create Role' page is loaded
        WebElement verifyCreateRole = driver.findElement(By.xpath("//button[normalize-space()='Cancel']"));
        boolean isCreateRoleFound = verifyCreateRole.getText().toLowerCase().equals("cancel");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking Create Role page visibility");
        System.out.println("Cancel button visibility in Create Role page: " + isCreateRoleFound);
        System.out.println("Text found: " + verifyCreateRole.getText());
        Assert.assertTrue(isCreateRoleFound, "Create Role Button Not Found");

        // Update the results in the 'Smoke Test Cases' sheet if the 'Create Role' page is found
        if (isCreateRoleFound) {
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Create Role page is visible and results updated in Excel with 'Pass' status");

            // Record the timestamp of the test execution in the Excel sheet
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test results updated in Excel with timestamp: " + formattedNow);
        } else {
            // Log failure in Extent Reports if validation fails
            ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Create Role page visibility failed");
        }
    }
}
