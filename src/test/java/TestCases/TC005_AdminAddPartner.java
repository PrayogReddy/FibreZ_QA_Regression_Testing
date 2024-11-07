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
public class TC005_AdminAddPartner extends AppUtils {

    @Test
    public void createNewAdminPartnerTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: createNewAdminPartnerTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform admin login
        adminLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Admin login performed");

        // Define row and column numbers for test data & smoke testing results
        int smokeTestRowNum = 11;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Verify if the 'Add Partner' button is visible on the admin dashboard
        WebElement verifyAddPartnerDisplayed = driver.findElement(By.xpath("//button[normalize-space()='Add partner']"));
        boolean isAddPartnerVisible = verifyAddPartnerDisplayed.getText().toLowerCase().equals("add partner");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking Add Partner button visibility");
        System.out.println("Add Partner visibility: " + isAddPartnerVisible);
        System.out.println("Text found: " + verifyAddPartnerDisplayed.getText());
        Assert.assertTrue(isAddPartnerVisible, "Add Partner was not visible after Admin login!");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Add Partner button is visible");

        verifyAddPartnerDisplayed.click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on Add Partner button");

        // Check if the 'Create Partner' page is displayed
        WebElement verifyCreatePartnerDisplayed = driver.findElement(By.xpath("//p[normalize-space()='Create Partner']"));
        boolean isCreatePartnerVisible = verifyCreatePartnerDisplayed.getText().toLowerCase().equals("create partner");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking Create Partner page visibility");
        System.out.println("Create Partner page visibility: " + isCreatePartnerVisible);
        System.out.println("Text found: " + verifyCreatePartnerDisplayed.getText());
        Assert.assertTrue(isCreatePartnerVisible, "Create Partner Page was not visible after clicking Add Partner!");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Create Partner page is visible");

        // Close the 'Create Partner' page and verify the 'Partners' page
        driver.findElement(By.xpath("//button[normalize-space()='Cancel']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on Cancel button to close Create Partner page");

        WebElement verifyPartnersPageDisplayed = driver.findElement(By.xpath("(//p[@class='text-2xl px-4 py-2'])[1]"));
        boolean isPartnersPageDisplayed = verifyPartnersPageDisplayed.getText().toLowerCase().equals("partners");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking Partners page visibility after closing Create Partner page");
        System.out.println("Partners page visibility: " + isPartnersPageDisplayed);
        System.out.println("Text found: " + verifyPartnersPageDisplayed.getText());
        Assert.assertTrue(isPartnersPageDisplayed, "Partners Page was not visible after closing Add Partner!");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Partners page is visible after closing Create Partner");

        // Update the results in the 'Smoke Test Cases' sheet
        if (isAddPartnerVisible && isCreatePartnerVisible && isPartnersPageDisplayed) {
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Test results updated in Excel with 'Pass' status");

            // Record the timestamp of the test execution in the Excel sheet
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test results updated in Excel with timestamp: " + formattedNow);
        } else {
            // Log failure in Extent Reports if validation fails
            ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Add Partner or Create Partner or Partners page visibility failed");
        }
    }
}
