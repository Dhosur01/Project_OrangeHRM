package Scenario1HRM1;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class BaseTest {
    protected WebDriver driver; // Ensure driver is accessible in this class

    public String captureScreenshot(String screenshotName) {
        if (driver == null) {
            System.out.println("Driver is not initialized. Cannot capture screenshot.");
            return null;
        }
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String destination = "C:\\Lakshmana_seleniumDemo\\Scenario1HRM1\\Screenshots\\" + screenshotName + ".png";
        try {
            FileUtils.copyFile(srcFile, new File(destination));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destination;
    }

    public boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
