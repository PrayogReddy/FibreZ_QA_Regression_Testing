package TestCases;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import Utilities.AppUtils;
import Utilities.ExcelUtils;
import Utilities.ExtentTestNGListener;

@Listeners(ExtentTestNGListener.class)
public class TC014_MsoRevenuePage extends AppUtils {

    @Test
    public void msoRevenuePageTest() throws Throwable {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));  // Set timeout to 2 seconds

        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: msoRevenuePageTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform MSO login
        msoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "MSO login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 20;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Navigate to Revenue Page
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Revenue']"))).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to Revenue Page");

        // Verify List of Partner share displayed
        WebElement verifyListOfPartnerShare = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(@class, 'text-base') and contains(text(), 'List of Partner share')]")));
        boolean isListOfPartnerVisible = verifyListOfPartnerShare.getText().equalsIgnoreCase("List of Partner share");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking visibility of List of Partner Share");
        System.out.println("List of Partner share visibility in Revenue page: " + isListOfPartnerVisible);
        System.out.println("Text found: " + verifyListOfPartnerShare.getText());
        Assert.assertTrue(isListOfPartnerVisible, "List of Partner share was not visible after clicking Revenue Page");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "List of Partner Share is visible");

        boolean isLcoShareFound = false;

        // Loop to handle pagination
        boolean hasNextPage = true;
        while (hasNextPage) {
            // Retry mechanism for finding JBLH_LCO
            for (int attempt = 0; attempt < 2; attempt++) {  // Retry up to 2 times
                try {
                    // Locate the table containing the partner share data
                    WebElement partnerShareTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@class='w-full table-data']")));

                    // Locate all the rows in the partner share table
                    List<WebElement> rows = partnerShareTable.findElements(By.tagName("tr"));

                    // Log the structure of the table for debugging
                    System.out.println("Number of rows: " + rows.size());
                    if (!rows.isEmpty()) {
                        WebElement firstRow = rows.get(0);
                        List<WebElement> headers = firstRow.findElements(By.tagName("th"));
                        System.out.println("Number of columns: " + headers.size());
                    }

                    // Iterate through each row to find the "JBLH_LCO"
                    for (WebElement row : rows) {
                        List<WebElement> columns = row.findElements(By.tagName("td"));

                        // Check if the row contains the "JBLH_LCO"
                        if (columns.size() > 1 && columns.get(1).getText().equalsIgnoreCase("JBLH_LCO")) {
                            isLcoShareFound = true;

                            // Print the LCO Share value for "JBLH_LCO"
                            String lcoShare = columns.size() > 4 ? columns.get(4).getText() : "N/A"; // Adjusted to 5th column (index 4)
                            System.out.println("LCO Share value for JBLH_LCO: " + lcoShare);
                            ExtentTestNGListener.getExtentTest().log(Status.INFO, "LCO Share value for JBLH_LCO: " + lcoShare);

                            break;
                        }
                    }

                    if (isLcoShareFound) {
                        break;  // Exit the retry loop if JBLH_LCO is found
                    }
                } catch (StaleElementReferenceException e) {
                    ExtentTestNGListener.getExtentTest().log(Status.INFO, "Encountered StaleElementReferenceException, retrying...");
                    System.out.println("Encountered StaleElementReferenceException, retrying...");
                    // Wait a bit before retrying
                    Thread.sleep(1000);
                }
            }

            // If the LCO share is not found, click the 'Next' button to go to the next page
            if (!isLcoShareFound) {
                try {
                    List<WebElement> nextButtons = driver.findElements(By.xpath("//button[contains(@class,'h-[30px] w-[30px] m-1 text-sm font-medium rounded-md border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-indigo-500')][normalize-space()='>']"));
                    if (!nextButtons.isEmpty() && nextButtons.get(0).isEnabled()) {
                        nextButtons.get(0).click();
                        Thread.sleep(3000); // Wait for the next page to load
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked 'Next' button to go to the next page");
                    } else {
                        // If no more pages are available, stop checking
                        hasNextPage = false;
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "No more pages available for pagination");
                    }
                } catch (StaleElementReferenceException e) {
                    ExtentTestNGListener.getExtentTest().log(Status.INFO, "Encountered StaleElementReferenceException while trying to click 'Next' button, stopping pagination.");
                    System.out.println("Encountered StaleElementReferenceException while trying to click 'Next' button, stopping pagination.");
                    hasNextPage = false;
                }
            } else {
                hasNextPage = false; // Stop checking if JBLH_LCO is found
            }
        }

        // Assert that the "JBLH_LCO" was found in the table
        if (isLcoShareFound) {
            // Update result as 'Pass' in the Excel sheet
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);

            // Log timestamp of test execution
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test results updated in Excel with timestamp: " + formattedNow);
        } else {
            // Log failure in Extent Reports if JBLH_LCO is not found
            ExtentTestNGListener.getExtentTest().log(Status.FAIL, "LCO Share value for JBLH_LCO was not found.");
        }

        // Assert that the "JBLH_LCO" was found or print an error message
        Assert.assertTrue(isLcoShareFound, "LCO Share value for JBLH_LCO was not found.");
    }
}
