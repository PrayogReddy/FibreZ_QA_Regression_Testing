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
public class TC033_LcoCustomPlans extends AppUtils {

    @Test
    public void lcoCustomPlansPageTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: lcoCustomPlansPageTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform LCO login
        lcoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "LCO login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 39;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Navigate to Plans Page
        driver.findElement(By.xpath("//span[normalize-space()='Plans']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to Plans page");
        Thread.sleep(2000);

        driver.findElement(By.xpath("//button[normalize-space()='Custom Plans']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on Custom Plans button");
        Thread.sleep(2000);

        // Verify Custom Plans is visible in Plans page
        WebElement CustomPlans = driver.findElement(By.xpath("//button[normalize-space()='Custom Plans']"));
        boolean isCustomPlansVisible = CustomPlans.getText().toLowerCase().equals("custom plans");
        System.out.println("Custom Plans visibility in Plans page: " + isCustomPlansVisible);
        System.out.println("Text found: " + CustomPlans.getText());
        Assert.assertTrue(isCustomPlansVisible, "Custom Plans was not visible in Plans page");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Custom Plans visibility in Plans page: " + isCustomPlansVisible);
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Custom Plans was visible in Plans page");

        // Verify Gold Plan visibility
        WebElement GoldPlan = driver.findElement(By.xpath("//p[normalize-space()='Gold Plan']"));
        boolean isGoldPlanVisible = GoldPlan.getText().toLowerCase().equals("gold plan");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking visibility of Gold Plan");
        System.out.println("Gold Plan visibility: " + isGoldPlanVisible);
        System.out.println("Text found: " + GoldPlan.getText());
        Assert.assertTrue(isGoldPlanVisible, "Gold Plan was not visible");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Gold Plan is visible");

        // Verify Rental Plan visibility
        WebElement RentalPlan = driver.findElement(By.xpath("//p[normalize-space()='Rental Plan']"));
        boolean isRentalPlanVisible = RentalPlan.getText().toLowerCase().equals("rental plan");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking visibility of Rental Plan");
        System.out.println("Rental Plan visibility: " + isRentalPlanVisible);
        System.out.println("Text found: " + RentalPlan.getText());
        Assert.assertTrue(isRentalPlanVisible, "Rental Plan was not visible");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Rental Plan is visible");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isCustomPlansVisible && isGoldPlanVisible && isRentalPlanVisible) {
            // Update result as 'Pass' in the Excel sheet
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Test result updated in Excel with 'Pass' status");

            // Log timestamp of the test execution
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test result updated in Excel with timestamp: " + formattedNow);
        }
    }
}
