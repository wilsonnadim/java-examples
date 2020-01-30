import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
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

    private EyesRunner runner = new ClassicRunner();
    private Eyes eyes = new Eyes(runner);
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {

        eyes.setServerUrl("https://eyesapi.applitools.com");
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        //eyes.setMatchLevel(MatchLevel.CONTENT);

        eyes.setLogHandler(new StdoutLogHandler(true));
        //Save logs to a file
        //eyes.setLogHandler(new FileLogger("C:\\Path\\To\\Your\\Dir\\Applitools.log", false, true));

        eyes.setMatchTimeout(5000);
        //eyes.setWaitBeforeScreenshots(3000);

        eyes.setHideCaret(true);

        BatchInfo batch = new BatchInfo("My Batch");
        eyes.setBatch(batch);

        driver = new ChromeDriver();
    }

    @Test
    public void GithubHomePageProd() throws Exception {
        driver.get("https://www.github.com");

        eyes.open(driver, "github.com", "Home Page", new RectangleSize(1300, 600));
        eyes.check("Github", Target.window());
        eyes.closeAsync();
    }

    @Test
    public void GithubHomePageQA() throws Exception {
        driver.get("https://www.github-qa.com");

        eyes.open(driver, "github.com", "Home Page", new RectangleSize(1300, 600));
        eyes.check("Github", Target.window());
        eyes.closeAsync();
    }

    @Test
    public void Google() throws Exception {
        driver.get("https://www.google.com");

        eyes.open(driver, "google.com", "Home Page", new RectangleSize(1200, 800));
        eyes.check("Google", Target.window());
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

