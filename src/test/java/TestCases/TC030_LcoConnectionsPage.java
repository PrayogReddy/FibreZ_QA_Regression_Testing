package TestCases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import Utilities.AppUtils;
import Utilities.ExcelUtils;
import Utilities.ExtentTestNGListener;
import com.aventstack.extentreports.Status;

@Listeners(ExtentTestNGListener.class)
public class TC030_LcoConnectionsPage extends AppUtils {

    @Test
    public void LcoConnectionsListTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: LcoConnectionsListTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);
        
        // Perform LCO login
        lcoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "LCO login performed");

        // Click on the Connections link
        driver.findElement(By.xpath("//span[normalize-space()='Connections']")).click();
        Thread.sleep(2000); // Wait for the page to load
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on the Connections link");

        // Verify List of all Connections page is visible
        WebElement verifyListofallConnectionsPage = driver.findElement(By.xpath("//p[contains(@class, 'text-base') and contains(text(), 'List of all Connections')]"));
        boolean isListofallConnectionsVisible = verifyListofallConnectionsPage.getText().toLowerCase().equals("list of all connections");
        System.out.println("List of all Connections page visibility: " + isListofallConnectionsVisible);
        System.out.println("Text found: " + verifyListofallConnectionsPage.getText());
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "List of all Connections page visibility: " + isListofallConnectionsVisible);
        Assert.assertTrue(isListofallConnectionsVisible, "List of all Connections page was not visible after clicking Connections");
        
        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 36;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        boolean isCustomerFound = false;
        boolean isLastPageReached = false; // To track if the last page is reached

        // Start of the while loop
        while (!isCustomerFound && !isLastPageReached) {
            // Capture all the data of the web table in Connections
            WebElement customerTable = driver.findElement(By.xpath("//table[@class='w-full table-data']"));
            List<WebElement> rows = customerTable.findElements(By.tagName("tr"));

            // Iterate through each row to find the specific Customer
            for (int i = 1; i < rows.size(); i++) {
                List<WebElement> cols = rows.get(i).findElements(By.tagName("td"));
                if (!cols.isEmpty()) {
                    String customerIDdata = cols.get(2).getText(); // Assuming Customer ID is in the 3rd column (index 2)

                    // Check if the Customer ID matches the expected value
                    if ("TC61366568".equals(customerIDdata)) {
                        // Print the captured Customer ID
                        System.out.println("Captured Customer ID: " + customerIDdata);
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Captured Customer ID: " + customerIDdata);

                        // Update summary results in the 'Smoke Test Cases' sheet
                        ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
                        ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
                        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Test results updated with 'Pass' status in Excel");

                        // Record the timestamp of the test execution in the Excel sheet
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                        String formattedNow = now.format(formatter);
                        ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test execution timestamp updated in Excel: " + formattedNow);

                        isCustomerFound = true; // Mark the customer as found
                        break; // Exit the for loop since the customer is found
                    }
                }
            }

            // Check if the customer was found
            if (!isCustomerFound) {
                try {
                    // Find the 'Next' button and click it if enabled
                    WebElement nextButton = driver.findElement(By.xpath("(//button[contains(@class,'h-[30px] w-[30px] m-1 text-sm font-medium rounded-md border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-indigo-500')][normalize-space()='>'])[1]"));
                    if (nextButton.isEnabled()) {
                        nextButton.click();
                        Thread.sleep(3000); // Wait for the next page to load
                        //ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to the next page");
                    } else {
                        // No more pages to search
                        isLastPageReached = true;
                        System.out.println("Customer ID 'TC61366568' not found after checking all pages.");
                        ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Customer ID 'TC61366568' not found after checking all pages");
                        Assert.fail("Customer ID 'TC61366568' not found after checking all pages.");
                    }
                } catch (NoSuchElementException e) {
                    // 'Next' button not found, meaning no additional pages exist
                    isLastPageReached = true;
                    System.out.println("Customer ID 'TC61366568' not found on the first page, and no more pages exist.");
                    ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Customer ID 'TC61366568' not found on the first page, and no more pages exist");
                    Assert.fail("Customer ID 'TC61366568' not found on the first page, and no more pages exist.");
                }
            }
        }
    }
}
