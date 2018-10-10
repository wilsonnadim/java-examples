import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class PerfectoTest {

    private static final int HEADER_SIZE = 65; // Should be adopted according to device
    private static final String USER_NAME = "your_perfecto_user";
    private static final String PASSWORD = "your_pefecto_password";
    private static final String MANUFACTURER = "Samsung";
    private static final String MODEL = "Galaxy S7";
    private static final String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    public static void main(String[] args) throws MalformedURLException, InterruptedException, UnsupportedEncodingException {

        Eyes eyes = new Eyes();

        eyes.setApiKey(applitoolsKey);

        DesiredCapabilities capabilities = new DesiredCapabilities("Chrome", "", Platform.ANY);

        capabilities.setCapability("user", USER_NAME);
        capabilities.setCapability("password", PASSWORD);
        //capabilities.setCapability("deviceName", "Galaxy S5");
        capabilities.setCapability("manufacturer", "Samsung");
        capabilities.setCapability("model", "Galaxy S6");
        capabilities.setCapability("platformVersion", "7.0");
        capabilities.setCapability("platformName", "Android");

        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 200);

        String user = URLEncoder.encode(USER_NAME, "UTF-8");
        String password = URLEncoder.encode(PASSWORD, "UTF-8");

        URL url = new URL("https://" + user + ":" + password + "@partners.perfectomobile.com/nexperience/wd/hub");

        RemoteWebDriver driver = new RemoteWebDriver(url, capabilities);

        try {

            driver.get("https://www.spectrum.com/");

            eyes.setForceFullPageScreenshot(true);
            eyes.setStitchMode(StitchMode.CSS);
            //eyes.setImageCut(new FixedCutProvider(HEADER_SIZE , 0, 0, 0));
            eyes.open(driver, "App Name", "Test Name");

            eyes.checkWindow("Main Page");

            eyes.close();

        } finally {

            eyes.abortIfNotClosed();

            driver.quit();

        }
    }
}