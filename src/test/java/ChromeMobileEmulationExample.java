import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class ChromeMobileEmulationExample {

    private EyesRunner runner = new ClassicRunner();
    private Eyes eyes = new Eyes(runner);
    private RemoteWebDriver driver;
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Before
    public void setUp() throws Exception {

        eyes.setApiKey(applitoolsKey);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.SCROLL);
        eyes.setLogHandler(new StdoutLogHandler(true));
        BatchInfo batch = new BatchInfo("Chrome Emulation Test");
        eyes.setBatch(batch);

        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", "iPhone X");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
        driver = new ChromeDriver(chromeOptions);

        driver.get("https://www.github.com/");
    }

    @Test
    public void GitHubTest() throws Exception {
        eyes.open(driver, "GitHub.com", "GitHub - Chrome iOS Emulation");
        eyes.check("GH", Target.window());
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