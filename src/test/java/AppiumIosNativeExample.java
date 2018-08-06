import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.selenium.Eyes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class AppiumIosNativeExample {

    private Eyes eyes = new Eyes();
    private WebDriver driver;
    public static String applitoolsKey = "9RkMajXrzS1Zu110oTWQps102CHiPRPmeyND99E9iL0G7yAc110";

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(applitoolsKey);
        BatchInfo batch = new BatchInfo("iOS Native Local");
        eyes.setBatch(batch);
        eyes.setLogHandler(new StdoutLogHandler(true));

        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability("platformName", "iOS");
        capability.setCapability("deviceName", "iPhone Simulator");
        capability.setCapability("browserName", "");
        capability.setCapability("automationName", "XCUITest");
        capability.setCapability("platformVersion", "10.3");
        capability.setCapability("app", "https://store.applitools.com/download/iOS.TestApp.app.zip");

        driver = new RemoteWebDriver(new URL("http://localhost:4723/wd/hub"), capability);
    }

    @Test
    public void iOSTestApp() throws Exception {
        eyes.open(driver, "Test iOS App", "Main View");
        eyes.checkWindow("TEST");
        eyes.close();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}