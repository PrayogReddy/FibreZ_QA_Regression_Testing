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

import com.aventstack.extentreports.Status;

import Utilities.AppUtils;
import Utilities.ExcelUtils;
import Utilities.ExtentTestNGListener;

@Listeners(ExtentTestNGListener.class)
public class TC035_LcoPartnersPage extends AppUtils {

    @Test
    public void LcoListOfPartnersPageTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: LcoListOfPartnersPageTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform LCO login
        lcoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "LCO login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 41;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        boolean isPartnerFound = false;
        boolean isLastPageReached = false; // To track if the last page is reached

        // Start of the while loop
        while (!isPartnerFound && !isLastPageReached) {
            // Click on the Partners link
            driver.findElement(By.xpath("//span[normalize-space()='Partners']")).click();
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on 'Partners' link");
            Thread.sleep(2000); // Wait for the page to load

            // Capture all the data of the web table in Partners
            WebElement partnerTable = driver.findElement(By.xpath("//table[@class='w-full table-data']"));
            List<WebElement> rows = partnerTable.findElements(By.tagName("tr"));

            // Iterate through each row to find the specific Partner
            for (int i = 1; i < rows.size(); i++) {
                List<WebElement> cols = rows.get(i).findElements(By.tagName("td"));
                if (!cols.isEmpty()) {
                    String partnerIDdata = cols.get(2).getText(); // Assuming Partner ID is in the 3rd column (index 2)

                    // Check if the Partner ID matches the expected value
                    if ("PAT8334186".equals(partnerIDdata)) {
                        // Print the captured Partner ID
                        System.out.println("Captured Partner ID: " + partnerIDdata);
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Captured Partner ID: " + partnerIDdata);

                        // Click the radio button associated with the Partner
                        WebElement radioButton = rows.get(i).findElement(By.xpath(".//*[name()='svg'][@class='size-6']"));
                        radioButton.click();
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on the radio button for Partner ID: " + partnerIDdata);
                        Thread.sleep(2000); // Wait for the radio button action to complete

                        // Verify Partner Details page is visible
                        WebElement verifyPartnerDetailsPage = driver.findElement(By.xpath("//label[normalize-space()='Partner Details']"));
                        boolean isPartnerDetailsPageVisible = verifyPartnerDetailsPage.getText().toLowerCase().equals("partner details");
                        System.out.println("Partner Details page visibility: " + isPartnerDetailsPageVisible);
                        System.out.println("Text found: " + verifyPartnerDetailsPage.getText());
                        Assert.assertTrue(isPartnerDetailsPageVisible, "Partner Details page was not visible after clicking Radio button");
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Partner Details page visibility: " + isPartnerDetailsPageVisible);
                        ExtentTestNGListener.getExtentTest().log(Status.PASS, "Partner Details page was visible");

                        // Verify if all required fields are filled
                        WebElement firstNameField = driver.findElement(By.xpath("(//input[@id='floating_outlined'])[1]"));
                        WebElement lastNameField = driver.findElement(By.xpath("(//input[@id='floating_outlined'])[2]"));
                        WebElement mobileNumberField = driver.findElement(By.xpath("(//input[@id='floating_outlined'])[3]"));
                        WebElement emailField = driver.findElement(By.xpath("(//input[@id='floating_outlined'])[4]"));
                        WebElement flatNoField = driver.findElement(By.xpath("(//input[@id='floating_outlined'])[5]"));
                        WebElement streetField = driver.findElement(By.xpath("(//input[@id='floating_outlined'])[6]"));
                        WebElement areaField = driver.findElement(By.xpath("(//input[@id='floating_outlined'])[7]"));
                        WebElement cityField = driver.findElement(By.xpath("(//input[@id='floating_outlined'])[8]"));
                        WebElement stateField = driver.findElement(By.xpath("(//input[@id='floating_outlined'])[9]"));
                        WebElement pincodeField = driver.findElement(By.xpath("(//input[@id='floating_outlined'])[10]"));
                        WebElement roleDetailsField = driver.findElement(By.xpath("//div[@class='dropdown-heading-value']"));

                        // Check if all required fields are filled
                        boolean isFirstNameFilled = !firstNameField.getAttribute("value").isEmpty();
                        boolean isLastNameFilled = !lastNameField.getAttribute("value").isEmpty();
                        boolean isMobileNumberFilled = !mobileNumberField.getAttribute("value").isEmpty();
                        boolean isEmailFilled = !emailField.getAttribute("value").isEmpty();
                        boolean isFlatNoFilled = !flatNoField.getAttribute("value").isEmpty();
                        boolean isStreetFilled = !streetField.getAttribute("value").isEmpty();
                        boolean isAreaFilled = !areaField.getAttribute("value").isEmpty();
                        boolean isCityFilled = !cityField.getAttribute("value").isEmpty();
                        boolean isStateFilled = !stateField.getAttribute("value").isEmpty();
                        boolean isPincodeFilled = !pincodeField.getAttribute("value").isEmpty();
                        boolean isroleDetailsFilled = !roleDetailsField.getText().isEmpty();

                        // If all fields are filled, mark as partner found
                        if (isPartnerDetailsPageVisible && isFirstNameFilled && isLastNameFilled && isMobileNumberFilled && isEmailFilled
                                && isFlatNoFilled && isStreetFilled && isAreaFilled && isCityFilled
                                && isStateFilled && isPincodeFilled && isroleDetailsFilled) {
                            System.out.println("All fields are filled.");
                            ExtentTestNGListener.getExtentTest().log(Status.INFO, "All fields are filled.");

                            // Update summary results in the 'Smoke Test Cases' sheet
                            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
                            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
                            ExtentTestNGListener.getExtentTest().log(Status.PASS, "All required fields are filled and 'Pass' result updated in Excel.");

                            // Record the timestamp of the test execution in the Excel sheet
                            LocalDateTime now = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                            String formattedNow = now.format(formatter);
                            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
                            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test result updated in Excel with timestamp: " + formattedNow);

                            // Close Pop up window
                            driver.findElement(By.xpath("//span[@class='absolute -inset-2.5']")).click();
                            Thread.sleep(3000);
                            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Closed the pop-up window");

                            isPartnerFound = true; // Mark the partner as found
                            break; // Exit the loop since the partner is found
                        } else {
                            System.out.println("All fields are not filled.");
                            ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Some fields are not filled for the found partner.");
                            Assert.fail("Some fields are not filled for the found partner.");
                        }
                    }
                }
            }

            // If Partner is not found in the current page, check for the presence of the 'Next' button
            if (!isPartnerFound) {
                try {
                    WebElement nextButton = driver.findElement(By.xpath("(//button[@type='button'][@class='w-9 h-9 mx-1 bg-white border border-primary-300 rounded outline-none text-primary-300 text-sm disabled:bg-primary-200'])[2]"));
                    String nextBtnStatus = nextButton.getAttribute("disabled");

                    // If the 'Next' button is not disabled, click it
                    if (nextBtnStatus == null) {
                        nextButton.click();
                        //ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on 'Next' button to go to the next page");
                        Thread.sleep(3000); // Wait for the next page to load
                    } else {
                        // No more pages to search
                        isLastPageReached = true;
                        System.out.println("Partner ID 'PAT8334186' not found after checking all pages.");
                        ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Partner ID 'PAT8334186' not found after checking all pages.");
                        Assert.fail("Partner ID 'PAT8334186' not found after checking all pages.");
                    }
                } catch (NoSuchElementException e) {
                    // 'Next' button not found, meaning no additional pages exist
                    isLastPageReached = true;
                    System.out.println("Partner ID 'PAT8334186' not found on the first page, and no more pages exist.");
                    ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Partner ID 'PAT8334186' not found on the first page, and no more pages exist.");
                    Assert.fail("Partner ID 'PAT8334186' not found on the first page, and no more pages exist.");
                }
            }
        }

        // End of the test case
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: LcoListOfPartnersPageTest ended");
    }
}
