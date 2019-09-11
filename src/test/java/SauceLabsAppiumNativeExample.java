//https://wiki.saucelabs.com/display/DOCS/Platform+Configurator#/

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.selenium.Eyes;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

@RunWith(Parallel.class)
public class SauceLabsAppiumNativeExample {

    protected String os;
    protected String version;
    protected String deviceName;
    protected String browser;
    protected String deviceOrientation;

    public static String username = "your_sauce_user";
    public static String accesskey = "your_sauce_key";
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Rule
    public TestName name = new TestName() {
        public String getMethodName() {
            return String.format("%s", super.getMethodName());
        }
    };

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
        env.add(new String[]{"Android", "6.0", "Android Emulator",                     "", "portrait"});
        env.add(new String[]{"Android", "7.0", "Android GoogleAPI Emulator",           "", "portrait"});
        env.add(new String[]{"Android", "4.4", "Google Nexus 7 HD GoogleAPI Emulator", "", "portrait"});
        env.add(new String[]{"Android", "4.4", "Samsung Galaxy S3 GoogleAPI Emulator", "", "portrait"});
        return env;
    }

    public SauceLabsAppiumNativeExample(String os, String version, String deviceName, String browser, String deviceOrientation) {
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
        batch = new BatchInfo("Native App With Sauce Labs");
    }

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(applitoolsKey);
        eyes.setBatch(batch);
        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability(CapabilityType.PLATFORM, os);
        capability.setCapability(CapabilityType.BROWSER_NAME, browser);
        capability.setCapability(CapabilityType.VERSION, version);
        capability.setCapability("deviceName", deviceName);
        capability.setCapability("device-orientation", deviceOrientation);
        capability.setCapability("name", name.getMethodName());
        capability.setCapability("app", "https://www.dropbox.com/s/yksa2wfszjjslba/app-debug.apk?dl=1");
        String sauce_url = "https://"+ username +":"+ accesskey + "@ondemand.saucelabs.com:443/wd/hub";
        driver = new RemoteWebDriver(new URL(sauce_url), capability);
    }

    @Test
    public void NativeApp() throws Exception {
        driver.findElement(By.id("ReferenceApp")).click();
        List<WebElement> we  = driver.findElements(By.id("Row Category Name"));
        we.get(2).click();
        eyes.open(driver, "Native App Test", "Native Components");
        eyes.checkWindow("Test");
        eyes.close();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}