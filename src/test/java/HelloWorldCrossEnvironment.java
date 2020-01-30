import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import static org.junit.Assert.assertEquals;

public class HelloWorldCrossEnvironment {

    private EyesRunner runner = new ClassicRunner();
    private Eyes eyes = new Eyes(runner);
    private WebDriver driver;
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    protected Integer width = 1300;
    protected Integer height = 900;

    private static BatchInfo batch;

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo("Cross-Enviroment Tests");
    }

    @Before
    public void setUp() throws Exception {

        eyes.setApiKey(applitoolsKey);
        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setBaselineEnvName("MacOSX-Chrome");
        eyes.setBatch(batch);
        eyes.setForceFullPageScreenshot(true);
        eyes.setHideScrollbars(true);

        //Layout should be used on cross-env tests.
        //Almost everything will fail when running against a different env otherwise.
        eyes.setMatchLevel(MatchLevel.LAYOUT2);
    }

    //Run this once to create the baseline. And then run each time to maintain it going forward.
    //This is what you are saying your site should look like.
    //Everything should look like this image across all browsers you test against.
    @Test
    public void BaselineTest() throws Exception {
        driver = new ChromeDriver();
        driver.get("https://applitools.com/helloworld");

        //AppName should really stay static. The testName should be changed per different test
        eyes.open(driver, "My Awesome App", "Test Example", new RectangleSize(width, height));
        eyes.check("Chrome Test!", Target.window());
        eyes.closeAsync();
    }

    @Test
    public void TestFirefox() throws Exception {
        driver = new FirefoxDriver();
        driver.get("https://applitools.com/helloworld");

        eyes.open(driver, "My Awesome App", "Test Example", new RectangleSize(width, height));
        eyes.check("Firefox Test!", Target.window());
        eyes.closeAsync();
    }

    @Test
    public void TestSafari() throws Exception {
        driver = new SafariDriver();
        driver.get("https://applitools.com/helloworld");

        eyes.open(driver, "My Awesome App", "Test Example", new RectangleSize(width, height));
        eyes.check("Safari!", Target.window());
        eyes.closeAsync();
    }

    @Test
    public void Test4() throws Exception {
        //Produces a New unsavable test. Does not match hostOS, hostApp or viewport
        eyes.setHostOS("Awesome-OS");
        eyes.setHostApp("Awesome-Browser");
        eyes.setExplicitViewportSize(new RectangleSize(1, 1));

        eyes.open(driver, "Hello World!", "Test 4", new RectangleSize(763, 763));
        eyes.check("Test 4!", Target.window());
        eyes.closeAsync();
    }

    @Test
    public void Test5() throws Exception {
        //Produces a New unsavable test. Does not match hostOS, hostApp or viewport
        eyes.setHostOS("Linux");
        eyes.setHostApp("Chrome");

        eyes.open(driver, "Hello World!", "Test 5", new RectangleSize(763, 763));
        eyes.check("Test 5!", Target.window());
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
        eyes.abort();
    }
}