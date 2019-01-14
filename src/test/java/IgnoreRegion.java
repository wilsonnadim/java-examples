import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import static org.junit.Assert.assertTrue;

public class IgnoreRegion {

    private Eyes eyes = new Eyes();
    private WebDriver driver;
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(applitoolsKey);
        //driver = new FirefoxDriver();
        //driver = new SafariDriver();
        //driver = new ChromeDriver();
        SafariOptions safariOptions = new SafariOptions();
        safariOptions.setUseTechnologyPreview(true);
        driver = new SafariDriver(safariOptions);
    }

    @Test
    public void Github() throws Exception {

        driver.get("https://www.github.com");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 0);");

        eyes.open(driver, "Github.com", "Ignore Region", new RectangleSize(1200, 800));

        WebElement element = driver.findElement(By.className("position-relative")); //github logo top left
        eyes.check("Fluent - Region by Selector and Element", Target.window()
            .ignore(By.cssSelector("div.mx-auto.col-sm-8.col-md-5.hide-sm")) //ignores the login form
            .ignore(element)); //ignores github logo top left

        TestResults results = eyes.close(false);
        assertTrue(results.isPassed());

    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}