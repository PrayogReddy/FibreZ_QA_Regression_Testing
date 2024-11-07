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
public class TC039_LcoOperatorInventoryPage extends AppUtils {

    @Test
    public void LcoOperatorInventoryPageTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: LcoOperatorInventoryPageTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform LCO login
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Performing LCO login");
        lcoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "LCO login successful");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 45;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Click on the Operator Inventory link
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicking on 'Operator Inventory' link");
        driver.findElement(By.xpath("//span[normalize-space()='Operator Inventory']")).click();
        Thread.sleep(2000); // Wait for the page to load
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "'Operator Inventory' link clicked");

        // Verify Summary tab is visible in Inventory page
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Verifying 'Summary' tab visibility");
        WebElement verifySummaryTab = driver.findElement(By.xpath("//button[normalize-space()='Summary']"));
        boolean isSummaryTabVisible = verifySummaryTab.getText().toLowerCase().equals("summary");
        System.out.println("Summary Tab visibility: " + isSummaryTabVisible);
        System.out.println("Text found: " + verifySummaryTab.getText());
        Assert.assertTrue(isSummaryTabVisible, "Summary Tab was not visible after clicking Operator Inventory");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "'Summary' tab is visible");

        // Verify Orders tab is visible in Inventory page
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Verifying 'Orders' tab visibility");
        WebElement verifyOrdersTab = driver.findElement(By.xpath("//button[normalize-space()='Orders']"));
        boolean isOrdersTabVisible = verifyOrdersTab.getText().toLowerCase().equals("orders");
        System.out.println("Orders Tab visibility: " + isOrdersTabVisible);
        System.out.println("Text found: " + verifyOrdersTab.getText());
        Assert.assertTrue(isOrdersTabVisible, "Orders Tab was not visible after clicking Operator Inventory");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "'Orders' tab is visible");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isSummaryTabVisible && isOrdersTabVisible) {
            // Update result as 'Pass' in the Excel sheet
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Updated 'Smoke Test Cases' sheet with 'Pass' status");

            // Log timestamp of the test execution
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Recorded timestamp in 'Smoke Test Cases' sheet: " + formattedNow);
        }

        // Log the end of the test case
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: LcoOperatorInventoryPageTest completed");
    }
}
