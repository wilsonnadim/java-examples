import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.Assert.assertEquals;

public class Localization {

    private EyesRunner runner = new ClassicRunner();
    private Eyes eyes = new Eyes(runner);
    private WebDriver driver;
    private static BatchInfo batchInfo;

    @org.junit.BeforeClass
    public static void createBatch(){
        batchInfo = new BatchInfo("Localization Example");
    }

    @Before
    public void setUp() throws Exception {
        initializeEyes();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver();
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

    private void initializeEyes() {
        eyes.setServerUrl("https://eyesapi.applitools.com");
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setBatch(batchInfo);
    }

    //Create a baseline in each language...
    @Test
    public void GoogleUS() throws Exception {
        driver.get("https://www.google.com");
        eyes.open(driver, "Google", "Google US", new RectangleSize(1035, 635));
        eyes.check("America!", Target.window());
        eyes.closeAsync();
    }

    @Test
    public void GoogleIT() throws Exception {
        driver.get("https://www.google.it");
        eyes.open(driver, "Google", "Google Italy", new RectangleSize(1035, 635));
        eyes.check("Italy!", Target.window());
        eyes.closeAsync();
    }

    @Test
    public void GoogleSpain() throws Exception {
        driver.get("https://www.google.es");
        eyes.open(driver, "Google", "Google Spain", new RectangleSize(1035, 635));
        eyes.check("Spain!", Target.window());
        eyes.closeAsync();
    }

    //Check layouts of one language to another...
    @Test
    public void FacebookLayoutTestFrance() throws Exception {
        eyes.setMatchLevel(MatchLevel.LAYOUT2); //Must use Layout for this...
        driver.get("https://fr-fr.facebook.com/");
        eyes.open(driver, "Facebook", "Facebook", new RectangleSize(1035, 635));
        eyes.check("France!", Target.window());
        eyes.closeAsync();
    }

    @Test
    public void FacebookLayoutTestGerman() throws Exception {
        eyes.setMatchLevel(MatchLevel.LAYOUT2); //Must use Layout for this...
        driver.get("https://de-de.facebook.com/");
        eyes.open(driver, "Facebook", "Facebook", new RectangleSize(1035, 635));
        eyes.check("Germany!", Target.window());
        eyes.closeAsync();
    }
}