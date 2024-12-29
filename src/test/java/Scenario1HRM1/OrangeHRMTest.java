package Scenario1HRM1;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;

public class OrangeHRMTest extends BaseTest {
    WebDriverWait wait;
    ExtentReports extent;
    ExtentTest test;

    // Specify the file path and sheet name
    String excelFilePath = "C:\\Lakshmana_seleniumDemo\\Scenario1HRM1\\DataSheet2\\Data.xlsx";
    String sheetName = "Sheet1";

    @BeforeSuite
    public void setUpExtent() {
        extent = ExtentManager.createInstance();
    }

    @BeforeClass
    public void setUp() {
        // Correct path to EdgeDriver executable
        System.setProperty("webdriver.edge.driver", "C:\\Users\\klaks\\Downloads\\edgedriver_win64\\msedgedriver.exe");

        // EdgeOptions setup
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--remote-allow-origins=*");

        driver = new EdgeDriver(options); // Ensure driver is initialized here
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Initialize WebDriverWait with 20 seconds timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    public void loginTest() throws IOException, InterruptedException {
        test = extent.createTest("loginTest");

        // Set the Excel file path and sheet name
        ExcelUtils.setExcelFile(excelFilePath, sheetName);

        // Loop through the rows in the Excel file
        for (int i = 1; i <= ExcelUtils.getRowCount(); i++) {
            String username = ExcelUtils.getCellData(i, 0);
            String password = ExcelUtils.getCellData(i, 1);

            // Ensure you have the correct URL
            driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
            test.log(Status.INFO, "Navigated to OrangeHRM login page");

            // Use relative XPath for username field
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='username']"))).sendKeys(username);
            test.log(Status.INFO, "Entered username");

            // Use relative XPath for password field
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='password']"))).sendKeys(password);
            test.log(Status.INFO, "Entered password");

            // Use relative XPath for login button
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'oxd-button')]"))).click();
            test.log(Status.INFO, "Clicked on login button");

            // Capture screenshot after login attempt
            String loginAttemptScreenshot = captureScreenshot("LoginAttempt_" + i);
            test.addScreenCaptureFromPath(loginAttemptScreenshot);

            try {
                // Perform assertions for valid and invalid data sets
                if (username.equals("Admin") && password.equals("admin123")) {
                    // Relative XPath for successful login indicator
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class, 'oxd-userdropdown-tab')]")));  // Assuming the username appears here after login
                    assert driver.findElement(By.xpath("//span[contains(@class, 'oxd-userdropdown-tab')]")).isDisplayed();
                    test.log(Status.PASS, "Login successful");
                } else {
                    if (isElementPresent(By.xpath("//div[@class='oxd-alert-content']"))) {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='oxd-alert-content']")));
                        assert driver.findElement(By.xpath("//div[@class='oxd-alert-content']")).isDisplayed();
                        test.log(Status.PASS, "Login failed with invalid credentials");

                    } else {
                        test.log(Status.FAIL, "Error message element not found");
                    }
                }
            } catch (AssertionError e) {
                test.log(Status.FAIL, "Assertion failed: " + e.getMessage());
            }

            // Capture screenshot after assertion
            String afterAssertionScreenshot = captureScreenshot("AfterAssertion_" + i);
            test.addScreenCaptureFromPath(afterAssertionScreenshot);

            // Add logout functionality if login is successful
            if (isElementPresent(By.xpath("//span[contains(@class, 'oxd-userdropdown-tab')]"))) {
                driver.findElement(By.xpath("//span[contains(@class, 'oxd-userdropdown-tab')]")).click();  // Adjusted relative XPath for logout dropdown
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Logout')]"))).click();
                // Capture screenshot after logout
                String logoutScreenshot = captureScreenshot("Logout_" + i);
                test.addScreenCaptureFromPath(logoutScreenshot);
            }
        }
    }

    @AfterMethod
    public void getResult(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, "Test Case Failed is " + result.getName()); // to add name in extent report
            test.log(Status.FAIL, "Test Case Failed is " + result.getThrowable()); // to add error/exception in extent report
            String screenshotPath = captureScreenshot(result.getName());
            test.addScreenCaptureFromPath(screenshotPath); // adding screenshot
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.log(Status.SKIP, "Test Case Skipped is " + result.getName());
        }
        extent.flush();
    }

    @AfterClass
    public void tearDown() throws IOException {
        if (driver != null) {
            driver.quit();
        }
        ExcelUtils.closeExcelFile();
    }
}
