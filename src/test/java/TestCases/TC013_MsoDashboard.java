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
public class TC013_MsoDashboard extends AppUtils {

    @Test
    public void msoLoginDashboardTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: msoLoginDashboardTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform MSO login
        msoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "MSO login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 19;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Verify MSO Dashboard is visible
        WebElement verifyMsoDashboardSuccess = driver.findElement(By.xpath("//p[contains(text(), 'Mso Dashboard')]"));
        boolean isMsoDashboardVisible = verifyMsoDashboardSuccess.getText().toLowerCase().equals("mso dashboard");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking MSO Dashboard visibility");
        System.out.println("MSO Dashboard visibility: " + isMsoDashboardVisible);
        System.out.println("Text found: " + verifyMsoDashboardSuccess.getText());
        Assert.assertTrue(isMsoDashboardVisible, "MSO Dashboard was not visible after login!");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "MSO Dashboard is visible");

        // Validate Plans Pie Chart is visible on the Dashboard
        WebElement pieChartElement = driver.findElement(By.xpath("(//canvas[@role='img'])[1]"));
        pieChartElement.click();
        WebElement isPieChartVisible = driver.findElement(By.xpath("//p[normalize-space()='List of Plans']"));
        boolean isListofPlansVisible = isPieChartVisible.getText().toLowerCase().equals("list of plans");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking visibility of Pie Chart on Dashboard");
        System.out.println("Pie chart visibility: " + isListofPlansVisible);
        System.out.println("Text found: " + isPieChartVisible.getText());
        Assert.assertTrue(isListofPlansVisible, "Pie chart is not visible on the MSO Dashboard!");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Pie chart is visible and List of Plans is displayed");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isMsoDashboardVisible && isListofPlansVisible) {
            // Update result as 'Pass' in the Excel sheet
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Test results updated in Excel with 'Pass' status");

            // Log timestamp of test execution
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test results updated in Excel with timestamp: " + formattedNow);
        } else {
            // Log failure in Extent Reports if validation fails
            ExtentTestNGListener.getExtentTest().log(Status.FAIL, "MSO Dashboard or Pie Chart visibility check failed");
        }
    }
}
