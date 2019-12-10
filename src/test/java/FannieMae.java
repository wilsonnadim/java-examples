import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class FannieMae {

    public static void main(String[] args) {

        System.out.println("Welcome to APPLITOOLS Training!!");

        // Comment out below line if using windows machine - for mac provide your chromedrivear location
        //System.setProperty("webdriver.chrome.driver", "/Users/r2utev/Downloads/chromedriver77");

        WebDriver driver = new ChromeDriver();

        // Initialize the eyes SDK
        Eyes eyes = new Eyes();

        // Set Applitools API Key
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        eyes.setForceFullPageScreenshot(true);

        // Set your proxy server
        //eyes.setProxy(new ProxySettings(AppConstants.PROXY_URL, AppConstants.PROXY_PORT_NUMBER));

        // Set Eyes Server
        // Default applitools cloud https://eyesapi.applitools.com
        eyes.setServerUrl("https://eyesapi.applitools.com");

        // print logs to console
        //eyes.setLogHandler(new StdoutLogHandler(true));

        // To group multiple tests
        BatchInfo batch = new BatchInfo("Justin - Applitools Training");
        eyes.setBatch(batch);

        // Set Match Level (before eyes.open) -- Exact, Strict, Content, Layout
        //eyes.setMatchLevel(MatchLevel.CONTENT);

        try {

            // Set Application Name, Test Name and Viewport Size
            eyes.open(driver, "applitools.com", "Hello World",
                    new RectangleSize(301, 600));

            // Navigate the browser to specified URL
            driver.get("https://applitools.com/helloworld");

            // Visual Validation Check Point 1
            eyes.checkWindow("HelloWorld");

            driver.get("https://applitools.com/helloworld?diff1");

            // Visual Validation Check Point 2
            eyes.checkWindow("Diff1");

            driver.get("https://applitools.com/helloworld?diff2");

            // Visual Validation Check Point 3
            eyes.checkWindow("Diff2");

            // End Visual Test
            eyes.close(false);


        } catch (Exception e) {

            System.out.println(e);

        } finally {

            // Abort test in case of an unexpected error
            eyes.abortIfNotClosed();

            // Close the browser.
            driver.quit();

            // End main test
            System.exit(0);
        }
    }
}
