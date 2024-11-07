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
public class TC006_AdminPartnersPage extends AppUtils {

    @Test
    public void check_Manjula_MSO_Test() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: check_Manjula_MSO_Test started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);
        
        // Perform admin login
        adminLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Admin login performed");
        
        // Click on the Partners link
        driver.findElement(By.xpath("//span[normalize-space()='Partners']")).click();
        Thread.sleep(3000);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on Partners link");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 12;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        boolean isPartnerFound = false;
        boolean isLastPageReached = false; // To track if the last page is reached

        while (!isPartnerFound && !isLastPageReached) {
            // Wait for the customer table to be present
            WebElement customerTable = driver.findElement(By.xpath("//table[@class='w-full table-data']"));
            List<WebElement> rows = customerTable.findElements(By.tagName("tr"));

            // Iterate through each row to find the specific partner
            for (int i = 1; i < rows.size(); i++) {
                List<WebElement> cols = rows.get(i).findElements(By.tagName("td"));
                if (!cols.isEmpty()) {
                    String accountIDdata = cols.get(2).getText(); // Assuming Partner ID is in the 3rd column (index 2)

                    // Check if the Partner ID matches the expected value
                    if ("PAT7654592".equals(accountIDdata)) {
                        // Print the captured Partner ID
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Captured Partner ID: " + accountIDdata);

                        // Click the radio button associated with the partner
                        WebElement radioButton = rows.get(i).findElement(By.xpath(".//*[name()='svg'][@class='size-6']"));
                        radioButton.click();
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on radio button for Partner ID: " + accountIDdata);

                        // Verify if all required fields are filled
                        boolean isFirstNameFilled = !driver.findElement(By.xpath("(//input[@id='floating_outlined'])[1]")).getAttribute("value").isEmpty();
                        boolean isLastNameFilled = !driver.findElement(By.xpath("(//input[@id='floating_outlined'])[2]")).getAttribute("value").isEmpty();
                        boolean isMobileNumberFilled = !driver.findElement(By.xpath("(//input[@id='floating_outlined'])[3]")).getAttribute("value").isEmpty();
                        boolean isEmailFilled = !driver.findElement(By.xpath("(//input[@id='floating_outlined'])[4]")).getAttribute("value").isEmpty();
                        boolean isFlatNoFilled = !driver.findElement(By.xpath("(//input[@id='floating_outlined'])[5]")).getAttribute("value").isEmpty();
                        boolean isStreetFilled = !driver.findElement(By.xpath("(//input[@id='floating_outlined'])[6]")).getAttribute("value").isEmpty();
                        boolean isAreaFilled = !driver.findElement(By.xpath("(//input[@id='floating_outlined'])[7]")).getAttribute("value").isEmpty();
                        boolean isCityFilled = !driver.findElement(By.xpath("(//input[@id='floating_outlined'])[8]")).getAttribute("value").isEmpty();
                        boolean isStateFilled = !driver.findElement(By.xpath("(//input[@id='floating_outlined'])[9]")).getAttribute("value").isEmpty();
                        boolean isPincodeFilled = !driver.findElement(By.xpath("(//input[@id='floating_outlined'])[10]")).getAttribute("value").isEmpty();

                        // If all fields are filled, mark as partner found
                        if (isFirstNameFilled && isLastNameFilled && isMobileNumberFilled && isEmailFilled
                                && isFlatNoFilled && isStreetFilled && isAreaFilled && isCityFilled
                                && isStateFilled && isPincodeFilled) {
                            ExtentTestNGListener.getExtentTest().log(Status.INFO, "All fields are filled for Partner ID: " + accountIDdata);                            

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
                            
                            isPartnerFound = true; // Mark the partner as found
                            break; // Exit the loop since the partner is found
                        } else {
                            ExtentTestNGListener.getExtentTest().log(Status.INFO, "All fields are not filled for Partner ID: " + accountIDdata);
                        }
                    }
                }
            }

            // Check if the partner was not found
            if (!isPartnerFound) {
                try {
                    // Find and click the 'Next' button to go to the next page
                    WebElement nextButton = driver.findElement(By.xpath("(//button[contains(@class,'h-[30px] w-[30px] m-1 text-sm font-medium rounded-md border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-indigo-500')][normalize-space()='>'])[1]"));
                    if (nextButton.isEnabled()) {
                        nextButton.click();
                        Thread.sleep(3000); // Wait for the next page to load
                        //ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on 'Next' button to go to next page");
                    } else {
                        // No more pages to search
                        isLastPageReached = true;
                        ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Partner ID 'PAT7654592' not found after checking all pages.");
                        Assert.fail("Partner ID 'PAT7654592' not found after checking all pages.");
                    }
                } catch (NoSuchElementException e) {
                    // 'Next' button not found, meaning no additional pages exist
                    isLastPageReached = true;
                    ExtentTestNGListener.getExtentTest().log(Status.FAIL, "Partner ID 'PAT7654592' not found on the first page, and no more pages exist.");
                    Assert.fail("Partner ID 'PAT7654592' not found on the first page, and no more pages exist.");
                }
            }
        }
    }
}
