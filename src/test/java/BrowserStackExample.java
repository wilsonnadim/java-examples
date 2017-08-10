import java.net.URL;
import java.util.LinkedList;
import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import static org.junit.Assert.assertEquals;

@RunWith(Parallell.class)
public class BrowserStackExample {

    @Rule
    public TestName name = new TestName() {
        public String getMethodName() {
            return String.format("%s", super.getMethodName());
        }
    };

    protected String browser;
    protected String os;
    protected String version;
    protected String deviceName;
    protected String deviceOrientation;

    public static String username = "your_browserstack_user";
    public static String accesskey = "your_browserstack_key";

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
        env.add(new String[]{"WINDOWS", "15",   "Edge",    null, null});
        env.add(new String[]{"WINDOWS", "54",   "firefox", null, null});
        env.add(new String[]{"MAC",     "10.1", "safari",  null, null});
        env.add(new String[]{"MAC",     "59",   "chrome",  null, null});

        return env;
    }

    public BrowserStackExample(String os, String version, String browser, String deviceName,
                            String deviceOrientation) {
        this.os = os;
        this.version = version;
        this.browser = browser;
        this.deviceName = deviceName;
        this.deviceOrientation = deviceOrientation;
    }

    private Eyes eyes = new Eyes();
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey("your_applitools_key");
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.LAYOUT2);

        //eyes.setSaveFailedTests(false);

        BatchInfo batch = new BatchInfo("Google");
        eyes.setBatch(batch);

        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability(CapabilityType.PLATFORM, os);
        capability.setCapability(CapabilityType.BROWSER_NAME, browser);
        capability.setCapability(CapabilityType.VERSION, version);
        capability.setCapability("deviceName", deviceName);
        capability.setCapability("device-orientation", deviceOrientation);
        capability.setCapability("name", name.getMethodName());

        String browserStackUrl = "http://" + username + ":" + accesskey + "@hub-cloud.browserstack.com/wd/hub";
        driver = new RemoteWebDriver(new URL(browserStackUrl), capability);
        driver.get("https://www.google.com");
    }

    @Test
    public void GoogleHomePage() throws Exception {
        eyes.open(driver, "Google", "Home Page", new RectangleSize(800, 600));
        eyes.checkWindow("Home Page Screenshot");
        TestResults results = eyes.close();
        assertEquals(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}