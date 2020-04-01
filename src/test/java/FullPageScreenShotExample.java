import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class FullPageScreenShotExample {

    private static EyesRunner runner = new ClassicRunner();
    private Eyes eyes = new Eyes(runner);
    private WebDriver driver;
    private static BatchInfo batch;

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo("My Batch");
        batch.setSequenceName("My Batch");
        batch.setNotifyOnCompletion(true);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        TestResultContainer[] results = allTestResults.getAllResults();
        for(TestResultContainer result: results){
            TestResults test = result.getTestResults();
            assertEquals(test.getName() + " has mismatches", 0, test.getMismatches());
        }
    }

    @Before
    public void setUp() throws Exception, URISyntaxException {

        eyes.setServerUrl("https://eyesapi.applitools.com");
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        //eyes.setMatchLevel(MatchLevel.CONTENT);

        eyes.setLogHandler(new StdoutLogHandler(true));
        //Save logs to a file
        //eyes.setLogHandler(new FileLogger("C:\\Path\\To\\Your\\Dir\\Applitools.log", false, true));

        //eyes.setMatchTimeout(5000);
        //eyes.setWaitBeforeScreenshots(3000);

        //eyes.setBranchName("QA");
        //eyes.setParentBranchName("QA");

        eyes.setHideCaret(true);
        eyes.setBatch(batch);

        driver = new ChromeDriver();
    }

    @Test
    public void Github() throws Exception {
        driver.get("https://www.github.com");
        eyes.open(driver, "github.com", "Home Page", new RectangleSize(1200, 800));
        eyes.check("GitHub", Target.window());

        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
        //eyes.closeAsync();
    }

    @Test
    public void Google() throws Exception {
        driver.get("https://www.google.com");
        eyes.open(driver, "google.com", "Home Page", new RectangleSize(1200, 800));
        eyes.check("Google", Target.window());
        eyes.closeAsync();
    }

    @After
    public void afterTest() throws Exception {
        driver.quit();
        eyes.abort();
    }
}