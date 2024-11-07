package Utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentTestNGListener implements ITestListener {

    private static final String OUTPUT_FOLDER = System.getProperty("user.dir") + "/ExtentReports/";
    private static ExtentReports extent;
    private static ExtentSparkReporter sparkReporter;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static List<ExtentTest> testList = new ArrayList<>();
    private String reportPath;

    // Getter method for ExtentTest
    public static ExtentTest getExtentTest() {
        return extentTest.get();
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Test Suite started!");

        String suiteName = context.getSuite().getName();
        String testName = context.getCurrentXmlTest().getName();

        if (suiteName.equalsIgnoreCase("Default suite") || testName.equalsIgnoreCase("Default test")) {
            suiteName = context.getAllTestMethods()[0].getTestClass().getRealClass().getSimpleName();
            testName = context.getAllTestMethods()[0].getMethodName();
        }

        String timestamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String reportFileName = OUTPUT_FOLDER + suiteName.replace(" ", "_") + "_Report_" + timestamp + ".html";

        Path path = Paths.get(OUTPUT_FOLDER);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        extent = new ExtentReports();
        sparkReporter = new ExtentSparkReporter(reportFileName);        
        this.reportPath = reportFileName; // Save report path for use in onFinish

        sparkReporter.config().setReportName(suiteName + " - Test Results");
        sparkReporter.config().setDocumentTitle(suiteName + " - Report");
        sparkReporter.config().setTheme(Theme.STANDARD); // Optionally, set to DARK for a dark theme

//        // Set the path to the local CSS file
//        sparkReporter.config().setCss(OUTPUT_FOLDER + "spark-style.css");
//        sparkReporter.config().setCss(OUTPUT_FOLDER + "fontawesome.min.css");
        
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("System", "Windows");
        extent.setSystemInfo("Name", "Prayog");
        extent.setSystemInfo("Build#", "1.1");
        extent.setSystemInfo("Team", "QA");
        extent.setSystemInfo("Customer Name", "FibreZ");
        extent.setSystemInfo("Test Type", "Smoke Testing");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        System.out.println(methodName + " started!");
        ExtentTest test = extent.createTest(methodName, result.getMethod().getDescription());

//      String className = result.getTestClass().getRealClass().getSimpleName();
//
//      // Assign category using class name
//      test.assignCategory(className);
//
//      // Assign tags (optional: you can add more tags here, if needed)
//      test.assignCategory(result.getTestContext().getSuite().getName());
        
        extentTest.set(test);
        extentTest.get().getModel().setStartTime(getTime(result.getStartMillis()));

        // Add test to the list as soon as it starts, to maintain the order of execution
        testList.add(extentTest.get());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() + " passed!");
        ExtentTest test = extentTest.get();
        test.pass("Test passed");
        test.getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() + " failed!");
        ExtentTest test = extentTest.get();
        test.fail(result.getThrowable());
        test.getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() + " skipped!");
        ExtentTest test = extentTest.get();
        test.skip(result.getThrowable());
        test.getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test Suite is ending!");

//        // Sort test cases by start time
//        testList.sort(Comparator.comparing(t -> t.getModel().getStartTime()));
//
//        // Log the sorted tests to verify order (optional)
//        for (ExtentTest test : testList) {
//        	System.out.println("Test: " + test.getModel().getName() + " - Start Time: " + test.getModel().getStartTime());
//        }

        // Flush the extent report
        extent.flush();

        // Attempt to open the report automatically upon test completion
        File extentReport = new File(reportPath);
        try {
            Desktop.getDesktop().browse(extentReport.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
}
