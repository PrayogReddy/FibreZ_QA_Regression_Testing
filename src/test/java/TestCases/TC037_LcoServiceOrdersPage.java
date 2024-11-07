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
public class TC037_LcoServiceOrdersPage extends AppUtils {

    @Test
    public void LcoServiceOrdersPageTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: LcoServiceOrdersPageTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform LCO login
        lcoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "LCO login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 43;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        boolean isServiceOrderFound = false;
        boolean isLastPageReached = false; // Track if the last page is reached

        // Pagination logic for Service Orders
        while (!isServiceOrderFound && !isLastPageReached) {
            // Click on the Service Orders link
            driver.findElement(By.xpath("//span[normalize-space()='Service Orders']")).click();
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on 'Service Orders' link");
            Thread.sleep(2000); // Wait for the page to load

            // Capture all the data of the web table in Service Orders
            WebElement serviceOrdersTable = driver.findElement(By.xpath("(//table[@class='w-full table-data'])[1]"));
            List<WebElement> rows = serviceOrdersTable.findElements(By.tagName("tr"));

            // Iterate through each row to find the specific Service Order
            for (int i = 1; i < rows.size(); i++) {
                List<WebElement> cols = rows.get(i).findElements(By.tagName("td"));
                if (!cols.isEmpty()) {
                    String serviceOrdersData = cols.get(1).getText(); // Assuming Service Order ID is in the 2nd column (index 1)

                    // Check if the Service Order ID matches the expected value
                    if ("95552346".equals(serviceOrdersData)) {
                        System.out.println("Captured Service Order ID: " + serviceOrdersData);
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Captured Service Order ID: " + serviceOrdersData);

                        // Click the radio button associated with the Service Order
                        WebElement radioButton = rows.get(i).findElement(By.xpath(".//*[name()='svg'][@class='size-6']"));
                        radioButton.click();
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on radio button for Service Order ID: " + serviceOrdersData);
                        Thread.sleep(2000); // Wait for the radio button action to complete

                        // Verify Order Details page is visible
                        WebElement verifyOrderDetailsPage = driver.findElement(By.xpath("//label[normalize-space()='Order Details']"));
                        boolean isOrderDetailsPageVisible = verifyOrderDetailsPage.getText().toLowerCase().equals("order details");
                        System.out.println("Order Details page visibility: " + isOrderDetailsPageVisible);
                        System.out.println("Text found: " + verifyOrderDetailsPage.getText());
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Order Details page visibility: " + isOrderDetailsPageVisible);
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Text found: " + verifyOrderDetailsPage.getText());
                        Assert.assertTrue(isOrderDetailsPageVisible, "Order Details page was not visible after clicking Radio button");
                        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Order Details page was visible after clicking Radio button");

                        // Assert Service Order ID is found with custom message
                        Assert.assertTrue("95552346".equals(serviceOrdersData), "Service Order ID '95552346' is not found.");
                        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Service Order ID '95552346' is found.");

                        // No pagination in Order Details, proceed to capture details
                        WebElement orderTable = driver.findElement(By.xpath("(//table[@class='w-full table-data'])[2]"));
                        List<WebElement> orderRows = orderTable.findElements(By.tagName("tr"));

                        boolean isWFMIdFound = false; // Track if WFM ID is found
                        for (int j = 1; j < orderRows.size(); j++) {
                            List<WebElement> orderCols = orderRows.get(j).findElements(By.tagName("td"));
                            if (!orderCols.isEmpty()) {
                                String orderDetailsData = orderCols.get(1).getText(); // Assuming WFM ID is in the 2nd column (index 1)

                                // Check if the WFM ID matches the expected value
                                if ("14".equals(orderDetailsData)) {
                                    System.out.println("Captured WFM ID: " + orderDetailsData);
                                    ExtentTestNGListener.getExtentTest().log(Status.INFO, "Captured WFM ID: " + orderDetailsData);

                                    // Click the radio button associated with the WFM ID
                                    WebElement radioButtonWFM = orderRows.get(j).findElement(By.xpath(".//*[name()='svg'][@class='size-6']"));
                                    radioButtonWFM.click();
                                    ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on radio button for WFM ID: " + orderDetailsData);
                                    Thread.sleep(2000); // Wait for the radio button action to complete

                                    // Verify Update WFM page is visible
                                    WebElement verifyUpdateWFMPage = driver.findElement(By.xpath("//h3[normalize-space()='Update WFM']"));
                                    boolean isUpdateWFMPageVisible = verifyUpdateWFMPage.getText().toLowerCase().equals("update wfm");
                                    System.out.println("Update WFM page visibility: " + isUpdateWFMPageVisible);
                                    System.out.println("Text found: " + verifyUpdateWFMPage.getText());
                                    ExtentTestNGListener.getExtentTest().log(Status.INFO, "Update WFM page visibility: " + isUpdateWFMPageVisible);
                                    ExtentTestNGListener.getExtentTest().log(Status.INFO, "Text found: " + verifyUpdateWFMPage.getText());
                                    Assert.assertTrue(isUpdateWFMPageVisible, "Update WFM page was not visible after clicking Radio button");
                                    ExtentTestNGListener.getExtentTest().log(Status.PASS, "Update WFM page was visible after clicking Radio button");

                                    // Assert WFM ID is found with custom message
                                    Assert.assertTrue("14".equals(orderDetailsData), "WFM ID '14' is not found.");
                                    ExtentTestNGListener.getExtentTest().log(Status.PASS, "WFM ID '14' is found.");

                                    // Close the pop-up window
                                    driver.findElement(By.xpath("//button[normalize-space()='Cancel']")).click();
                                    ExtentTestNGListener.getExtentTest().log(Status.INFO, "Closed the pop-up window");

                                    isWFMIdFound = true;
                                    break; // Exit loop as WFM ID is found
                                }
                            }
                        }

                        if (!isWFMIdFound) {
                            System.out.println("WFM ID '14' is not found.");
                            ExtentTestNGListener.getExtentTest().log(Status.FAIL, "WFM ID '14' is not found.");
                            Assert.fail("WFM ID '14' is not found.");
                        }

                        // If Service Order ID & WFM ID is found, mark as Pass
                        if (isOrderDetailsPageVisible && isWFMIdFound) {
                            System.out.println("Service Order ID & WFM ID are found");
                            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Service Order ID & WFM ID are found");

                            // Update summary results in the 'Smoke Test Cases' sheet
                            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
                            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
                            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Updated 'Smoke Test Cases' sheet with 'Pass' status");

                            // Record the timestamp of the test execution in the Excel sheet
                            LocalDateTime now = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                            String formattedNow = now.format(formatter);
                            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
                            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Recorded timestamp in 'Smoke Test Cases' sheet: " + formattedNow);

                            // Close pop-up window
                            driver.findElement(By.xpath("//span[@class='absolute -inset-2.5']")).click();
                            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Closed the pop-up window");
                            Thread.sleep(2000);

                            isServiceOrderFound = true; // Mark the Service Order as found
                            break; // Exit the loop since the Service Order is found
                        }
                    }
                }
            }

            // If Service Order is not found in the current page, check for the presence of the 'Next' button
            if (!isServiceOrderFound) {
                try {
                    WebElement nextButton = driver.findElement(By.xpath("(//button[@type='button'][@class='w-9 h-9 mx-1 bg-white border border-primary-300 rounded outline-none text-primary-300 text-sm disabled:bg-primary-200'])[2]"));
                    String nextBtnStatus = nextButton.getAttribute("disabled");

                    // If the 'Next' button is not disabled, click it
                    if (nextBtnStatus == null) {
                        nextButton.click();
                        //ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked 'Next' button to load next page");
                        Thread.sleep(3000); // Wait for the next page to load
                    } else {
                        // No more pages to search
                        isLastPageReached = true;
                        ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Service Order ID '95552346' is not found after checking all pages.");
                        Assert.fail("Service Order ID '95552346' is not found after checking all pages.");
                    }
                } catch (NoSuchElementException e) {
                    // 'Next' button not found, meaning no additional pages exist
                    isLastPageReached = true;
                    ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Service Order ID '95552346' is not found on the first page, and no more pages exist.");
                    Assert.fail("Service Order ID '95552346' is not found on the first page, and no more pages exist.");
                }
            }
        }

        // End logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: LcoServiceOrdersPageTest completed");
    }
}
