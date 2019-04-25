import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.model.TestResultContainer;
import com.applitools.eyes.visualgrid.model.TestResultSummary;
import com.applitools.eyes.visualgrid.services.EyesRunner;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(Parallell.class)
public class visualGridParallel {

    private String gskUrl;

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
        env.add(new String[]{"excedrin"});
        env.add(new String[]{"nicodermcq"});
        env.add(new String[]{"nicorette"});
        env.add(new String[]{"quit"});
        env.add(new String[]{"selzentry"});
        return env;
    }

    public visualGridParallel(String gskUrl) {
        this.gskUrl = gskUrl;
    }

    private void lazyLoadPage() throws InterruptedException {
        JavascriptExecutor js =(JavascriptExecutor)driver;
        Long height = (Long) js.executeScript("return document.body.scrollHeight;");
        for(int i = 0 ; i < height/100 ; i++)
            js.executeScript("window.scrollBy(0,100)");

        TimeUnit.SECONDS.sleep(1);
        js.executeScript("window.scrollTo(0, 0);");
        TimeUnit.SECONDS.sleep(1);
    }

    private EyesRunner visualGridRunner = new VisualGridRunner(10);
    private Eyes eyes = new Eyes(visualGridRunner);
    private WebDriver driver;

    private static BatchInfo batch;

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo("GSK Sites - Visual Grid Test");
    }

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setServerUrl("https://eyes.applitools.com/");

        Configuration configuration = new Configuration();
        configuration.setBatch(batch);
        configuration.addBrowser(700,  800, BrowserType.FIREFOX);
        configuration.addBrowser(700,  800, BrowserType.CHROME);
        configuration.addBrowser(1200, 800, BrowserType.FIREFOX);
        configuration.addBrowser(1200, 800, BrowserType.CHROME);
        configuration.addDeviceEmulation(DeviceName.iPhone_6_7_8, ScreenOrientation.PORTRAIT);
        configuration.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
        configuration.addDeviceEmulation(DeviceName.Nexus_10, ScreenOrientation.LANDSCAPE);

        eyes.setConfiguration(configuration);

        driver = new ChromeDriver();
        driver.get("https://www." + gskUrl + ".com");
        lazyLoadPage();
    }

    @Test
    public void GSKSites() throws Exception {
        eyes.open(driver, "GSK", gskUrl, new RectangleSize(1200, 800));
        eyes.check(gskUrl, Target.window().fully());

        TestResultSummary allTestResults = visualGridRunner.getAllTestResults();
        System.out.println("Test Results: " + allTestResults);

        TestResultContainer[] results = allTestResults.getAllResults();
        for(TestResultContainer result: results){
            TestResults test = result.getTestResults();
            assertEquals(test.getName() + " has mismatches", 0, test.getMismatches());
        }
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}