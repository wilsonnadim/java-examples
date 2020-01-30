//https://wiki.saucelabs.com/display/DOCS/Platform+Configurator#/

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

@RunWith(Parallel.class)
public class SauceLabsExampleDesktop {

    @Rule
    public TestName name = new TestName() {
        public String getMethodName() {
            return String.format("%s", super.getMethodName());
        }
    };

    protected String browser;
    protected String os;
    protected String version;
    protected String screenResolution;

    public static String username = "SauceUser";
    public static String accesskey = "SauceKey";

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
        env.add(new String[]{"Windows 7",   "69.0",     "chrome",            "2560x1600"});
        env.add(new String[]{"Windows 7",   "62.0",     "firefox",           "2560x1600"});

        env.add(new String[]{"Windows 8",   "69.0",     "chrome",            "2560x1600"});
        env.add(new String[]{"Windows 8",   "62.0",     "firefox",           "2560x1600"});

        env.add(new String[]{"Windows 8.1", "69.0",     "chrome",            "2560x1600"});
        env.add(new String[]{"Windows 8.1", "62.0",     "firefox",           "2560x1600"});

        env.add(new String[]{"Windows 10",  "16.16299", "chrome",            "2560x1600"});
        env.add(new String[]{"Windows 10",  "16.16299", "MicrosoftEdge",     "1400x1050"});
        env.add(new String[]{"Windows 10",  "62.0",     "firefox",           "2560x1600"});

        env.add(new String[]{"OS X 10.13",  "62.0",     "firefox",           "1920x1440"});
        env.add(new String[]{"OS X 10.13",  "11.1",     "safari",            "1920x1440"});
        env.add(new String[]{"OS X 10.13",  "69.0",     "chrome",            "1920x1440"});
        return env;
    }

    public SauceLabsExampleDesktop(String os, String version, String browser, String resolution) {
        this.os = os;
        this.version = version;
        this.browser = browser;
        this.screenResolution = resolution;
    }

    private EyesRunner runner = new ClassicRunner();
    private Eyes eyes = new Eyes(runner);
    private RemoteWebDriver driver;

    private static BatchInfo batch;

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo("Github Supported Browsers Test");
    }

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.LAYOUT2); //Must use Layout for cross browser tests...
        eyes.setBatch(batch);
        eyes.setBranchName("myBranch");

        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability(CapabilityType.PLATFORM, os);
        capability.setCapability(CapabilityType.BROWSER_NAME, browser);
        capability.setCapability(CapabilityType.VERSION, version);
        capability.setCapability("screenResolution", screenResolution);
        capability.setCapability("name", name.getMethodName());

        String sauce_url = "https://"+ username +":"+ accesskey + "@ondemand.saucelabs.com:443/wd/hub";
        driver = new RemoteWebDriver(new URL(sauce_url), capability);
        driver.get("https://github.com");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 0);");
    }

    @Test
    public void GithubHomePage() throws Exception {
        eyes.open(driver, "Github Desktop", "Home Page", new RectangleSize(1200, 800));

        WebElement element = driver.findElement(By.className("position-relative")); //github logo top left
        eyes.check("Fluent - Region by Selector and Element", Target.window()
                .ignore(By.cssSelector("div.mx-auto.col-sm-8.col-md-5.hide-sm")) //ignores the login form
                .ignore(element)); //ignores github logo top left
        eyes.closeAsync();
    }

    @After
    public void tearDown() throws Exception {
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        TestResultContainer[] results = allTestResults.getAllResults();
        for (TestResultContainer result : results) {
            TestResults test = result.getTestResults();
            assertEquals(test.getName() + " has mismatches", 0, test.getMismatches());
        }

        driver.quit();
        eyes.abort();
    }
}