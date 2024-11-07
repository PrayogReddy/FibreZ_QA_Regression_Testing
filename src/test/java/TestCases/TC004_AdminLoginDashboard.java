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
public class TC004_AdminLoginDashboard extends AppUtils {

    @Test
    public void adminLoginDashboardTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: adminLoginDashboardTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform admin login
        adminLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Admin login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 9;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Verify Admin Dashboard is visible
        WebElement verifyAdminDashboardSuccess = driver.findElement(By.xpath("//p[normalize-space()='Admin Dashboard']"));
        boolean isAdminDashboardVisible = verifyAdminDashboardSuccess.getText().toLowerCase().equals("admin dashboard");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking Admin Dashboard visibility");
        System.out.println("Admin Dashboard visibility: " + isAdminDashboardVisible);
        System.out.println("Text found: " + verifyAdminDashboardSuccess.getText());
        Assert.assertTrue(isAdminDashboardVisible, "Admin Dashboard was not visible after login!");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Admin Dashboard is visible");

        // Validate Revenue Pie Chart is visible on the Dashboard
        WebElement pieChartElement = driver.findElement(By.xpath("(//canvas[@role='img'])[1]"));
        pieChartElement.click();
        WebElement isPieChartVisible = driver.findElement(By.xpath("//p[normalize-space()='List of Payments']"));
        boolean isListofPaymentsVisible = isPieChartVisible.getText().equalsIgnoreCase("List of Payments");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking Pie Chart visibility on Admin Dashboard");
        System.out.println("Pie chart visibility: " + isListofPaymentsVisible);
        System.out.println("Text found: " + isPieChartVisible.getText());
        Assert.assertTrue(isListofPaymentsVisible, "Pie chart is not visible on the Admin Dashboard!");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Pie chart is visible on the Admin Dashboard");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isAdminDashboardVisible && isListofPaymentsVisible) {
            // Update result as 'Pass' in the Excel sheet
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Test results updated in Excel with 'Pass' status");

            // Log timestamp of the test execution
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test results updated in Excel with timestamp: " + formattedNow);
        } else {
            // Log failure in Extent Reports if validation fails
            ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Admin Dashboard or Pie Chart visibility failed");
        }
    }
}
