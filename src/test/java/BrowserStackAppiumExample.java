//https://www.browserstack.com/automate/capabilities#ie-capabilities
//https://www.browserstack.com/list-of-browsers-and-platforms?product=live

import com.applitools.eyes.*;
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

import java.net.URL;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

@RunWith(Parallell.class)
public class BrowserStackAppiumExample {

    @Rule
    public TestName name = new TestName() {
        public String getMethodName() {
            return String.format("%s", super.getMethodName());
        }
    };

    protected String browser;
    protected String os;
    protected String version;
    protected String device;
    protected String deviceOrientation;

    public static String username = "your_browserstack_user";
    public static String accesskey = "your_browserstack_key";
    public static String applitoolsKey = "your_applitools_key";

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
        env.add(new String[]{"Android", "6.0",  "Samsung Galaxy S7", "chrome", "portrait"});
        env.add(new String[]{"iPhone",     "9.1", "iPhone 6S Plus", "Safari", "portrait"});
        env.add(new String[]{"iPhone",     "9.1", "iPhone 6S",   "Safari", "portrait"});

        return env;
    }

    public BrowserStackAppiumExample(String os, String version, String device, String browser,
                                  String deviceOrientation) {
        this.os = os;
        this.version = version;
        this.device = device;
        this.browser = browser;
        this.deviceOrientation = deviceOrientation;
    }

    private Eyes eyes = new Eyes();
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(applitoolsKey);
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.LAYOUT2);

        //eyes.setSaveFailedTests(false);

        BatchInfo batch = new BatchInfo("Github");
        eyes.setBatch(batch);

        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability(CapabilityType.PLATFORM_NAME, os);
        capability.setCapability(CapabilityType.BROWSER_NAME, browser);
        capability.setCapability("os_version", version);
        capability.setCapability("device", device);
        capability.setCapability("device-orientation", deviceOrientation);
        capability.setCapability("name", name.getMethodName());
        capability.setCapability("realMobile", true); //Set for real devices on BS.
        //capability.setCapability("browserstack.appium_version", "1.6.3");

        if (browser == "Safari") {
            //lower scale gets wider. higher scale gets thinner
            eyes.setScaleRatio(0.66); //scale the device image to appropriate size.
            eyes.setImageCut(new FixedCutProvider(63,135,0,0)); //remove URL and footer.
        }

        String browserStackUrl = "http://" + username + ":" + accesskey + "@hub-cloud.browserstack.com/wd/hub";
        driver = new RemoteWebDriver(new URL(browserStackUrl), capability);
        //driver.get("https://www.github.com/");
        driver.get("https://www.spectrum.com/");
    }

    @Test
    public void GithubHomePage() throws Exception {
        eyes.open(driver, "Github", "Home Page");
        eyes.checkWindow("Home Page Screenshot");
        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}