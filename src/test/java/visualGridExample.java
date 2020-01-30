import com.applitools.eyes.*;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
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
        eyes.setServerUrl("https://eyesapi.applitools.com");
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setLogHandler(new StdoutLogHandler(true));
        //eyes.setLogHandler(new FileLogger("/Users/justin/logs/Applitools.log", false, true));
        BatchInfo batchInfo = new BatchInfo("TEST");
        batchInfo.setNotifyOnCompletion(true);
        eyes.setBatch(batchInfo);

        //getConfiguration gets the eyes.set values above and adds to the config...
        Configuration config = eyes.getConfiguration();
        config.setAppName("Applitools VG");
        config.setTestName("Hello World");
        //config.setProxy(new ProxySettings("http://192.168.1.155", 8888, "user", "password"));
        config.addBrowser(700,  800, BrowserType.CHROME);
        config.addBrowser(700,  800, BrowserType.FIREFOX);
        config.addBrowser(1920, 937, BrowserType.FIREFOX);
        config.addBrowser(1200, 800, BrowserType.CHROME);
        config.addBrowser(1200, 800, BrowserType.IE_10);
        config.addBrowser(1200, 800, BrowserType.IE_11);
        config.addBrowser(1200, 800, BrowserType.EDGE);
        config.addDeviceEmulation(DeviceName.iPhone_6_7_8, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Pixel_2_XL, ScreenOrientation.LANDSCAPE);

        eyes.setConfiguration(config);

        driver = new ChromeDriver();
        driver.get("https://applitools.com/helloworld");
    }

    @Test
    public void HelloWorld() throws Exception {
        // Navigate the browser to the "hello world!" web-site.
        driver.get("https://applitools.com/helloworld");

        // Start the test and set the browser's viewport size to 800x600.
        eyes.open(driver, "MY-HELLO-WORLD", "My first Selenium Java test!", new RectangleSize(1200, 800));

        // Visual checkpoint #1.
        eyes.check("Hello!", Target.window());

        // Click the "Click me!" button.
        driver.findElement(By.tagName("button")).click();

        // Visual checkpoint #2.
        eyes.check("Click!", Target.window());

        eyes.closeAsync();
    }

    @After
    public void tearDown() throws Exception {
        TestResultsSummary allTestResults = visualGridRunner.getAllTestResults();
        TestResultContainer[] results = allTestResults.getAllResults();
        for(TestResultContainer result: results){
            TestResults test = result.getTestResults();
            assertEquals(test.getName() + " has mismatches", 0, test.getMismatches());
        }

        driver.quit();
        eyes.abortAsync();
    }
}

