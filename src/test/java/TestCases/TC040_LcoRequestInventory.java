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
public class TC040_LcoRequestInventory extends AppUtils {

    @Test
    public void LcoRequestInventoryCreationTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: LcoRequestInventoryCreationTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform LCO login
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Performing LCO login");
        lcoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "LCO login successful");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 46;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Click on the Operator Inventory link
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicking on 'Operator Inventory' link");
        driver.findElement(By.xpath("//span[normalize-space()='Operator Inventory']")).click();
        Thread.sleep(2000); // Wait for the page to load
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "'Operator Inventory' link clicked");

        // Verify Request Inventory button is visible
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Verifying 'Request Inventory' button visibility");
        WebElement verifyRequestInventory = driver.findElement(By.xpath("//button[normalize-space()='Request Inventory']"));
        verifyRequestInventory.click();
        boolean isRequestInventoryVisible = verifyRequestInventory.getText().toLowerCase().equals("request inventory");
        System.out.println("Request Inventory button visibility: " + isRequestInventoryVisible);
        System.out.println("Text found: " + verifyRequestInventory.getText());
        Assert.assertTrue(isRequestInventoryVisible, "Request Inventory button was not visible in Operator Inventory page");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "'Request Inventory' button is visible");

        // Verify Request Shipment button is visible
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Verifying 'Request Shipment' button visibility");
        WebElement verifyRequestShipment = driver.findElement(By.xpath("//button[normalize-space()='Request Shipment']"));
        boolean isRequestShipmentVisible = verifyRequestShipment.getText().toLowerCase().equals("request shipment");
        System.out.println("Request Shipment pop-up visibility: " + isRequestShipmentVisible);
        System.out.println("Text found: " + verifyRequestShipment.getText());
        Assert.assertTrue(isRequestShipmentVisible, "'Request Shipment' button was not visible after clicking Request Inventory");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "'Request Shipment' button is visible");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isRequestInventoryVisible && isRequestShipmentVisible) {
            // Update result as 'Pass' in the Excel sheet
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);

            // Log timestamp of the test execution
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Test results updated in Excel with timestamp: " + formattedNow);
        }

        // Close the pop-up window
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Closing the pop-up window");
        driver.findElement(By.xpath("//button[normalize-space()='Cancel']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Pop-up window closed");

        // Log the end of the test case
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: LcoRequestInventoryCreationTest completed");
    }
}
