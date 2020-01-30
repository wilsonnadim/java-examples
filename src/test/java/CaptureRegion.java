import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.Assert.assertEquals;

public class CaptureRegion {

    private EyesRunner runner = new ClassicRunner();
    private Eyes eyes = new Eyes(runner);
    private WebDriver driver;
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(applitoolsKey);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        driver = new ChromeDriver();
        driver.get("https://www.github.com");
        //eyes.setProxy(new ProxySettings("http://YOUR-PROXY-URI", 2333));
    }

    @Test
    public void GithubRegionTest() throws Exception {
        eyes.open(driver, "Github.com", "Check Region", new RectangleSize(1200, 800));

        eyes.check("Circle Badge Clasic", Target.region(By.cssSelector("img.CircleBadge-icon")));

        WebElement element = driver.findElement(By.cssSelector("img.CircleBadge-icon"));
        eyes.check("Fluent by Element", Target.region(element).ignoreDisplacements(true));

        eyes.check("GH", Target.region(By.cssSelector("div.mx-auto.col-sm-8.col-md-6.hide-sm")));

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
        eyes.abortIfNotClosed();
    }
}