import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;

public class FullPageScreenShotExample {

    private Eyes eyes = new Eyes();
    private WebDriver driver;
    public static String applitoolsKey = "your_applitools_key";

    @Before
    public void setUp() throws Exception {

        //eyes.setServerUrl(URI.create("https://your-onprem-server"));
        eyes.setApiKey(applitoolsKey);
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.STRICT);
        //set new baseline images...
        //eyes.setSaveFailedTests(true);

        //output detailed log data
        //eyes.setLogHandler(new StdoutLogHandler(true));

        driver = new ChromeDriver();
        driver.get("https://www.github.com");

        //for sites with older or non-standard css structure. Need to inject JS so SDK can get correct screen size.
        JavascriptExecutor js =(JavascriptExecutor)driver;
//        js.executeScript("$('html').css('overflow-y','visible');");
//        js.executeScript("$('html').css('overflow-x','visible');");
//        js.executeScript("$('html').css('height','unset');");
    }

    @Test
    public void GithubHomePage() throws Exception {
        eyes.open(driver, "Github", "Home Page", new RectangleSize(1035, 635));
        eyes.checkWindow("1000x800");
        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}