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

import org.openqa.selenium.chrome.ChromeDriver;

@RunWith(Parallelized.class)
public class NetAPorterParallel {

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

    public static String username = "isonic";
    public static String accesskey = "694f60e4-112f-4fd0-a9b2-80ccb97f573f";
    public static String seleniumURI;

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
//        env.add(new String[]{"Windows 10", "14.14393", "MicrosoftEdge", null, null,"1280x1024"});
//        env.add(new String[]{"Windows 10", "49.0", "firefox", null, null,"1280x1024"});
//        env.add(new String[]{"Windows 7", "11.0", "internet explorer", null, null,"1280x1024"});
//        env.add(new String[]{"OS X 10.10", "54.0", "chrome", null, null,"1280x1024"});
        env.add(new String[]{"OS X 10.12", "59.0", "chrome", null, null,"1280x960"});

        return env;
    }

    public NetAPorterParallel(String os, String version, String browser, String deviceName,
                         String deviceOrientation, String resolution) {
        //super();
        this.os = os;
        this.version = version;
        this.browser = browser;
        this.deviceName = deviceName;
        this.deviceOrientation = deviceOrientation;
        this.screenResolution = resolution;
    }

    private Eyes eyes = new Eyes();
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey("9RkMajXrzS1Zu110oTWQps102CHiPRPmeyND99E9iL0G7yAc110");
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.LAYOUT2);
        BatchInfo batch = new BatchInfo("Net-A-Porter-SauceLabs");
        eyes.setBatch(batch);

        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability(CapabilityType.PLATFORM, os);
        capability.setCapability(CapabilityType.BROWSER_NAME, browser);
        capability.setCapability(CapabilityType.VERSION, version);
        capability.setCapability("deviceName", deviceName);
        capability.setCapability("device-orientation", deviceOrientation);
        capability.setCapability("screenResolution", screenResolution);
        String methodName = name.getMethodName();
        capability.setCapability("name", methodName);

        String sauce_url = "https://"+ username +":"+ accesskey + "@ondemand.saucelabs.com:443/wd/hub";

        driver = new RemoteWebDriver(new URL(sauce_url), capability);

        //driver = new ChromeDriver();
        driver.get("http://www.net-a-porter.com/");
    }

    @Test
    public void NetAPorterHomePage() throws Exception {
        eyes.open(driver, "Net-A-Porter-SauceLabs", "Home Page",
                new RectangleSize(1024, 768));
        eyes.checkWindow(screenResolution);
        TestResults results = eyes.close();
        assertEquals(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}