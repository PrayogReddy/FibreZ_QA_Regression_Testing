package TestCases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import Utilities.AppUtils;
import Utilities.ExcelUtils;
import Utilities.ExtentTestNGListener;

@Listeners(ExtentTestNGListener.class)
public class TC031_LcoConnectionsPersonalDetails extends AppUtils {

    @Test
    public void LcoConnectionsPersonalDetailsTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: LcoConnectionsPersonalDetailsTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform LCO login
        lcoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "LCO login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 37;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        boolean isCustomerFound = false;
        boolean isLastPageReached = false; // To track if the last page is reached

        // Start of the while loop
        while (!isCustomerFound && !isLastPageReached) {
            // Click on the Connections link
            driver.findElement(By.xpath("//span[normalize-space()='Connections']")).click();
            Thread.sleep(2000); // Wait for the page to load
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on Connections link");

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

                        // Click the radio button associated with the Customer
                        WebElement radioButton = rows.get(i).findElement(By.xpath(".//*[name()='svg'][@class='size-6']"));
                        radioButton.click();
                        Thread.sleep(2000); // Wait for the radio button action to complete
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on radio button for Customer ID: " + customerIDdata);

                        // Verify Personal Details page is visible
                        WebElement verifyPersonalDetailsPage = driver.findElement(By.xpath("//label[normalize-space()='Personal Details']"));
                        boolean isPersonalDetailsPageVisible = verifyPersonalDetailsPage.getText().toLowerCase().equals("personal details");
                        System.out.println("Personal Details page visibility: " + isPersonalDetailsPageVisible);
                        System.out.println("Text found: " + verifyPersonalDetailsPage.getText());
                        Assert.assertTrue(isPersonalDetailsPageVisible, "Personal Details page was not visible after clicking Radio button");
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Personal Details page visibility: " + isPersonalDetailsPageVisible);
                        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Personal Details page is visible");

                        // Verify if all Personal Details fields are filled
                        boolean isFirstNameFilled = !driver.findElement(By.xpath("//p[contains(text(),'First Name')]/following-sibling::p")).getText().isEmpty();
                        boolean isLastNameFilled = !driver.findElement(By.xpath("//p[contains(text(),'Last Name')]/following-sibling::p")).getText().isEmpty();
                        boolean isGenderFilled = !driver.findElement(By.xpath("//p[contains(text(),'Gender')]/following-sibling::p")).getText().isEmpty();
                        boolean isMobileNumberFilled = !driver.findElement(By.xpath("//p[contains(text(),'Mobile Number')]/following-sibling::p")).getText().isEmpty();
                        boolean isDateOfBirthFilled = !driver.findElement(By.xpath("//p[contains(text(),'Date of Birth')]/following-sibling::p")).getText().isEmpty();
                        boolean isFatherNameFilled = !driver.findElement(By.xpath("//p[contains(text(),'Father Name')]/following-sibling::p")).getText().isEmpty();
                        boolean isEmailFilled = !driver.findElement(By.xpath("//p[contains(text(),'Email ID')]/following-sibling::p")).getText().isEmpty();
                        
                        // Verify if all Communication Address fields are filled
                        boolean isFlatNoFilled = !driver.findElement(By.xpath("//p[contains(text(),'Flat No / Plot No')]/following-sibling::p")).getText().isEmpty();
                        boolean isStreetFilled = !driver.findElement(By.xpath("//p[contains(text(),'Street / Colony')]/following-sibling::p")).getText().isEmpty();
                        boolean isCityFilled = !driver.findElement(By.xpath("//p[contains(text(),'City / District')]/following-sibling::p")).getText().isEmpty();
                        boolean isStateFilled = !driver.findElement(By.xpath("//p[contains(text(),'State')]/following-sibling::p")).getText().isEmpty();
                        boolean isAddressType = !driver.findElement(By.xpath("//p[contains(text(),'Address Type')]/following-sibling::p")).getText().isEmpty();
                        boolean isPincodeFilled = !driver.findElement(By.xpath("//p[contains(text(),'Pincode')]/following-sibling::p")).getText().isEmpty();

                        // Verify if all Billing Address fields are filled
                        boolean isBillingFlatNoFilled = !driver.findElement(By.xpath("(//p[contains(text(),'Flat No / Plot No')])[2]/following-sibling::p")).getText().isEmpty();
                        boolean isBillingStreetFilled = !driver.findElement(By.xpath("(//p[contains(text(),'Street / Colony')])[2]/following-sibling::p")).getText().isEmpty();
                        boolean isBillingCityFilled = !driver.findElement(By.xpath("(//p[contains(text(),'City / District')])[2]/following-sibling::p")).getText().isEmpty();
                        boolean isBillingStateFilled = !driver.findElement(By.xpath("(//p[contains(text(),'State')])[2]/following-sibling::p")).getText().isEmpty();
                        boolean isBillingAddressType = !driver.findElement(By.xpath("//p[contains(text(),'Address Type')]/following-sibling::p")).getText().isEmpty();
                        boolean isBillingPincodeFilled = !driver.findElement(By.xpath("(//p[contains(text(),'Pincode')])[2]/following-sibling::p")).getText().isEmpty();

                        // Verify if all Plan Details fields are filled
                        boolean isPlanNameFilled = !driver.findElement(By.xpath("//p[contains(text(),'Plan Name')]/following-sibling::p")).getText().isEmpty();
                        boolean isVoiceFilled = !driver.findElement(By.xpath("//p[contains(text(),'Voice')]/following-sibling::p")).getText().isEmpty();
                        boolean isInternetSpeedFilled = !driver.findElement(By.xpath("//p[contains(text(),'Internet Speed')]/following-sibling::p")).getText().isEmpty();
                        boolean isTvFilled = !driver.findElement(By.xpath("//p[contains(text(),'TV')]/following-sibling::p")).getText().isEmpty();
                        boolean isOttFilled = !driver.findElement(By.xpath("//p[contains(text(),'OTT')]/following-sibling::p")).getText().isEmpty();
                        boolean isPriceFilled = !driver.findElement(By.xpath("//p[contains(text(),'Price')]/following-sibling::p")).getText().isEmpty();

                        // If all fields are filled, mark as Customer found
                        if (isPersonalDetailsPageVisible && isFirstNameFilled && isLastNameFilled && isGenderFilled && isMobileNumberFilled 
                                && isDateOfBirthFilled && isFatherNameFilled && isEmailFilled
                                && isFlatNoFilled && isStreetFilled && isCityFilled && isStateFilled && isAddressType && isPincodeFilled
                                && isBillingFlatNoFilled && isBillingStreetFilled && isBillingCityFilled && isBillingStateFilled && isBillingAddressType && isBillingPincodeFilled
                                && isPlanNameFilled && isVoiceFilled && isInternetSpeedFilled && isTvFilled && isOttFilled && isPriceFilled) {
                            System.out.println("All fields are filled.");
                            ExtentTestNGListener.getExtentTest().log(Status.INFO, "All fields are filled for Customer ID: " + customerIDdata);

                            // Update summary results in the 'Smoke Test Cases' sheet
                            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
                            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
                            ExtentTestNGListener.getExtentTest().log(Status.PASS, "Test results updated in Excel with 'Pass' status");

                            // Record the timestamp of the test execution in the Excel sheet
                            LocalDateTime now = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                            String formattedNow = now.format(formatter);
                            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
                            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test results updated in Excel with timestamp: " + formattedNow);

                            isCustomerFound = true;
                        }
                    }
                }
            }

            // If Customer is not found in the current page, check for the presence of the 'Next' button
            if (!isCustomerFound) {
                try {
                    WebElement nextButton = driver.findElement(By.xpath("(//button[@type='button'][@class='w-9 h-9 mx-1 bg-white border border-primary-300 rounded outline-none text-primary-300 text-sm disabled:bg-primary-200'])[2]"));
                    String nextBtnStatus = nextButton.getAttribute("disabled");

                    // If the 'Next' button is not disabled, click it
                    if (nextBtnStatus == null) {
                        nextButton.click();
                        Thread.sleep(3000); // Wait for the next page to load
                        //ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on the Next Page button");
                    } else {
                        // No more pages to search
                        isLastPageReached = true;
                        System.out.println("Customer ID 'TC61366568' not found after checking all pages.");
                        Assert.fail("Customer ID 'TC61366568' not found after checking all pages.");
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "No more pages to search; Last page reached or Next Page button is disabled");
                    }
                } catch (NoSuchElementException e) {
                    // 'Next' button not found, meaning no additional pages exist
                    isLastPageReached = true;
                    System.out.println("Customer ID 'TC61366568' not found on the first page, and no more pages exist.");
                    Assert.fail("Customer ID 'TC61366568' not found on the first page, and no more pages exist.");
                    ExtentTestNGListener.getExtentTest().log(Status.INFO, "No Next Page button found, assuming last page reached");
                }
            }
        }
    }
}
