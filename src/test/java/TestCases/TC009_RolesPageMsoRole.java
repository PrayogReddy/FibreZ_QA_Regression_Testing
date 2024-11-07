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
public class TC009_RolesPageMsoRole extends AppUtils {

    @Test
    public void rolesPageMsoRoleTest() throws Throwable {
        // Start logging in Extent Report
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test Case: rolesPageMsoRoleTest started");

        // Open the application URL
        driver.get(url);
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to URL: " + url);
        
        // Perform admin login
        adminLogin();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Admin login performed");

        // Define row and column numbers for test data and smoke testing results
        int smokeTestRowNum = 15;
        int smokeTestColNum = 12;
        int smokeTestTimestampColNum = 15;

        // Navigate to Roles Page
        driver.findElement(By.xpath("//span[normalize-space()='Roles']")).click();
        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Navigated to Roles Page");
        Thread.sleep(3000);
        
        boolean isMsoRoleFound = false;
        boolean isLastPageReached = false; // To track if the last page is reached

        while (!isMsoRoleFound && !isLastPageReached) {
            // Capture all the data of the web table in Roles
            WebElement rolesTable = driver.findElement(By.xpath("//table[@class='w-full table-data']"));
            List<WebElement> rows = rolesTable.findElements(By.tagName("tr"));

            // Iterate through each row to find the specific role
            for (int i = 1; i < rows.size(); i++) {
                List<WebElement> cols = rows.get(i).findElements(By.tagName("td"));
                if (!cols.isEmpty()) {
                    String roleName = cols.get(1).getText(); // Assuming Role Name is in the 2nd column (index 1)

                    // Check if the role contains "MSO"
                    if ("MSO".equals(roleName)) {
                        WebElement msoRoleName = cols.get(1).findElement(By.xpath(".//span[contains(@class,'font-lexendDecaLight')]"));
                        boolean isMsoRoleNameDisplayed = msoRoleName.isDisplayed();
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "MSO Role icon visibility: " + isMsoRoleNameDisplayed);
                        Assert.assertTrue(isMsoRoleNameDisplayed, "MSO Role icon is not displayed!");

                        String displayedText = msoRoleName.getText();
                        System.out.println("Text of MSO Role icon: " + displayedText);
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Text of MSO Role icon: " + displayedText);

                        isMsoRoleFound = true; // Set to true if the role is found
                        break; // Exit the row loop once the role is found
                    }
                }
            }

            // Check if the role was not found
            if (!isMsoRoleFound) {
                try {
                    // Find and click the 'Next' button to go to the next page
                    WebElement nextButton = driver.findElement(By.xpath("(//button[contains(@class,'h-[30px] w-[30px] m-1 text-sm font-medium rounded-md border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-indigo-500')][normalize-space()='>'])[1]"));
                    if (nextButton.isEnabled()) {
                        nextButton.click();
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Clicked on 'Next' button");
                        Thread.sleep(3000); // Wait for the next page to load
                    } else {
                        // No more pages to search
                        isLastPageReached = true;
                        System.out.println("Role 'MSO' not found after checking all pages.");
                        ExtentTestNGListener.getExtentTest().log(Status.INFO, "Role 'MSO' not found after checking all pages.");
                        Assert.fail("Role 'MSO' not found after checking all pages.");
                    }
                } catch (NoSuchElementException e) {
                    // 'Next' button not found, meaning no additional pages exist
                    isLastPageReached = true;
                    System.out.println("Role 'MSO' not found on the first page, and no more pages exist.");
                    ExtentTestNGListener.getExtentTest().log(Status.INFO, "Role 'MSO' not found on the first page, and no more pages exist.");
                    Assert.fail("Role 'MSO' not found on the first page, and no more pages exist.");
                }
            }
        }

        // Update summary results in the 'Smoke Test Cases' sheet
        if (isMsoRoleFound) {
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum, "Pass");
            ExcelUtils.fillGreenColor(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestColNum);
            ExtentTestNGListener.getExtentTest().log(Status.PASS, "MSO Role found successfully. Results updated in Excel.");

            // Record the timestamp of the test execution in the Excel files
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedNow = now.format(formatter);
            ExcelUtils.setCellData(smokeTestingFilePath, smokeTestingSheetName, smokeTestRowNum, smokeTestTimestampColNum, formattedNow);
            ExtentTestNGListener.getExtentTest().log(Status.INFO, "Test results updated in Excel with timestamp: " + formattedNow);
        }
    }
}
