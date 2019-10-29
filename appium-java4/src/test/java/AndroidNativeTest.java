import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.appium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.concurrent.TimeUnit;


public class AndroidNativeTest {

    public static void main(String[] args) throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android");
        //capabilities.setCapability("platformVersion", "6.0");
        capabilities.setCapability("app", "/Users/justin/repos/applitools/java-examples/appium-java4/app-debug.apk");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("newCommandTimeout", 300);

        AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

        driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);

        Eyes eyes = new Eyes();

        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setForceFullPageScreenshot(true);
        eyes.setMatchTimeout(1000);
        //eyes.setApiKey("YourApiKey");

        try {

            eyes.open(driver, "Applitools Demo", "Appium Native Android with Full Page Screenshot");

            eyes.check( Target.window().fully());

//            eyes.check("Fluent - Region by Selector and Element", Target.window()
//                    .ignore(icons_array)
//                    .ignore(element)
//                    .ignore(By.cssSelector("div.mx-auto.col-sm-8.col-md-5.hide-sm"))
//                    .region(By.className("MyNavBar")).matchLevel(MatchLevel.LAYOUT2));

            eyes.close();

        } finally {

            driver.quit();

            eyes.abortIfNotClosed();

        }

    }
}
