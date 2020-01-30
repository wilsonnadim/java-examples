import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

import static org.junit.Assert.assertEquals;

public class AppiumIosExample {

    private EyesRunner runner = new ClassicRunner();
    private Eyes eyes = new Eyes(runner);
    private WebDriver driver;
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(applitoolsKey);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.LAYOUT2);
        eyes.setImageCut(new FixedCutProvider(63,135,0,0)); //removes URL field...
        BatchInfo batch = new BatchInfo("Github");
        eyes.setBatch(batch);

        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability("platformName", "iOS");
        capability.setCapability("deviceName", "iPhone 7 Plus");
        capability.setCapability("browserName", "safari");
        capability.setCapability("automationName", "XCUITest");
        capability.setCapability("platformVersion", "10.3");
        capability.setCapability("orientation", "PORTRAIT");

        driver = new RemoteWebDriver(new URL("http://localhost:4723/wd/hub"), capability);
    }

    @Test
    public void GithubHomePage() throws Exception {
        driver.get("https://github.com");
        eyes.open(driver, "Github", "Home Page");
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