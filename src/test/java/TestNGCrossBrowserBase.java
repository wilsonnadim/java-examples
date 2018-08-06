import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.UnexpectedException;

/**
 * Simple TestNG test which demonstrates being instantiated via a DataProvider in order to supply multiple browser combinations.
 *
 * @author Neil Manvar
 */
public class TestNGCrossBrowserBase {

    public String username = System.getenv("CBT_USERNAME");
    public String authkey = System.getenv("CBT_AUTH_KEY");

    public ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
    public WebDriver getWebDriver() {
        return webDriver.get();
    }

    private ThreadLocal<String> sessionId = new ThreadLocal<String>();
    public String getSessionId() {
        return sessionId.get();
    }

    private ThreadLocal<Eyes> myEyes = new ThreadLocal<Eyes>();
    public Eyes getEyes() { return myEyes.get(); }

    /**
     * DataProvider that explicitly sets the browser combinations to be used.
     *
     * @param testMethod
     * @return Two dimensional array of objects with browser, version, and platform information
     */

    //Comment out all but one for your initial baseline sample. After your baseline is created then uncomment out each browser/os combo to do cross browser testing.
    @DataProvider(name = "hardCodedBrowsers", parallel = true)
    public static Object[][] cbtBrowserDataProvider(Method testMethod) {
        return new Object[][]{
                new Object[]{"Chrome",            "60",    "Windows 10",    "2560x1920"},
                new Object[]{"Internet Explorer", "9",     "Windows 7",     "1920x1080"},
                new Object[]{"Safari",            "9",     "Mac OSX 10.11", "2560x1600"},
                new Object[]{"Firefox",           "54",    "Mac OSX 10.12", "2560x1600"},
        };
    }

    /**
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use the capabilities defined by the browser,
     * version and os parameters, and which is configured to run against ondemand.saucelabs.com, using
     * the username and access key populated by the {@link //authentication} instance.
     *
     * @param browser Represents the browser to be used as part of the test run.
     * @param version Represents the version of the browser to be used as part of the test run.
     * @param os Represents the operating system to be used as part of the test run.
     * @param resolution Represents the operating system virtual resolution. Should always set to max available.
     * @param methodName Represents the name of the test case that will be used to identify the test on Sauce.
     * @return
     * @throws MalformedURLException if an error occurs parsing the url
     */
    protected void createDriver(String browser, String version, String os, String resolution, String methodName)
            throws MalformedURLException, UnexpectedException {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // set desired capabilities to launch appropriate browser on Sauce
        capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        capabilities.setCapability(CapabilityType.VERSION, version);
        capabilities.setCapability(CapabilityType.PLATFORM, os);
        capabilities.setCapability("screenResolution", resolution);
        capabilities.setCapability("name", methodName);
        capabilities.setCapability("record_video", "true");

        // Launch remote browser and set it as the current thread
        webDriver.set(new RemoteWebDriver(
                new URL("http://" + username +":" + authkey +"@hub.crossbrowsertesting.com:80/wd/hub"),
                capabilities));

        //webDriver.set(new ChromeDriver());

        myEyes.set(new Eyes());
        getEyes().setApiKey(System.getenv("APPLITOOLS_KEY"));
        getEyes().setHideScrollbars(true);
        getEyes().setForceFullPageScreenshot(true);
        getEyes().setStitchMode(StitchMode.CSS);
        getEyes().setMatchLevel(MatchLevel.LAYOUT2);
        getEyes().setBatch(batch);
        getEyes().setBaselineEnvName("Win7Chrome");

        // set current sessionId
        String id = ((RemoteWebDriver) getWebDriver()).getSessionId().toString();
        sessionId.set(id);
    }

    private static BatchInfo batch;

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo("TestNG CBT Applitools");
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws Exception {
        webDriver.get().quit();
    }

    protected void annotate(String text) {

    }
}
