package TestCases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
public class TC034_LcoBasicPlanNewCustomer extends AppUtils {

    @Test
    public void lcoBasicPlanNewCustomerCreationTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: lcoBasicPlanNewCustomerCreationTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform LCO login
        lcoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "LCO login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 40;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Navigate to Plans Page
        driver.findElement(By.xpath("//span[normalize-space()='Plans']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to Plans page");
        Thread.sleep(2000);

        driver.findElement(By.xpath("(//button[contains(text(),'Get a Start')])[1]")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on 'Get a Start' button");

        // Verify Customer Mobile Number field is visible in Pop-up window
        WebElement MobileNumberField = driver.findElement(By.xpath("//p[normalize-space()='Enter Customer Mobile Number']"));
        boolean isMobileNumberFieldVisible = MobileNumberField.getText().toLowerCase().equals("enter customer mobile number");
        System.out.println("Customer Mobile Number field visibility in Pop-up window: " + isMobileNumberFieldVisible);
        System.out.println("Text found: " + MobileNumberField.getText());
        Assert.assertTrue(isMobileNumberFieldVisible, "Customer Mobile Number field was not visible in Pop up window");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Customer Mobile Number field visibility in Pop-up window: " + isMobileNumberFieldVisible);
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Customer Mobile Number field was visible in Pop-up window");

        // Enter phone number and get OTP
        driver.findElement(By.xpath("//input[@type='text']")).sendKeys("8659862301");
        driver.findElement(By.xpath("//button[contains(text(),'Get OTP')]")).click();
        Thread.sleep(2000);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Entered phone number and clicked 'Get OTP'");

        // Enter OTP
        String otp = "654321";
        List<WebElement> otpFields = driver.findElements(By.xpath("//div[@class='relative']/input[@type='text' and @maxlength='1']"));
        for (int i = 0; i < otp.length(); i++) {
            otpFields.get(i).sendKeys(Character.toString(otp.charAt(i)));
        }
        Thread.sleep(2000);
        driver.findElement(By.xpath("//button[contains(text(),'Submit')]")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Entered OTP and clicked 'Submit'");

        // Verify KYC page visibility
        WebElement KYCpage = driver.findElement(By.xpath("//p[normalize-space()='Get Instant KYC Verification with DigiLocker']"));
        boolean isKYCpageVisible = KYCpage.getText().toLowerCase().equals("get instant kyc verification with digilocker");
        System.out.println("KYC page visibility: " + isKYCpageVisible);
        System.out.println("Text found: " + KYCpage.getText());
        Assert.assertTrue(isKYCpageVisible, "KYC page was not visible");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "KYC page visibility: " + isKYCpageVisible);
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "KYC page was visible");

        // Click for KYC verification 
        driver.findElement(By.xpath("//span[contains(text(),'Click Here')]")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on 'Click Here' for KYC verification");

        // Verify Aadhaar Number page visibility
        WebElement aadhaarNumberPage = driver.findElement(By.xpath("//p[normalize-space()='Enter your 12 digit Aadhaar Number']"));
        boolean isaadhaarNumberPageVisible = aadhaarNumberPage.getText().toLowerCase().equals("enter your 12 digit aadhaar number");
        System.out.println("Aadhaar Number page visibility: " + isaadhaarNumberPageVisible);
        System.out.println("Text found: " + aadhaarNumberPage.getText());
        Assert.assertTrue(isaadhaarNumberPageVisible, "Aadhaar Number page was not visible");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Aadhaar Number page visibility: " + isaadhaarNumberPageVisible);
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Aadhaar Number page was visible");

        // Click cancel button
        driver.findElement(By.xpath("//button[normalize-space()='Cancel']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on 'Cancel' button");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isMobileNumberFieldVisible && isKYCpageVisible && isaadhaarNumberPageVisible) {
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
