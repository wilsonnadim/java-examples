//https://wiki.saucelabs.com/display/DOCS/Platform+Configurator#/

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.FixedCutProvider;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
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

    public static String username = "YourSauceUser";
    public static String accesskey = "YourSauceKey";
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
        env.add(new String[]{"Windows 7",   "59.0", "chrome",            null, null, "1600x1200"});
        env.add(new String[]{"Windows 8.1", "59.0", "chrome",            null, null, "1280x1024"});
        env.add(new String[]{"Windows 10",  "49.0", "firefox",           null, null, "1280x1024"});
        env.add(new String[]{"Windows 7",   "11.0", "internet explorer", null, null, "1280x1024"});
        env.add(new String[]{"OS X 10.10",  "54.0", "chrome",            null, null, "1280x1024"});
        env.add(new String[]{"Android",     "6.0",  "chrome", "Android Emulator", "portrait", null});
        env.add(new String[]{"Android",     "7.1",  "chrome", "Android GoogleAPI Emulator", "landscape", null});
        env.add(new String[]{"iOS",         "10.3", "Safari", "iPhone 7 Plus Simulator", "portrait", null});
        env.add(new String[]{"iOS",         "10.3", "Safari", "iPhone Simulator",           "portrait", null});

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
    private RemoteWebDriver driver;

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
        //eyes.setMatchLevel(MatchLevel.EXACT);
        eyes.setBatch(batch);

        //eyes.setBranchName("myBranch");

        if (browser == "Safari") {
            //remove URL and footer. values = (header, footer, left, right)
            eyes.setImageCut(new FixedCutProvider(63,135,0,0));
        }

        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability(CapabilityType.PLATFORM, os);
        //capability.setCapability(CapabilityType.PLATFORM_NAME, "MAC");
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

        if (deviceName != null ) {
            eyes.open(driver, "Github", "Home Page");
        } else {
            eyes.open(driver, "Github", "Home Page", new RectangleSize(1200, 800));
        }

        //eyes.checkWindow("Home Page Screenshot");
        eyes.check("Fluent - Region by Selector and Element", Target.window());

        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}