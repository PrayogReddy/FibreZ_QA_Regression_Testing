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
public class TC002_BookNowRequest extends AppUtils {

    @Test
    public void bookNowFeatureTest() throws Throwable {

        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: bookNowFeatureTest started");

        // Get the row count from the test data sheet
        ExcelUtils.getRowCount(testDataFilePath, testDataSheetName1);
        
        // Define row and column numbers for test data and smoke testing results
        int testDataColNum = 4;
        int smokeTestRowNum = 8;
        int smokeTestColNum = 12;
        int testDataTimestampColNum = 5;
        int smokeTestTimestampColNum = 15;

        // Iterate through each row of the test data
        for (int i = 4; i < 5; i++) {
            // Retrieve test data from the Excel sheet
            String name = ExcelUtils.getCellData(testDataFilePath, testDataSheetName1, i, 0);
            String phoneNumber = ExcelUtils.getCellData(testDataFilePath, testDataSheetName1, i, 1);
            String emailAddress = ExcelUtils.getCellData(testDataFilePath, testDataSheetName1, i, 2);
            String message = ExcelUtils.getCellData(testDataFilePath, testDataSheetName1, i, 3);

            // Open the URL and perform the test
            driver.get(url);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);
            Thread.sleep(2000);
            
            // Click the 'Book Now' button
            driver.findElement(By.xpath("//div[normalize-space()='Book Now']")).click();
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "'Book Now' button clicked");
            Thread.sleep(2000);

            // Enter data into the 'Book Now' form fields
            driver.findElement(By.xpath("//input[@placeholder='Name']")).sendKeys(name);
            driver.findElement(By.xpath("//input[@placeholder='Phone Number']")).sendKeys(phoneNumber);
            driver.findElement(By.xpath("//input[@placeholder='Email ID']")).sendKeys(emailAddress);
            driver.findElement(By.xpath("//input[@placeholder='Message']")).sendKeys(message);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Entered data: Name=" + name + ", Phone Number=" + phoneNumber + ", Email=" + emailAddress + ", Message=" + message);

            // Click the submit button
            driver.findElement(By.xpath("//button[normalize-space()='Submit']")).click();
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "'Submit' button clicked");
            Thread.sleep(2000);

            // Validate that the submission was successful by checking the presence of "Login"
            WebElement verifySuccess = driver.findElement(By.xpath("//p[contains(@class, 'hidden') and contains(text(), 'Login')]"));
            boolean isSubmissionSuccessful = verifySuccess.getText().toLowerCase().equals("login");
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Validating submission success");
            System.out.println("Admin Dashboard visibility: " + isSubmissionSuccessful);
            System.out.println("Text found: " + verifySuccess.getText());
            Assert.assertTrue(isSubmissionSuccessful, "Submission was not successful!");

            // Update Excel with results if submission is successful
            if (isSubmissionSuccessful) {
                // Update detailed results in the 'Book Now Creation' sheet
                ExcelUtils.setCellData(testDataFilePath, testDataSheetName1, i, testDataColNum, "Pass");
                ExcelUtils.fillGreenColor(testDataFilePath, testDataSheetName1, i, testDataColNum);
                ExtentTestNGListener.getExtentTest().log(Status.PASS, "Submission was successful and result updated in Excel");

                // Update summary results in the 'Smoke Test Cases' sheet
                ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
                ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
                ExtentTestNGListener.getExtentTest().log(Status.PASS, "Summary results updated in Excel");
            } else {
                ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Submission was not successful");
            }

            // Record the timestamp of the test execution in the Excel sheet
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(testDataFilePath, testDataSheetName1, i, testDataTimestampColNum, formattedNow);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test results updated in Excel with timestamp: " + formattedNow);
        }
    }
}
