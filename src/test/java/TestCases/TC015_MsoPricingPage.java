package TestCases;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
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
public class TC015_MsoPricingPage extends AppUtils {

    @Test
    public void msoPricingPageTest() throws Throwable {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(2000));  // Set timeout to 2 seconds

        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: msoPricingPageTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform MSO login
        msoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "MSO login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 21;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Navigate to Pricing Page
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'▼')]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Pricing']"))).click();
        Thread.sleep(3000);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to Pricing Page");

        // Verify List of all Pricing is displayed
        WebElement verifyListOfAllPricing = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(@class, 'text-base') and contains(text(), 'List of all Pricing')]")));
        boolean isListOfAllPricingVisible = verifyListOfAllPricing.getText().toLowerCase().equals("list of all pricing");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking visibility of List of all Pricing");
        System.out.println("List of all Pricing visibility in Pricing page: " + isListOfAllPricingVisible);
        System.out.println("Text found: " + verifyListOfAllPricing.getText());
        Assert.assertTrue(isListOfAllPricingVisible, "List of all Pricing was not visible after clicking Pricing Page");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "List of all Pricing is visible");

        // Define a list of expected values
        List<String> expectedValues = List.of("399_Price", "799_Price", "1299_Price");
        List<String> foundValues = new ArrayList<>();

        boolean allValuesFound = false;
        boolean isNextButtonEnabled = true;

        while (!allValuesFound && isNextButtonEnabled) {
            // Locate the table containing the List of all Pricing data
            WebElement pricingTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@class='w-full table-data']")));

            // Locate all the rows in the List of all Pricing table
            List<WebElement> rows = pricingTable.findElements(By.tagName("tr"));

            // Iterate through each row to find the specified values
            for (WebElement row : rows) {
                List<WebElement> columns = row.findElements(By.tagName("td"));

                if (columns.size() > 0) {
                    String cellText = columns.get(1).getText();
                    if (expectedValues.contains(cellText) && !foundValues.contains(cellText)) {
                        foundValues.add(cellText);

                        // Print the Pricing Device Name for the matched entry
                        System.out.println("Pricing Device Name :" + cellText);
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Pricing Device Name: " + cellText);
                    }
                }

                // Exit early if all values are found
                if (foundValues.size() == expectedValues.size()) {
                    allValuesFound = true;
                    break;
                }
            }

            // Handle potential pagination, if it exists
            if (!allValuesFound) {
                List<WebElement> nextButtons = driver.findElements(By.xpath("(//button[contains(@class,'h-[30px] w-[30px] m-1 text-sm font-medium rounded-md border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-indigo-500')][normalize-space()='>'])[1]"));
                if (!nextButtons.isEmpty()) {
                    WebElement nextButton = nextButtons.get(0);
                    isNextButtonEnabled = nextButton.isEnabled();
                    if (isNextButtonEnabled) {
                        nextButton.click();
                        Thread.sleep(3000); // Wait for the next page to load
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked 'Next' button to go to the next page");
                    } else {
                        // No more pages, end the loop
                        break;
                    }
                } else {
                    // No "Next" button found, assume no pagination
                    isNextButtonEnabled = false;
                }
            }
        }

        // Assert that all expected values were found in the table
        if (foundValues.containsAll(expectedValues)) {
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "All expected pricing values were found.");
            // Update summary results in the 'Smoke Test Cases' sheet
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);

            // Log timestamp of the test execution
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test results updated in Excel with timestamp: " + formattedNow);
        } else {
            ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Not all expected pricing values were found.");
        }
    }
}
