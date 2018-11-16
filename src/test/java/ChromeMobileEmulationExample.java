import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
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

    private Eyes eyes = new Eyes();
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

        driver.get("https://www.made.com/");
    }

    //Start your Selenium server prior to running this test...
    @Test
    public void HomePageTest() throws Exception {
        eyes.open(driver, "Made.com", "Chrome iOS Emulation");
        eyes.checkWindow("home page");

        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}