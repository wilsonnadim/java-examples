import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class AppiumAndroidExample {

    private Eyes eyes = new Eyes();
    private WebDriver driver;
    private static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(applitoolsKey);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);

        eyes.setMatchLevel(MatchLevel.LAYOUT2);

        BatchInfo batch = new BatchInfo("Github Android");
        eyes.setBatch(batch);
        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability("platformName", "Android");
        capability.setCapability("deviceName", "android");
        capability.setCapability("automationName", "uiautomator2");
        capability.setCapability("browserName", "Chrome");
        driver = new RemoteWebDriver(new URL("http://localhost:4723/wd/hub"), capability);
    }

    @Test
    public void GithubHomePage() throws Exception {
        driver.get("https://github.com/");
        eyes.open(driver, "Github.com", "Home Page");
        eyes.checkWindow("Home Page Screenshot");
        TestResults results = eyes.close(false);
        //assertEqual(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}