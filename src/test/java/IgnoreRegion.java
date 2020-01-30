import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
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
import static org.junit.Assert.assertEquals;

public class IgnoreRegion {

    private EyesRunner runner = new ClassicRunner();
    private Eyes eyes = new Eyes(runner);
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
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

        eyes.closeAsync();

    }

    @After
    public void tearDown() throws Exception {
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        TestResultContainer[] results = allTestResults.getAllResults();
        for(TestResultContainer result: results){
            TestResults test = result.getTestResults();
            assertEquals(test.getName() + " has mismatches", 0, test.getMismatches());
        }

        driver.quit();
        eyes.abort();
    }
}
}