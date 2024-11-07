package TestCases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class TC008_EditRoleRollBack extends AppUtils {

    @Test
    public void editRoleChangeRollBackTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: editRoleChangeRollBackTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);
        
        // Perform admin login
        adminLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Admin login performed");
        
        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 14;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Navigate to Roles Page
        driver.findElement(By.xpath("//span[normalize-space()='Roles']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to Roles Page");
        Thread.sleep(3000);
        
        boolean isRoleFound = false;

        // Loop to handle pagination
        while (!isRoleFound) {
            // Capture all the data of the web table in Roles
            WebElement rolesTable = driver.findElement(By.xpath("//table[@class='w-full table-data']"));
            List<WebElement> rows = rolesTable.findElements(By.tagName("tr"));

            // Iterate through each row to find the specific role
            for (int i = 1; i < rows.size(); i++) {
                List<WebElement> cols = rows.get(i).findElements(By.tagName("td"));
                if (!cols.isEmpty()) {
                    String roleName = cols.get(1).getText(); // Assuming Role Name is in the 2nd column (index 1)

                    if ("MSO_1".equals(roleName)) {
                        WebElement radioButton = rows.get(i).findElement(By.xpath(".//*[name()='svg'][@class='size-6']"));
                        radioButton.click();
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Selected role 'MSO_1'");
                        Thread.sleep(3000);
                        
                        driver.findElement(By.xpath("//p[text()='Edit']")).click();
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked Edit button for role 'MSO_1'");
                        Thread.sleep(3000);

                        isRoleFound = true; // Role is found and edit action is performed
                        break; // Exit the loop once the role is found and edited
                    }
                }
            }

            // If the role is not found, click the 'Next' button to go to the next page
            if (!isRoleFound) {
                WebElement nextButton = driver.findElement(By.xpath("(//button[contains(@class,'h-[30px] w-[30px] m-1 text-sm font-medium rounded-md border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-indigo-500')][normalize-space()='>'])[1]"));
                if (nextButton.isEnabled()) {
                    nextButton.click();
                    ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked 'Next' button to go to the next page");
                    Thread.sleep(3000); // Wait for the next page to load
                } else {
                    // If no more pages are available, break the loop
                    ExtentTestNGListener.getExtentTest().log(Status.WARNING, "No more pages available. Role 'MSO_1' not found.");
                    System.out.println("No more pages available. Role 'MSO_1' not found.");
                    break;
                }
            }
        }

        // Assert that the role was found
        Assert.assertTrue(isRoleFound, "Role 'MSO_1' not found in the Roles table.");
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Role 'MSO_1' found in the Roles table.");
        
        // Uncheck specific checkboxes based on text
        String[] textsToCheck = {"LCO DashBoard", "Service Orders"}; // Replace with actual texts to uncheck
        if (isRoleFound) {
            checkCheckboxesByText(textsToCheck);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Unchecked checkboxes based on text");
            Thread.sleep(3000);
            
            // Click the 'Upgrade Role' button
            driver.findElement(By.xpath("//button[normalize-space()='Upgrade Role']")).click();
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked 'Upgrade Role' button");
            Thread.sleep(3000);
            
            // Verify success message is displayed
            boolean successElement = driver.findElement(By.xpath("//p[@class='text-xl' and text()='Success']")).isDisplayed();            
            driver.findElement(By.xpath("//button[normalize-space()='Ok']")).click();
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Verified success message and clicked 'Ok'");
            
            // Assert that the success message was displayed
            Assert.assertTrue(successElement, "Success message not displayed. Rollback failed.");
            
            // Update summary results in the 'Smoke Test Cases' sheet
            if (successElement) {
                ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
                ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
                ExtentTestNGListener.getExtentTest().log(Status.PASS, "Test results updated in Excel with 'Pass' status");

                // Record the timestamp of the test execution in the Excel files
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedNow = now.format(formatter);
                ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
                ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test results updated in Excel with timestamp: " + formattedNow);
            }
        }
    }

    private void checkCheckboxesByText(String[] textsToCheck) throws InterruptedException {
        // Find all <li> elements that contain checkboxes
        List<WebElement> privileges = driver.findElements(By.xpath("//li[contains(@class,'text-sm font-lexendDecaLight leading-6')]"));

        for (WebElement privilege : privileges) {
            String text = privilege.getText();
            // Check if the text is in the provided array
            for (String checkText : textsToCheck) {
                if (text.contains(checkText)) {
                    WebElement checkbox = privilege.findElement(By.xpath(".//input[@type='checkbox']"));
                    // Check if the checkbox is already checked
                    if (checkbox.isSelected()) {
                        checkbox.click();
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Unchecked checkbox with text: " + text);
                        Thread.sleep(1000);
                    }
                    break; // Exit the inner loop once the checkbox is unchecked
                }
            }
        }
    }
}
