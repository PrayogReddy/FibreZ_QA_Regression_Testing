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
public class TC022_MsoPartnersPage extends AppUtils {

    @Test
    public void check_Arjun_LCO_Test() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: check_Arjun_LCO_Test started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);

        // Perform mso login
        msoLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "MSO login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 28;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        boolean isPartnerFound = false;

        // Iterate through pages to find the desired Partner ID
        while (!isPartnerFound) {
            // Click on the Partners link
            driver.findElement(By.xpath("//span[normalize-space()='Partners']")).click();
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to Partners Page");
            Thread.sleep(3000); // Wait for the page to load

            // Capture all the data of the web table in Partners
            WebElement customerTable = driver.findElement(By.xpath("//table[@class='w-full table-data']"));
            List<WebElement> rows = customerTable.findElements(By.tagName("tr"));

            // Iterate through each row to find the specific partner
            for (int i = 1; i < rows.size(); i++) {
                List<WebElement> cols = rows.get(i).findElements(By.tagName("td"));
                if (!cols.isEmpty()) {
                    String accountIDdata = cols.get(2).getText(); // Assuming Partner ID is in the 3rd column (index 2)

                    // Check if the Partner ID matches the expected value
                    if ("PAT5811771".equals(accountIDdata)) {
                        // Print the captured Partner ID
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Captured Partner ID: " + accountIDdata);

                        // Click the radio button associated with the partner
                        WebElement radioButton = rows.get(i).findElement(By.xpath(".//*[name()='svg'][@class='size-6']"));
                        radioButton.click();
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Selected Partner ID: " + accountIDdata);
                        Thread.sleep(3000); // Wait for the radio button action to complete

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

                        // If all fields are filled, mark as partner found
                        if (isFirstNameFilled && isLastNameFilled && isMobileNumberFilled && isEmailFilled
                                && isFlatNoFilled && isStreetFilled && isAreaFilled && isCityFilled
                                && isStateFilled && isPincodeFilled) {
                            ExtentTestNGListener.getExtentTest().log(Status.PASS, "All fields are filled for Partner ID: " + accountIDdata);

                            // Update summary results in the 'Smoke Test Cases' sheet
                            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
                            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);

                            // Record the timestamp of the test execution in the Excel sheet
                            LocalDateTime now = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                            String formattedNow = now.format(formatter);
                            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
                            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test results updated in Excel with timestamp: " + formattedNow);

                            driver.findElement(By.xpath("//span[@class='absolute -inset-2.5']")).click();
                            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on the element to exit the details view");
                            Thread.sleep(3000);

                            isPartnerFound = true; // Mark the partner as found
                            break; // Exit the loop since the partner is found
                        } else {
                            ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Some fields are not filled for the found customer.");
                            System.out.println("All fields are not filled.");
                            Assert.fail("Some fields are not filled for the found customer.");
                        }
                    }
                }
            }

            // Check if the partner was found
            if (!isPartnerFound) {
                try {
                    // Click the 'Next' button to go to the next page
                    WebElement nextButton = driver.findElement(By.xpath("(//button[contains(@class,'h-[30px] w-[30px] m-1 text-sm font-medium rounded-md border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-indigo-500')][normalize-space()='>'])[1]"));
                    if (nextButton.isEnabled()) {
                        nextButton.click();
                        //ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on 'Next' button to go to the next page");
                        Thread.sleep(3000); // Wait for the next page to load
                    } else {
                        // If no more pages, throw an exception to indicate failure
                        ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Partner ID 'PAT5811771' not found in the Partners table.");
                        Assert.fail("Partner ID 'PAT5811771' not found in the Partners table.");
                        break;
                    }
                } catch (NoSuchElementException e) {
                    // 'Next' button not found, meaning no additional pages exist
                    ExtentTestNGListener.getExtentTest().log(Status.FAIL, "No more pages available. Partner ID 'PAT5811771' not found.");
                    System.out.println("No more pages available. Partner ID 'PAT5811771' not found.");
                    Assert.fail("Partner ID 'PAT5811771' not found in the Partners table.");
                }
            }
        }
    }
}
