import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
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

    private static VisualGridRunner VisualGrid = new VisualGridRunner(25);
    private static Eyes eyes = new Eyes(VisualGrid);
    private static Configuration sconf = new Configuration();
    private static WebDriver driver;


    @BeforeClass
    public static void init(){
        sconf.setBatch(new BatchInfo("Visual Grid - Hello World"));
    }

    @Before
    public void setUp() throws Exception {

        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setLogHandler(new StdoutLogHandler(true));

        sconf.addBrowser(300, 800, Configuration.BrowserType.CHROME);
        sconf.addBrowser(300, 800, Configuration.BrowserType.FIREFOX);
        sconf.addBrowser(700, 800, Configuration.BrowserType.CHROME);
        sconf.addBrowser(700, 800, Configuration.BrowserType.FIREFOX);
        sconf.addBrowser(1300, 800, Configuration.BrowserType.CHROME);
        sconf.addBrowser(1300, 800, Configuration.BrowserType.FIREFOX);
        sconf.addDeviceEmulation(EmulationInfo.DeviceName.iPhone_X, ScreenOrientation.LANDSCAPE);
        sconf.addDeviceEmulation(EmulationInfo.DeviceName.iPad_Pro, ScreenOrientation.PORTRAIT);
        sconf.addDeviceEmulation(EmulationInfo.DeviceName.Galaxy_Note_3, ScreenOrientation.LANDSCAPE);
        sconf.addDeviceEmulation(EmulationInfo.DeviceName.Pixel_2_XL, ScreenOrientation.LANDSCAPE);
        eyes.setConfiguration(sconf);

        driver = new ChromeDriver();
        driver.get("https://applitools.com/helloworld");
    }

    @Test
    public void HelloWorld() throws Exception {

        eyes.open(driver, "app", "test", new RectangleSize(1200, 800));

        eyes.check("Hello World", Target.window().fully(false));

        driver.findElement(By.tagName("button")).click();

        eyes.check("Click!", Target.window().fully());

        TestResultSummary allTestResults = VisualGrid.getAllTestResults();
        System.out.println("Results: " + allTestResults);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}