import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;

public class FullPageScreenShotEsriExample {

    private Eyes eyes = new Eyes();
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {

        eyes.setServerUrl("https://eyes.applitools.com");
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        //eyes.setLogHandler(new StdoutLogHandler(true));
        driver = new ChromeDriver();
        eyes.setSendDom(false);
    }

    @Test
    public void Esri() throws Exception {
        driver.get("https://uat.esri.com/en-us/industries/education/schools/our-story");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        Integer heightSize = Integer.parseInt(js.executeScript("return document.documentElement.scrollHeight").toString());
        RectangleSize viewport = eyes.getViewportSize(driver);
        for (int j = 0; j < heightSize; j += viewport.getHeight() - 100) {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + j + ")");
            Thread.sleep(500);
        }

        eyes.open(driver, "esri.com", "Education - Our Story", new RectangleSize(1080, 800));

        eyes.check("Our Story", Target.region(By.cssSelector("body")).fully().ignoreDisplacements(true));

        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}