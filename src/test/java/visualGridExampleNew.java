import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.config.SeleniumConfiguration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgridclient.model.EmulationDevice;
import com.applitools.eyes.visualgridclient.model.EmulationInfo;
import com.applitools.eyes.visualgridclient.model.ScreenOrientation;
import com.applitools.eyes.visualgridclient.model.TestResultSummary;
import com.applitools.eyes.visualgridclient.services.VisualGridRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class visualGridExampleNew {

    private VisualGridRunner renderingManager = new VisualGridRunner(25);
    private Eyes eyes = new Eyes(renderingManager);
    private WebDriver driver;
    private SeleniumConfiguration seleniumConfiguration = new SeleniumConfiguration();

    private static BatchInfo batch;
    private static Long unixTime = System.currentTimeMillis();
    private static String batchId = Long.toString(unixTime);

    @BeforeClass
    public static void batchInitialization(){
        //renderingManager = new VisualGridRunner(40);
        //renderingManager.setLogHandler(new FileLogger(true));

        //renderingManager.setServerUrl(SERVER_URL);
        //batchInfo = new BatchInfo("Top Sites - Visual Grid");
        batch = new BatchInfo("Visual Grid - Hello World");
    }

    @Before
    public void setUp() throws Exception {

        //renderingManager = new VisualGridRunner(25);

        //renderingManager.setServerUrl("YOU_SERVER_URL");

        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setLogHandler(new StdoutLogHandler(true));

        batch.setId(batchId);
        eyes.setBatch(batch);

        seleniumConfiguration.setAppName("Applitools");
        seleniumConfiguration.setTestName("Hello World");

        String environment = "VeryCoolEnvironment";
        seleniumConfiguration.addBrowser(800, 600, SeleniumConfiguration.BrowserType.CHROME, environment);
        seleniumConfiguration.addBrowser(700, 500, SeleniumConfiguration.BrowserType.CHROME, environment);
        seleniumConfiguration.addBrowser(1200, 800, SeleniumConfiguration.BrowserType.CHROME, environment);
        seleniumConfiguration.addBrowser(1600, 1200, SeleniumConfiguration.BrowserType.CHROME, environment);

        EmulationDevice emulationDevice = new EmulationDevice(300, 400, 0.5f, true, ScreenOrientation.LANDSCAPE);
        EmulationInfo emulationInfo = new EmulationInfo(EmulationInfo.DeviceName.Galaxy_Note_II, ScreenOrientation.PORTRAIT);

        seleniumConfiguration.addDeviceEmulation(emulationDevice, environment);
        seleniumConfiguration.addDeviceEmulation(emulationInfo);

        driver = new ChromeDriver();
        driver.get("https://applitools.com/helloworld");
    }

    @Test
    public void HelloWorld() throws Exception {

        eyes.open(driver, seleniumConfiguration);
        eyes.check("first check", Target.window().fully());
        driver.findElement(By.tagName("button")).click();
        eyes.check("forth check", Target.window().fully());

        eyes.close();

        TestResultSummary allTestResults = renderingManager.getAllTestResults();
        System.out.println("Results: " + allTestResults);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}