import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;

public class JJFullPageScreenShotExample {

    private Eyes eyes = new Eyes();
    private WebDriver driver;
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Before
    public void setUp() throws Exception {

        //eyes.setServerUrl(URI.create("https://your-onprem-server"));
        eyes.setApiKey(applitoolsKey);
        eyes.setHideScrollbars(true);
        //eyes.setForceFullPageScreenshot(true);
        //eyes.setStitchMode(StitchMode.CSS);
        //eyes.setMatchLevel(MatchLevel.LAYOUT2);
        //eyes.setLogHandler(new StdoutLogHandler(true));
        //eyes.setBaselineEnvName("OldOSX");

        driver = new ChromeDriver();
        driver.get("https://www.jnj.com/");
    }

    @Test
    public void JJHomePage() throws Exception {
        eyes.open(driver, "J&J2", "Home Page2", new RectangleSize(1000, 700));

        driver.findElement(By.cssSelector("button.CookieBanner-button")).click();

        eyes.checkWindow("J&J");

        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}