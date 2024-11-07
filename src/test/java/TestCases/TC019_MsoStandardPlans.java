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
public class TC019_MsoStandardPlans extends AppUtils {

    @Test
    public void msoStandardPlansPageTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: msoStandardPlansPageTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform MSO login
        msoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "MSO login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 25;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Navigate to Plans Page
        driver.findElement(By.xpath("//span[normalize-space()='Plans']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to Plans Page");
        Thread.sleep(2000);
        
        driver.findElement(By.xpath("//button[normalize-space()='Standard Plans']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on Standard Plans button");

        // Verify Standard Plans is visible in Plans page
        WebElement StandardPlans = driver.findElement(By.xpath("//button[normalize-space()='Standard Plans']"));
        boolean isStandardPlansVisible = StandardPlans.getText().toLowerCase().equals("standard plans");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking visibility of Standard Plans");
        System.out.println("Standard Plans visibility in Plans page: " + isStandardPlansVisible);
        System.out.println("Text found: " + StandardPlans.getText());
        Assert.assertTrue(isStandardPlansVisible, "Standard Plans was not visible in Plans page");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Standard Plans is visible");

        // Verify Platinum Plan visibility
        WebElement StarterPackPlan = driver.findElement(By.xpath("//p[normalize-space()='Platinum']"));
        boolean isStarterPackPlanVisible = StarterPackPlan.getText().toLowerCase().equals("platinum");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking visibility of Platinum Plan");
        System.out.println("Platinum Plan visibility: " + isStarterPackPlanVisible);
        System.out.println("Text found: " + StarterPackPlan.getText());
        Assert.assertTrue(isStarterPackPlanVisible, "Platinum Plan was not visible");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Platinum Plan is visible");

//        // Verify Regular Pack Plan visibility
//        WebElement RegularPackPlan = driver.findElement(By.xpath("//p[normalize-space()='Regular Pack']"));
//        boolean isRegularPackPlanVisible = RegularPackPlan.getText().toLowerCase().equals("regular pack");
//        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking visibility of Regular Pack Plan");
//        System.out.println("Regular Pack Plan visibility: " + isRegularPackPlanVisible);
//        System.out.println("Text found: " + RegularPackPlan.getText());
//        Assert.assertTrue(isRegularPackPlanVisible, "Regular Pack Plan was not visible");
//        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Regular Pack Plan is visible");
//
//        // Verify Premium Plan visibility
//        WebElement PremiumPlan = driver.findElement(By.xpath("//p[normalize-space()='Premium Plan']"));
//        boolean isPremiumPlanVisible = PremiumPlan.getText().toLowerCase().equals("premium plan");
//        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking visibility of Premium Plan");
//        System.out.println("Premium Plan visibility: " + isPremiumPlanVisible);
//        System.out.println("Text found: " + PremiumPlan.getText());
//        Assert.assertTrue(isPremiumPlanVisible, "Premium Plan was not visible");
//        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Premium Plan is visible");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isStandardPlansVisible && isStarterPackPlanVisible) {
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
