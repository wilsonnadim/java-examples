import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import io.appium.java_client.ios.IOSDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

public class AppiumIosNativeExample {

    private EyesRunner runner = new ClassicRunner();
    private Eyes eyes = new Eyes(runner);
    private WebDriver driver;
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(applitoolsKey);
        BatchInfo batch = new BatchInfo("iOS Native Local");
        eyes.setBatch(batch);
        eyes.setLogHandler(new StdoutLogHandler(true));

        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability("platformName", "iOS");
        capability.setCapability("deviceName", "iPhone Simulator");
        //capability.setCapability("browserName", "");
        capability.setCapability("automationName", "XCUITest");
        capability.setCapability("platformVersion", "10.3");
        capability.setCapability("app", "https://store.applitools.com/download/iOS.TestApp.app.zip");

        driver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capability);
    }

    @Test
    public void iOSTestApp() throws Exception {
        TimeUnit.SECONDS.sleep(5);
        eyes.open(driver, "Test iOS App", "Main View");
        eyes.check("Home Page Screenshot", Target.window());
        eyes.closeAsync();
    }

    @After
    public void tearDown() throws Exception {
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        TestResultContainer[] results = allTestResults.getAllResults();
        for(TestResultContainer result: results){
            TestResults test = result.getTestResults();

            assertEquals(test.getName() + " has mismatches", 0, test.getMismatches());
        }

        driver.quit();
        eyes.abortIfNotClosed();
    }
}
