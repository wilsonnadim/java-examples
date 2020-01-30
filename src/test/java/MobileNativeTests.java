import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.EyesRunner;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

@org.testng.annotations.Test()
public class MobileNativeTests {

    private String sauceUser = System.getenv("SAUCE_USERNAME");
    private String sauceKey = System.getenv("SAUCE_ACCESS_KEY");
    private String appiumServerUrl = "https://" + sauceUser + ":" + sauceKey + "@ondemand.saucelabs.com:443/wd/hub";
    private static BatchInfo batchInfo = new BatchInfo("Mobile Native Tests");

    private void setupLogging(Eyes eyes, DesiredCapabilities capabilities, String methodName) {
        capabilities.setCapability("Sauce", sauceUser);
        capabilities.setCapability("SauceKey",sauceKey);
        capabilities.setCapability("name", methodName);

        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setBatch(batchInfo);
    }

    @Test
    public void AndroidNativeAppTest1() throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("platformVersion", "6.0");
        capabilities.setCapability("app", "http://saucelabs.com/example_files/ContactManager.apk");
        capabilities.setCapability("clearSystemFiles", true);
        capabilities.setCapability("noReset", true);
        //capabilities.setCapability("appiumVersion", "1.9.1");

        EyesRunner runner = new ClassicRunner();
        Eyes eyes = new Eyes(runner);

        setupLogging(eyes, capabilities, "AndroidNativeAppTest1");

        WebDriver driver = new AndroidDriver(new URL(appiumServerUrl), capabilities);

        try {
            eyes.open(driver, "Mobile Native Tests", "Android Native App 1");
            eyes.check("Contact List", Target.window());
            eyes.closeAsync();
        } finally {
            TestResultsSummary allTestResults = runner.getAllTestResults(false);
            driver.quit();
            eyes.abort();
        }
    }

    @Test
    public void AndroidNativeAppTest2() throws MalformedURLException, InterruptedException {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "7.1");
        capabilities.setCapability("deviceName", "Samsung Galaxy S8 WQHD GoogleAPI Emulator");
        capabilities.setCapability("automationName", "uiautomator2");

        capabilities.setCapability("app", "https://applitools.bintray.com/Examples/app-debug.apk");

        capabilities.setCapability("appPackage", "com.applitoolstest");
        capabilities.setCapability("appActivity", "com.applitoolstest.ScrollActivity");
        capabilities.setCapability("newCommandTimeout", 600);
        capabilities.setCapability("appiumVersion", "1.6.0");

        EyesRunner runner = new ClassicRunner();
        Eyes eyes = new Eyes(runner);

        setupLogging(eyes, capabilities,"AndroidNativeAppTest2");

        AndroidDriver<AndroidElement> driver = new AndroidDriver<>(new URL(appiumServerUrl), capabilities);

        try {
            eyes.open(driver, "Mobile Native Tests", "Android Native App 2");
            Thread.sleep(10000);

            MobileElement scrollableElement = driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().scrollable(true)"));

            eyes.check("Main window with ignore", Target.region(scrollableElement).ignore(scrollableElement));
            eyes.closeAsync();
        } finally {
            TestResultsSummary allTestResults = runner.getAllTestResults(false);
            eyes.abortIfNotClosed();
            driver.quit();
        }
    }

    @Test
    public void iOSNativeAppTest() throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("deviceName", "iPhone 7 Simulator");
        capabilities.setCapability("platformVersion", "10.0");
        capabilities.setCapability("app", "https://store.applitools.com/download/iOS.TestApp.app.zip");
        capabilities.setCapability("clearSystemFiles", true);
        capabilities.setCapability("noReset", true);

        EyesRunner runner = new ClassicRunner();
        Eyes eyes = new Eyes(runner);

        setupLogging(eyes, capabilities,"iOSNativeAppTest");

        WebDriver driver = new IOSDriver(new URL(appiumServerUrl), capabilities);

        try {
            eyes.open(driver, "Mobile Native Tests", "iOS Native App");
            eyes.check("Check Window", Target.window());
            eyes.closeAsync();
        } finally {
            TestResultsSummary allTestResults = runner.getAllTestResults(false);
            driver.quit();
            eyes.abort();
        }
    }
}