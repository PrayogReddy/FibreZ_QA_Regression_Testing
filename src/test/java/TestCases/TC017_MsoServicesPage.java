package TestCases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
public class TC017_MsoServicesPage extends AppUtils {

    @Test
    public void msoServicesPageTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: msoServicesPageTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform MSO login
        msoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "MSO login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 23;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Navigate to Services Page
        driver.findElement(By.xpath("//span[contains(text(),'▼')]")).click();
        driver.findElement(By.xpath("//span[normalize-space()='Services']")).click();
        Thread.sleep(1000);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to Services Page");

        // Verify List of all Services is displayed
        WebElement verifyListOfAllServices = driver.findElement(By.xpath("//p[contains(@class, 'text-base') and contains(text(), 'List of all Services')]"));
        boolean isListOfAllServicesVisible = verifyListOfAllServices.getText().toLowerCase().equals("list of all services");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Checking visibility of List of all Services");
        System.out.println("List of all Services visibility in Services page: " + isListOfAllServicesVisible);
        System.out.println("Text found: " + verifyListOfAllServices.getText());
        Assert.assertTrue(isListOfAllServicesVisible, "List of all Services was not visible after clicking Services Page");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "List of all Services is visible");

        // Define a list of expected values
        List<String> expectedValues = List.of("On Net Local Calls", "Set-Top Box");
        List<String> foundValues = new ArrayList<>();

        // Locate the table containing the List of all Services data
        WebElement servicesTable = driver.findElement(By.xpath("//table[@class='w-full table-data']"));

        // Find the index of the "Service Name" column using a simple loop
        List<WebElement> headers = servicesTable.findElements(By.tagName("th"));
        int serviceNameColumnIndex = 0;
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().trim().equalsIgnoreCase("Service Name")) {
                serviceNameColumnIndex = i;
                break;
            }
        }

        // Pagination and data extraction loop
        boolean allValuesFound = false;
        do {
            // Locate all the rows in the List of all Services table
            List<WebElement> rows = servicesTable.findElements(By.tagName("tr"));

            // Iterate through each row to find the specified values
            for (WebElement row : rows) {
                List<WebElement> columns = row.findElements(By.tagName("td"));

                if (columns.size() > 0) {
                    String cellText = columns.get(serviceNameColumnIndex).getText().trim();
                    if (expectedValues.contains(cellText) && !foundValues.contains(cellText)) {
                        foundValues.add(cellText);

                        // Print the Service name for the matched entry
                        System.out.println("Service Name: " + cellText);
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Found Service Name: " + cellText);
                    }
                }

                // Exit early if all values are found
                if (foundValues.size() == expectedValues.size()) {
                    allValuesFound = true;
                    break;
                }
            }

            if (!allValuesFound) {
                // Locate the pagination next button and click it
                WebElement nextButton = driver.findElement(By.xpath("(//button[contains(@class,'h-[30px] w-[30px] m-1 text-sm font-medium rounded-md border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-indigo-500')][normalize-space()='>'])[1]"));
                if (nextButton.isEnabled()) {
                    nextButton.click();
                    //ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on pagination next button");
                    // Wait for the next page to load
                    //Thread.sleep(3000);
                } else {
                    break;  // No more pages left
                }
            }

        } while (!allValuesFound);

        // Assert that all expected values were found in the table
        Assert.assertTrue(foundValues.containsAll(expectedValues), "Not all expected service values were found.");
        ExtentTestNGListener.getExtentTest().log(Status.PASS, "All expected service values were found");

        // Update summary results in the 'Smoke Test Cases' sheet
        if (foundValues.containsAll(expectedValues) && isListOfAllServicesVisible) {
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
