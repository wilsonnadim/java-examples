//https://wiki.saucelabs.com/display/DOCS/Platform+Configurator#/

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.applitools.eyes.selenium.fluent.Target;

import java.net.URL;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

@RunWith(Parallel.class)
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

    public static String username = "sauceUser";
    public static String accesskey = "sauceKey";
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
        env.add(new String[]{"Windows 10",   "77.0", "chrome",            null, null, "1280x1024"});
        env.add(new String[]{"Windows 10", "78.0", "chrome",            null, null, "1280x1024"});
        env.add(new String[]{"Windows 10",  "49.0", "firefox",           null, null, "1280x1024"});
        env.add(new String[]{"Windows 7",   "11.0", "internet explorer", null, null, "1280x1024"});
        env.add(new String[]{"OS X 10.12",  "54.0", "chrome",            null, null, "1280x960"});
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

    private EyesRunner runner = new ClassicRunner();
    private Eyes eyes = new Eyes(runner);
    private RemoteWebDriver driver;

    private static BatchInfo batch;

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo("Google Sauce Labs");
    }

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(applitoolsKey);
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.CONTENT);
        eyes.setBatch(batch);
        //eyes.setBaselineBranchName("blob");
        eyes.setBaselineEnvName("blob");

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
        driver.get("https://www.google.com");
    }

    @Test
    public void GoogleHomePage() throws Exception {

        if (deviceName != null ) {
            eyes.open(driver, "Google", "Home Page");
        } else {
            eyes.open(driver, "Google", "Home Page", new RectangleSize(1000, 800));
        }

        eyes.check("Google Search", Target.window());
        eyes.closeAsync();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abort();

        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        TestResultContainer[] results = allTestResults.getAllResults();
        for (TestResultContainer result : results) {
            TestResults test = result.getTestResults();
            assertEquals(test.getName() + " has mismatches", 0, test.getMismatches());
        }

    }
}