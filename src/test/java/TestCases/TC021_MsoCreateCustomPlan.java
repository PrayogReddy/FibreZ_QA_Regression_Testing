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
public class TC021_MsoCreateCustomPlan extends AppUtils {

    @Test
    public void msoCreateCustomPlanTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: msoCreateCustomPlanTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform MSO login
        msoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "MSO login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 27;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Navigate to Plans Page
        driver.findElement(By.xpath("//span[normalize-space()='Plans']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to Plans Page");
        Thread.sleep(2000);

        driver.findElement(By.xpath("//button[normalize-space()='Create Plan']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on Create Plan button");

        // Verify Select Plan Type page after clicking Create Plan
        WebElement SelectPlan = driver.findElement(By.xpath("//p[@class='text-xl mt-3' and text()='Selection of Plan Type for Creation']"));
        boolean isSelectPlanPageVisible = SelectPlan.getText().toLowerCase().equals("selection of plan type for creation");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking visibility of Select Plan Type page");
        System.out.println("Select Plan Type visibility: " + isSelectPlanPageVisible);
        System.out.println("Text found: " + SelectPlan.getText());
        Assert.assertTrue(isSelectPlanPageVisible, "Select Plan Type page was not visible after clicking Create Plan");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Select Plan Type page is visible");

        driver.findElement(By.xpath("//span[normalize-space()='CUSTOM']")).click();
        driver.findElement(By.xpath("//button[normalize-space()='Done']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Selected CUSTOM option and clicked Done");

        // Verify Create Plan visibility
        WebElement CreatePlan = driver.findElement(By.xpath("//p[normalize-space()='Create Plan']"));
        boolean isCreatePlanVisible = CreatePlan.getText().toLowerCase().equals("create plan");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking visibility of Create Plan page");
        System.out.println("Create Plan visibility: " + isCreatePlanVisible);
        System.out.println("Text found: " + CreatePlan.getText());
        Assert.assertTrue(isCreatePlanVisible, "Create Plan was not visible");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Create Plan page is visible");

        // Verify Plans Page is visible after clicking Go Back button
        driver.findElement(By.xpath("//button[normalize-space()='Go Back']")).click();
        Thread.sleep(3000);
        WebElement PlansPage = driver.findElement(By.xpath("//p[contains(@class, 'text-2xl px-4 py-2') and contains(text(), 'All Plans')]"));
        boolean isPlansPageVisible = PlansPage.getText().toLowerCase().equals("all plans");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking visibility of Plans Page");
        System.out.println("Plans page visibility: " + isPlansPageVisible);
        System.out.println("Text found: " + PlansPage.getText());
        Assert.assertTrue(isPlansPageVisible, "Plans Page was not visible");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Plans Page is visible");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isSelectPlanPageVisible && isCreatePlanVisible && isPlansPageVisible) {
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
