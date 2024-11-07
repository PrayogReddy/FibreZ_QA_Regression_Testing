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
public class TC028_LcoDashboard extends AppUtils {

    @Test
    public void lcoLoginDashboardTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: lcoLoginDashboardTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform LCO login
        lcoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "LCO login performed");
        
        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 34;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Verify LCO Dashboard is visible
        WebElement verifyLcoDashboardSuccess = driver.findElement(By.xpath("//p[contains(text(), 'Lco Dashboard')]"));
        boolean isLcoDashboardVisible = verifyLcoDashboardSuccess.getText().toLowerCase().equals("lco dashboard");
        System.out.println("LCO Dashboard visibility: " + isLcoDashboardVisible);
        System.out.println("Text found: " + verifyLcoDashboardSuccess.getText());
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "LCO Dashboard visibility: " + isLcoDashboardVisible);
        Assert.assertTrue(isLcoDashboardVisible, "LCO Dashboard was not visible after login!");

        // Validate Connections Pie Chart is visible on the Dashboard
        WebElement pieChartElement = driver.findElement(By.xpath("(//canvas[@role='img'])[1]"));
        pieChartElement.click();
        WebElement isPieChartVisible = driver.findElement(By.xpath("//p[normalize-space()='List of Connections']"));
        boolean isListOfConnectionsVisible = isPieChartVisible.getText().equalsIgnoreCase("List of Connections");
        System.out.println("Pie chart visibility: " + isListOfConnectionsVisible);
        System.out.println("Text found: " + isPieChartVisible.getText());
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Pie chart visibility: " + isListOfConnectionsVisible);
        Assert.assertTrue(isListOfConnectionsVisible, "Pie chart is not visible on the LCO Dashboard!");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isLcoDashboardVisible && isListOfConnectionsVisible) {
            // Update result as 'Pass' in the Excel sheet
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Test results updated with 'Pass' status in Excel");

            // Log timestamp of the test execution
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test execution timestamp updated in Excel: " + formattedNow);
        } else {
            ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Dashboard or Pie Chart verification failed");
        }
    }
}
