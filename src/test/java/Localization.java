import com.applitools.eyes.*;
import com.applitools.eyes.selenium.Eyes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.Assert.assertEquals;

public class Localization {

    private Eyes eyes = new Eyes();
    private WebDriver driver;
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");
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
        driver.quit();
        eyes.abortIfNotClosed();
    }

    public void initializeEyes() {
        eyes = new Eyes();
        //eyes.setServerUrl(URI.create("https://your-onprem-server"));
        eyes.setApiKey(applitoolsKey);
        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setBatch(batchInfo);
    }

    //Create a baseline in each language...
    @Test
    public void GoogleUS() throws Exception {
        driver.get("https://www.google.com");
        eyes.open(driver, "Google", "Google US", new RectangleSize(1035, 635));
        eyes.checkWindow("github");

        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @Test
    public void GoogleIT() throws Exception {
        driver.get("https://www.google.it");
        eyes.open(driver, "Google", "Google Italy", new RectangleSize(1035, 635));
        eyes.checkWindow("github");

        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @Test
    public void GoogleSpain() throws Exception {
        driver.get("https://www.google.es");
        eyes.open(driver, "Google", "Google Spain", new RectangleSize(1035, 635));
        eyes.checkWindow("github");

        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    //Check layouts of one language to another...
    @Test
    public void FacebookLayoutTestFrance() throws Exception {
        eyes.setMatchLevel(MatchLevel.LAYOUT2); //Must use Layout for this...
        driver.get("https://fr-fr.facebook.com/");
        eyes.open(driver, "Facebook", "Facebook", new RectangleSize(1035, 635));
        eyes.checkWindow("FR"); //This creates the baseline

        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @Test
    public void FacebookLayoutTestGerman() throws Exception {
        eyes.setMatchLevel(MatchLevel.LAYOUT2); //Must use Layout for this...
        driver.get("https://de-de.facebook.com/");
        eyes.open(driver, "Facebook", "Facebook", new RectangleSize(1035, 635));
        eyes.checkWindow("DE"); //This compares against above baseline. appName, testName, viewPort size must be the same...

        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }
}