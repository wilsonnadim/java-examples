//https://www.browserstack.com/automate/capabilities#ie-capabilities
//https://www.browserstack.com/list-of-browsers-and-platforms?product=live

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
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

import java.net.URL;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

@RunWith(Parallel.class)
public class BrowserStackExample {

    @Rule
    public TestName name = new TestName() {
        public String getMethodName() {
            return String.format("%s", super.getMethodName());
        }
    };

    protected String os;
    protected String os_version;
    protected String browser_version;
    protected String browser;
    protected String screenResolution;

    public static String username = "your_bs_user";
    public static String accesskey = "your_bs_key";
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
        env.add(new String[]{"Windows", "10",         "55",   "firefox", "2048x1536"});
        env.add(new String[]{"Windows", "8.1",        "59",   "chrome",  "2048x1536"});
        env.add(new String[]{"OS X",    "Sierra",     "55",   "firefox", "1920x1080"});
        env.add(new String[]{"OS X",    "El Capitan", "9.1",  "safari",  "1920x1080"});

        return env;
    }

    public BrowserStackExample(String os, String os_version, String browser_version, String browser, String resolution) {
        this.os = os;
        this.os_version = os_version;
        this.browser_version = browser_version;
        this.browser = browser;
        this.screenResolution = resolution;
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

        //eyes.setStitchOverlap(70);
        //eyes.setSaveFailedTests(false);

        BatchInfo batch = new BatchInfo("Github");
        eyes.setBatch(batch);

        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability("os", os);
        capability.setCapability("os_version", os_version);
        capability.setCapability(CapabilityType.BROWSER_NAME, browser);
        capability.setCapability(CapabilityType.VERSION, browser_version);
        capability.setCapability("resolution", screenResolution);
        capability.setCapability("name", name.getMethodName());

        String browserStackUrl = "http://" + username + ":" + accesskey + "@hub-cloud.browserstack.com/wd/hub";
        driver = new RemoteWebDriver(new URL(browserStackUrl), capability);
        driver.get("https://www.github.com");
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