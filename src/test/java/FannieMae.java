import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;

public class FannieMae {

    public static void main(String[] args) {

        System.out.println("Welcome to APPLITOOLS Training!!");

        // Comment out below line if using windows machine - for mac provide your chromedrivear location
        //System.setProperty("webdriver.chrome.driver", "/Users/r2utev/Downloads/chromedriver77");

        WebDriver driver = new ChromeDriver();

        // Initialize the eyes SDK
        EyesRunner runner = new ClassicRunner();
        Eyes eyes = new Eyes(runner);

        // Set Applitools API Key
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        eyes.setForceFullPageScreenshot(true);

        // Set your proxy server
        //eyes.setProxy(new ProxySettings(AppConstants.PROXY_URL, AppConstants.PROXY_PORT_NUMBER));

        // Set Eyes Server
        // Default applitools cloud https://eyesapi.applitools.com
        eyes.setServerUrl("https://eyesapi.applitools.com");

        // To group multiple tests
        BatchInfo batch = new BatchInfo("Justin - Applitools Training");
        eyes.setBatch(batch);

        // Set Match Level (before eyes.open)s -- Exact, Strict, Content, Layout
        //eyes.setMatchLevel(MatchLevel.CONTENT);

        try {

            // Set Application Name, Test Name and Viewport Size
            eyes.open(driver, "applitools.com", "Hello World - New",
                    new RectangleSize(1000, 600));

            // Navigate the browser to specified URL
            driver.get("https://applitools.com/helloworld");

            driver.findElement(By.tagName("button")).click();

            // Visual Validation Check Point 1
            eyes.checkWindow("HelloWorld");

            driver.get("https://applitools.com/helloworld?diff1");

            // Visual Validation Check Point 2
            eyes.check("Diff1", Target.window());

            driver.get("https://applitools.com/helloworld?diff2");

            // Visual Validation Check Point 3
            eyes.check("Diff2", Target.window());

            eyes.closeAsync();


        } catch (Exception e) {

            System.out.println(e);

        } finally {

            TestResultsSummary allTestResults = runner.getAllTestResults(false);
            TestResultContainer[] results = allTestResults.getAllResults();
            for(TestResultContainer result: results){
                TestResults test = result.getTestResults();
                assertEquals(test.getName() + " has mismatches", 0, test.getMismatches());
            }

            // Abort test in case of an unexpected error
            eyes.abortIfNotClosed();

            // Close the browser.
            driver.quit();

            // End main test
            System.exit(0);
        }
    }
}
