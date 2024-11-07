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
public class TC036_LcoAddPartner extends AppUtils {

    @Test
    public void createNewLcoPartnerTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: createNewLcoPartnerTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform LCO login
        lcoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "LCO login performed");

        // Click on the Partners link
        driver.findElement(By.xpath("//span[normalize-space()='Partners']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on 'Partners' link");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 42;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Verify if the 'Add Partner' button is visible on the Partners page
        WebElement verifyAddPartnerPageDisplayed = driver.findElement(By.xpath("//button[normalize-space()='Add Partner']"));
        System.out.println("Text found: " + verifyAddPartnerPageDisplayed.getText());
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Add Partner button text: " + verifyAddPartnerPageDisplayed.getText());
        verifyAddPartnerPageDisplayed.click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on 'Add Partner' button");

        // Check if the 'Create Partner' page is displayed
        boolean isCreatePartnerVisible = driver.findElement(By.xpath("//p[normalize-space()='Create Partner']")).getText().toLowerCase().equals("create partner");
        System.out.println(isCreatePartnerVisible);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Create Partner page visibility: " + isCreatePartnerVisible);
        Assert.assertTrue(isCreatePartnerVisible, "Create Partner Page was not visible after clicking Add Partner!");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Create Partner Page was visible after clicking Add Partner!");

        // Click on 'Cancel' button to close 'Create Partner' page
        driver.findElement(By.xpath("//button[normalize-space()='Cancel']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on 'Cancel' button");

        // Verify if the 'Partners' page is displayed after closing the 'Create Partner' page
        boolean isPartnersPageDisplayed = driver.findElement(By.xpath("(//p[@class='text-2xl px-4 py-2'])[1]")).getText().toLowerCase().equals("partners");
        System.out.println(isPartnersPageDisplayed);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Partners page visibility after closing Create Partner page: " + isPartnersPageDisplayed);
        Assert.assertTrue(isPartnersPageDisplayed, "Partners Page was not visible after closing Add Partner!");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Partners Page was visible after closing Add Partner!");

        // Update the results in the 'Smoke Test Cases' sheet if both pages are correctly displayed
        if (isCreatePartnerVisible && isPartnersPageDisplayed) {
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Updated 'Smoke Test Cases' sheet with 'Pass' status");

            // Record the timestamp of the test execution in the Excel sheet
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Recorded timestamp in 'Smoke Test Cases' sheet: " + formattedNow);
        }

        // End of the test case
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: createNewLcoPartnerTest ended");
    }
}
