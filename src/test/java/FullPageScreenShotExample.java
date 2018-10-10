import com.applitools.eyes.*;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
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

        //eyes.setServerUrl(URI.create("https://your-onprem-server"));
        eyes.setApiKey(applitoolsKey);
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.LAYOUT2);
        eyes.setLogHandler(new StdoutLogHandler(true));


        BatchInfo batch = new BatchInfo("My Fullpage Test");
        eyes.setBatch(batch);

        driver = new ChromeDriver();
        driver.get("https://www.github.com/");
    }

    @Test
    public void GithubHomePage() throws Exception {
        eyes.open(driver, "Github", "Home Page", new RectangleSize(414, 568));
        eyes.checkWindow("github");

        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}