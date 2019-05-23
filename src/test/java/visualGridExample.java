import com.applitools.eyes.RectangleSize;
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
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;

public class visualGridExample {

    private EyesRunner visualGridRunner = new VisualGridRunner(10);
    private Eyes eyes = new Eyes(visualGridRunner);
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {

        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        //eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setServerUrl("https://eyes.applitools.com/");

        Configuration configuration = new Configuration();
        configuration.addBrowser(700,  800, BrowserType.FIREFOX);
        configuration.addBrowser(700,  800, BrowserType.CHROME);
        configuration.addBrowser(1200, 800, BrowserType.FIREFOX);
        configuration.addBrowser(1200, 800, BrowserType.CHROME);
        configuration.addBrowser(1200, 800, BrowserType.IE_10);
        configuration.addBrowser(1200, 800, BrowserType.IE_11);
        configuration.addBrowser(1200, 800, BrowserType.EDGE);
        configuration.addDeviceEmulation(DeviceName.iPhone_6_7_8, ScreenOrientation.PORTRAIT);
        configuration.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
        configuration.addDeviceEmulation(DeviceName.Pixel_2_XL, ScreenOrientation.LANDSCAPE);

        eyes.setConfiguration(configuration);

        driver = new ChromeDriver();
        driver.get("https://applitools.com/helloworld");
    }

    @Test
    public void HelloWorld() throws Exception {

        eyes.open(driver, "Applitools VG", "Hello World", new RectangleSize(1200, 800));

        eyes.check("first check", Target.window());
        driver.findElement(By.tagName("button")).click();
        eyes.check("second check", Target.window());

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