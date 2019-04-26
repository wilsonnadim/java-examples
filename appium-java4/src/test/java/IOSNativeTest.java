

import com.applitools.eyes.LogHandler;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.appium.Eyes;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class IOSNativeTest {

    public static void main(String[] args) throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("deviceName", "iPhone 7");
        capabilities.setCapability("platformVersion", "12.2");
        capabilities.setCapability("app", "/Users/justin/repos/applitools/java-examples/appium-java4/iOS-Example-App.app");
        capabilities.setCapability("clearSystemFiles", true);
        capabilities.setCapability("noReset", true);
        capabilities.setCapability("automationName", "XCUITest");

        AppiumDriver driver = new IOSDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

        Eyes eyes = new Eyes();
        LogHandler logHandler = new StdoutLogHandler(true);
        eyes.setLogHandler(logHandler);
        eyes.setForceFullPageScreenshot(true);
        //eyes.setApiKey("YourAPIKey");

        try {

            eyes.open(driver, "Contacts!", "My first Appium iOS native Java test!");

            WebElement clickme = driver.findElement(MobileBy.AccessibilityId("show table view"));
            Point clp = clickme.getLocation();
            Dimension cd = clickme.getSize();
            clickme.click();
            eyes.checkWindow("Contact list!");

            eyes.close();
        } finally {
            driver.quit();
            eyes.abortIfNotClosed();
        }
    }

}
