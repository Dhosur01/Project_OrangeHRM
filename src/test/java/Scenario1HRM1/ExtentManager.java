package Scenario1HRM1;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    private static ExtentReports extent;
    private static ExtentSparkReporter spark;

    public static ExtentReports createInstance() {
        spark = new ExtentSparkReporter("extentReport.html");
        spark.config().setDocumentTitle("Automation Report");
        spark.config().setReportName("OrangeHRM Test Report");
        spark.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(spark);

        return extent;
    }

    public static ExtentReports getInstance() {
        if (extent == null)
            extent = createInstance();  // Ensure this line returns the correct type
        return extent;
    }
}
