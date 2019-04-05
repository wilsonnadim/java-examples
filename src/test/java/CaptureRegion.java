import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;

public class CaptureRegion {

    private Eyes eyes = new Eyes();
    private WebDriver driver;
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(applitoolsKey);
       // eyes.setForceFullPageScreenshot(true);
        //eyes.setStitchMode(StitchMode.CSS);
        driver = new ChromeDriver();
        driver.get("https://www.github.com");
        Thread.sleep(10000);
    }

    @Test
    public void StocksPage() throws Exception {
        eyes.open(driver, "Github Regions", "Check Region - Github", new RectangleSize(1200, 650));

       eyes.check("chart", Target.region(By.cssSelector("div.chart-container")).fully());

        eyes.checkWindow("test");
        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}