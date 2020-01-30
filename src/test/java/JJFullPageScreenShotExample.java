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
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;

public class JJFullPageScreenShotExample {

    private EyesRunner runner = new ClassicRunner();
    private Eyes eyes = new Eyes(runner);
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {

        eyes.setServerUrl("https://eyesapi.applitools.com");
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.STRICT);
        eyes.setLogHandler(new StdoutLogHandler(true));

        driver = new ChromeDriver();
        driver.get("https://www.jnj.com/");
    }

    @Test
    public void JJHomePage() throws Exception {
        eyes.open(driver, "J&J2", "Home Page2", new RectangleSize(1000, 700));

        driver.findElement(By.cssSelector("button.CookieBanner-button")).click();

        eyes.check("J&J", Target.window());
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