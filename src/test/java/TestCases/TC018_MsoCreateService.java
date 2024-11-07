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
public class TC018_MsoCreateService extends AppUtils {

    @Test
    public void msoServicePageTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: msoServicePageTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform MSO login
        msoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "MSO login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 24;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Navigate to Services Page
        driver.findElement(By.xpath("//span[contains(text(),'▼')]")).click();
        driver.findElement(By.xpath("//span[normalize-space()='Services']")).click();
        Thread.sleep(3000);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to Services Page");

        // Click on Create Service button
        driver.findElement(By.xpath("//button[normalize-space()='Create Service']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on Create Service button");

        // Verify Create Service Page is visible
        WebElement CreateServicePage = driver.findElement(By.xpath("//div[contains(text(),'Select Service')]"));
        boolean isCreateServicePageVisible = CreateServicePage.getText().toLowerCase().equals("select service");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking visibility of Create Service Page");
        System.out.println("Create Service page visibility: " + isCreateServicePageVisible);
        System.out.println("Text found: " + CreateServicePage.getText());
        Assert.assertTrue(isCreateServicePageVisible, "Create Service Page was not visible");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Create Service Page is visible");

        // Verify Services Page is visible after clicking Go back button
        driver.findElement(By.xpath("//button[normalize-space()='Go Back']")).click();
        Thread.sleep(3000);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on Go Back button");
        WebElement ServicePage = driver.findElement(By.xpath("//p[contains(@class, 'text-base') and contains(text(), 'List of all Services')]"));
        boolean isServicePageVisible = ServicePage.getText().toLowerCase().equals("list of all services");
        System.out.println("Service page visibility: " + isServicePageVisible);
        System.out.println("Text found: " + ServicePage.getText());
        Assert.assertTrue(isServicePageVisible, "Services Page was not visible");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Services Page is visible");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isCreateServicePageVisible && isServicePageVisible) {
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
    }
}
