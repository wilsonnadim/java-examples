//https://wiki.saucelabs.com/display/DOCS/Platform+Configurator#/

import java.net.URL;
import java.util.LinkedList;
import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.FixedCutProvider;
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
public class SauceLabsAppiumExample {

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

    public static String username = "your_sl_user";
    public static String accesskey = "your_sl_key";
    public static String applitoolsKey = "your_applitools_key";

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
        env.add(new String[]{"Android", "6.0",  "Android Emulator", "chrome", "portrait"});
        env.add(new String[]{"iOS",     "10.3", "iPhone Simulator", "Safari", "portrait"});
        env.add(new String[]{"iOS",     "10.3", "iPad Simulator",   "Safari", "portrait"});
        env.add(new String[]{"iOS",     "9.1", "iPhone Simulator",  "Safari", "portrait"});

        return env;
    }

    public SauceLabsAppiumExample(String os, String version, String deviceName, String browser,
                            String deviceOrientation) {
        this.os = os;
        this.version = version;
        this.deviceName = deviceName;
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

        BatchInfo batch = new BatchInfo("Github");
        eyes.setBatch(batch);

        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability(CapabilityType.PLATFORM, os);
        capability.setCapability(CapabilityType.BROWSER_NAME, browser);
        capability.setCapability(CapabilityType.VERSION, version);
        capability.setCapability("deviceName", deviceName);
        capability.setCapability("device-orientation", deviceOrientation);
        capability.setCapability("name", name.getMethodName());

        if (browser == "Safari") {
            //lower scale gets wider. higher scale gets thinner
            eyes.setScaleRatio(0.66); //scale the device image to appropriate size.
            eyes.setImageCut(new FixedCutProvider(63,135,0,0)); //remove URL and footer.
        }

        String sauce_url = "https://"+ username +":"+ accesskey + "@ondemand.saucelabs.com:443/wd/hub";
        driver = new RemoteWebDriver(new URL(sauce_url), capability);
        driver.get("https://github.com/");
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