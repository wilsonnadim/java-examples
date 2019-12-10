import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;

public class FullPageScreenShotExample {

    private Eyes eyes = new Eyes();
    private WebDriver driver;
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Before
    public void setUp() throws Exception {

        eyes.setServerUrl("https://sdeyesapi.applitools.com");
        eyes.setApiKey(applitoolsKey);
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        //eyes.setMatchLevel(MatchLevel.CONTENT);

        eyes.setLogHandler(new StdoutLogHandler(true));
        //Save logs to a file
        //eyes.setLogHandler(new FileLogger("C:\\Path\\To\\Your\\Dir\\Applitools.log", false, true));

        //eyes.setMatchTimeout(5000);
        //eyes.setWaitBeforeScreenshots(3000);

        eyes.setHideCaret(true);

        BatchInfo batch = new BatchInfo("My Batch");
        eyes.setBatch(batch);

        driver = new ChromeDriver();
    }

    @Test
    public void GithubHomePage() throws Exception {
        driver.get("https://www.github.com");

        eyes.open(driver, "github.com", "Home Page", new RectangleSize(1300, 600));

        eyes.check("Github", Target.window());

        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @Test
    public void Google() throws Exception {
        driver.get("https://www.google.com");
        eyes.open(driver, "google.com", "Home Page", new RectangleSize(1200, 800));

        eyes.checkWindow("Google");

        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }


    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}

