package utils;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class ExtentITestNGListeners implements ITestListener {
    ExtentReports extent = ExtentManager.getInstance();
    ThreadLocal<ExtentTest> parentTest = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        parentTest.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        parentTest.get().log(Status.PASS, result.getName() + " got Successfully Pass!");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        parentTest.get().fail("Test Failed " + result.getThrowable().getMessage());
        System.out.println("Test Failure: " + result.getMethod().getMethodName());
        parentTest.get().log(Status.FAIL, result.getName() + " Test Fail");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        parentTest.get().skip("Test Skipped " + result.getThrowable().getMessage());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
