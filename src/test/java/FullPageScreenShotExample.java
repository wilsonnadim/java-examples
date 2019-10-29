import com.applitools.eyes.*;
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

        eyes.setServerUrl("https://eyesapi.applitools.com");
        eyes.setApiKey(applitoolsKey);
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(false);
        eyes.setStitchMode(StitchMode.SCROLL);

        //eyes.setMatchLevel(MatchLevel.LAYOUT2);

        eyes.setLogHandler(new StdoutLogHandler(true));

        //eyes.setLogHandler(new FileLogger("C:\\Path\\To\\Your\\Dir\\Applitools.log", false, true));

        //eyes.setMatchTimeout(5000);
        //eyes.setWaitBeforeScreenshots(3000);

        eyes.setBranchName("HolyBranch3");
        eyes.setParentBranchName("HolyBranch3");

        eyes.setBaselineBranchName("HolyBranch2");

        eyes.setHideCaret(true);

        BatchInfo batch = new BatchInfo("My Batch - Justin");
        //batch.setId("1234");
        eyes.setBatch(batch);

        driver = new ChromeDriver();
    }

    @Test
    public void GithubHomePage() throws Exception {
        driver.get("https://www.google.com");

        eyes.open(driver, "github.com", "Home Page65", new RectangleSize(1000, 600));

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

