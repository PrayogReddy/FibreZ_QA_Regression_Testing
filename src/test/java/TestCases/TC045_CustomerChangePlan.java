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
public class TC045_CustomerChangePlan extends AppUtils {

    @Test
    public void CustomerChangePlanTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: CustomerChangePlanTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);
        
        // Perform Customer login
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Performing customer login");
        customerLogin();
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Customer login successful");
        
        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 51;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;
        
        // Click on the Change Plan link
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicking on 'Change Plan' link");
        driver.findElement(By.xpath("//span[normalize-space()='Change Plan']")).click();
        Thread.sleep(2000); // Wait for the page to load
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "'Change Plan' link clicked");

        // Verify Plan is visible in Recommended Plans page
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Verifying plan visibility");
        WebElement verifyPlan = driver.findElement(By.xpath("//b[@class='text-primary' and text()='999']"));
        boolean isPlanVisible = verifyPlan.getText().toLowerCase().equals("999");
        System.out.println("Plan visibility: " + isPlanVisible);
        System.out.println("Text found: " + verifyPlan.getText());
        Assert.assertTrue(isPlanVisible, "Plan was not visible in Recommended Plans page");        
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Plan is visible");

        // Verify Enter OTP screen pop up is visible after clicking required Plan
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Selecting the plan");
        driver.findElement(By.xpath("(//button[contains(text(),'Select Plan')])[1]")).click();
        WebElement verifyOtpScreen = driver.findElement(By.xpath("//p[@class='text-base text-black text-center' and text()='Enter OTP to verify']"));
        boolean isOtpScreenVisible = verifyOtpScreen.getText().toLowerCase().equals("enter otp to verify");
        System.out.println("Enter OTP screen pop up visibility: " + isOtpScreenVisible);
        System.out.println("Text found: " + verifyOtpScreen.getText());
        Assert.assertTrue(isOtpScreenVisible, "Enter OTP screen pop up was not visible after clicking required Plan");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Enter OTP screen pop up is visible");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isPlanVisible && isOtpScreenVisible) {
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
        
        // Close pop-up window
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Closing pop-up window");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//*[name()='svg'][@class='absolute top-[10px] right-[10px] h-5 w-5 text-slate-400 cursor-pointer hover:text-primary hover:border-primary'])[1]")).click();
        Thread.sleep(1000);

        // Log the end of the test case
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: CustomerChangePlanTest completed");
    }
}
