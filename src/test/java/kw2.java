import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class kw2 {

    public static void main(String[] args) {

        // Open a Chrome browser.
        WebDriver driver = new ChromeDriver();
        //WebDriver driver = new FirefoxDriver();

        // Initialize the eyes SDK and set your private API key.
        Eyes eyes = new Eyes();
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setHideScrollbars(false);
        eyes.setWaitBeforeScreenshots(3000);
        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        try{

            // Navigate the browser
            driver.get("https://console-qa.command.kw.com");

            //Username: kelle123412345
            //Password: w2EgJsFy

            // Set breakpoint here. Login manually with above credentials
            driver.get("https://console-qa.command.kw.com/command/opportunities/details?contactId=511480&id=161959&teamId=");

            //Your Code HERE

            // Start the test
            eyes.open(driver, "kw.com", "listing", new RectangleSize(1200, 800));

            //This gives me a fullpage screenshot
            eyes.check("Window", Target.window().fully(true));

            //This also gives me a fullpage screenshot
            eyes.check("Region", Target.region(By.cssSelector("body")).fully(true));

            TestResults results = eyes.close(false);


        } finally {

            // Close the browser.
            driver.quit();

            // If the test was aborted before eyes.close was called, ends the test as aborted.
            eyes.abortIfNotClosed();
        }

    }

}