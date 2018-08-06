//https://wiki.saucelabs.com/display/DOCS/Platform+Configurator#/

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.FixedCutProvider;
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
public class SauceLabsAppiumExample {

    protected String os;
    protected String version;
    protected String deviceName;
    protected String browser;
    protected String deviceOrientation;

    public static String username = "matan";
    public static String accesskey = "ec79e940-078b-41d4-91a6-d7d6008cf1ea";
    public static String applitoolsKey = "9RkMajXrzS1Zu110oTWQps102CHiPRPmeyND99E9iL0G7yAc110";

    @Rule
    public TestName name = new TestName() {
        public String getMethodName() {
            return String.format("%s", super.getMethodName());
        }
    };

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
        env.add(new String[]{"Android", "6.0",  "Android Emulator",           "chrome", "portrait"});
        env.add(new String[]{"iOS",     "7.1",  "Android GoogleAPI Emulator", "chrome", "portrait"});
        env.add(new String[]{"iOS",     "10.3", "iPhone 7 Plus Simulator",    "Safari", "portrait"});
        env.add(new String[]{"iOS",     "10.3",  "iPhone Simulator",           "Safari", "portrait"});
        return env;
    }

    public SauceLabsAppiumExample(String os, String version, String deviceName, String browser, String deviceOrientation) {
        this.os = os;
        this.version = version;
        this.deviceName = deviceName;
        this.browser = browser;
        this.deviceOrientation = deviceOrientation;
    }

    private Eyes eyes = new Eyes();
    private WebDriver driver;

    private static BatchInfo batch;

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo("Github With Sauce Labs");
    }

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(applitoolsKey);
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
        capability.setCapability("name", name.getMethodName());

        if (browser == "Safari") {
            eyes.setImageCut(new FixedCutProvider(63,135,0,0)); //remove URL and footer. values = (header, footer, left, right)
        }

        String sauce_url = "https://"+ username +":"+ accesskey + "@ondemand.saucelabs.com:443/wd/hub";
        driver = new RemoteWebDriver(new URL(sauce_url), capability);
        driver.get("https://www.radiologysolutions.bayer.com/aboutus/congresses/");
    }

    @Test
    public void GithubHomePage() throws Exception {


        eyes.open(driver, "Github2222", "Github Home Page");
        eyes.checkWindow("Home Page");
        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}