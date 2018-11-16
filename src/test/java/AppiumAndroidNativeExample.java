import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.selenium.Eyes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.List;

public class AppiumAndroidNativeExample {

    private Eyes eyes = new Eyes();
    private WebDriver driver;
    private static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(applitoolsKey);
        BatchInfo batch = new BatchInfo("Amazon Test App Local");
        eyes.setBatch(batch);
        eyes.setLogHandler(new StdoutLogHandler(true));
        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability("platformName", "Android");
        capability.setCapability("platform", "android");
        capability.setCapability("deviceName", "android");
        capability.setCapability("app", "/Users/justin/repos/arc/app-debug.apk");
        ///capability.setCapability("appPackage", "com.amazonaws.devicefarm.android.referenceapp");
        capability.setCapability("appActivity", ".Activities.MainActivity");
        driver = new RemoteWebDriver(new URL("http://localhost:4723/wd/hub"), capability);
    }

    @Test
    public void AmazonTestApp() throws Exception {
        driver.findElement(By.className("ReferenceApp")).click();
        List<WebElement> we  = driver.findElements(By.id("Row Category Name"));
        we.get(2).click();
        eyes.open(driver, "Native App Test", "Native Page");
        eyes.checkWindow("Test");
        eyes.close();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}