import com.applitools.eyes.*;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class visualGridExample {

    private static EyesRunner runner = new VisualGridRunner(9);
    private Eyes eyes = new Eyes(runner);
    private WebDriver driver;
    private static BatchInfo batch;
    private static String batchName = "Hello World - Visual Grid";

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo(batchName);
        batch.setSequenceName(batchName);
        batch.setNotifyOnCompletion(true);
    }

    @Before
    public void setUp() throws Exception {

        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setServerUrl("https://eyesapi.applitools.com");
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setLogHandler(new StdoutLogHandler(true));
        //eyes.setLogHandler(new FileLogger("/Users/justin/logs/Applitools.log", false, true));
        eyes.setBatch(batch);

        //getConfiguration gets the eyes.set values above and adds to the config...
        Configuration config = eyes.getConfiguration();
        config.setAppName("Hello World");
        config.setTestName("Visual Grid");
        //config.setProxy(new ProxySettings("http://192.168.1.155", 8888, "user", "password"));
        config.addBrowser(600, 800, BrowserType.FIREFOX);
        config.addBrowser(1000, 800, BrowserType.FIREFOX);
        config.addBrowser(1200, 800, BrowserType.FIREFOX);
        config.addBrowser(800, 800, BrowserType.CHROME);
        config.addBrowser(1200, 800, BrowserType.IE_10);
        config.addBrowser(600, 800, BrowserType.IE_11);
        config.addBrowser(1200, 800, BrowserType.EDGE);
        config.addBrowser(1200, 800, BrowserType.SAFARI);
        config.addDeviceEmulation(DeviceName.iPhone6_7_8_Plus, ScreenOrientation.LANDSCAPE);
        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Pixel_2_XL, ScreenOrientation.LANDSCAPE);

        eyes.setConfiguration(config);

        driver = new ChromeDriver();
    }

    @Test
    public void HelloWorld() throws Exception {

        // Navigate the browser to the "hello world!" web-site.
        driver.get("https://applitools.com/helloworld");

        // Start the test and set the browser's viewport size to 800x600.
        eyes.open(driver);

        // Visual checkpoint #1.
        eyes.check("Hello!", Target.window());

        // Click the "Click me!" button.
        driver.findElement(By.tagName("button")).click();

        // Visual checkpoint #2.
        eyes.check("Click!", Target.window());

        // End Visual Test
        eyes.closeAsync();
    }

    @After
    public void afterTest() throws Exception {
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        TestResultContainer[] results = allTestResults.getAllResults();
        for(TestResultContainer result: results){
            TestResults test = result.getTestResults();
            //assertEquals(test.getName() + " has mismatches", 0, test.getMismatches());
        }
        driver.quit();
        eyes.abort();
    }
}