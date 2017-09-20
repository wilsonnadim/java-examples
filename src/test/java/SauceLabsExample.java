//https://wiki.saucelabs.com/display/DOCS/Platform+Configurator#/

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

@RunWith(Parallell.class)
public class SauceLabsExample {

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
    protected String screenResolution;

    public static String username = "your_sauce_user";
    public static String accesskey = "your_sauce_key";
    public static String applitoolsKey = "your_applitools_key";

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
        env.add(new String[]{"Windows 7",   "59.0", "chrome",            null, null, "1600x1200"});
        env.add(new String[]{"Windows 8.1", "59.0", "chrome",            null, null, "1280x1024"});
        env.add(new String[]{"Windows 10",  "49.0", "firefox",           null, null, "1280x1024"});
        env.add(new String[]{"Windows 7",   "11.0", "internet explorer", null, null, "1280x1024"});
        env.add(new String[]{"OS X 10.10",  "54.0", "chrome",            null, null, "1280x1024"});

        return env;
    }

    public SauceLabsExample(String os, String version, String browser, String deviceName,
                   String deviceOrientation, String resolution) {
        this.os = os;
        this.version = version;
        this.browser = browser;
        this.deviceName = deviceName;
        this.deviceOrientation = deviceOrientation;
        this.screenResolution = resolution;
    }

    private Eyes eyes = new Eyes();
    private WebDriver driver;

    private static BatchInfo batch;

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo("Github Sauce Labs");
    }

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(applitoolsKey);
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.LAYOUT2);
        eyes.setBatch(batch);

        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability(CapabilityType.PLATFORM, os);
        capability.setCapability(CapabilityType.BROWSER_NAME, browser);
        capability.setCapability(CapabilityType.VERSION, version);
        capability.setCapability("deviceName", deviceName);
        capability.setCapability("device-orientation", deviceOrientation);
        capability.setCapability("screenResolution", screenResolution);
        capability.setCapability("name", name.getMethodName());

        String sauce_url = "https://"+ username +":"+ accesskey + "@ondemand.saucelabs.com:443/wd/hub";
        driver = new RemoteWebDriver(new URL(sauce_url), capability);
        driver.get("https://github.com");
    }

    @Test
    public void GithubHomePage() throws Exception {
        eyes.open(driver, "Github", "Home Page");
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