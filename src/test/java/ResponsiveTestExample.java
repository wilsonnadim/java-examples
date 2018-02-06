import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

@RunWith(Parallell.class)
public class ResponsiveTestExample {

    protected Integer width;
    protected Integer height;

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
        env.add(new Integer[]{454, 675});
        env.add(new Integer[]{630, 699});
        env.add(new Integer[]{860, 640});
        env.add(new Integer[]{1212, 666});

        return env;
    }

    public ResponsiveTestExample(Integer width, Integer height) {
        this.width = width;
        this.height = height;
    }

    private Eyes eyes = new Eyes();
    private WebDriver driver;
    public static String applitoolsKey = "your_applitools_key";

    private static BatchInfo batch;

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo("Bloomberg Selenium");
    }

    @Before
    public void setUp() throws Exception {

        eyes.setApiKey(applitoolsKey);
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.LAYOUT2);
        eyes.setBatch(batch);

        driver = new ChromeDriver();
        //driver = new FirefoxDriver();

        driver.get("https://www.bloomberg.com");
    }

    @Test
    public void BloombergResponsive() throws Exception {
        eyes.open(driver, "Bloomberg", "Responsive Bloomberg", new RectangleSize(width, height));

        String w = Integer.toString(width);
        String h = Integer.toString(height);

        driver.findElement(By.id("closeChromeTout")).click();
        JavascriptExecutor js =(JavascriptExecutor)driver;
        js.executeScript("document.querySelector('body > bb-mini-player').setAttribute('style', 'display:none')");

        eyes.checkWindow(w + "x" + h);
        //eyes.checkRegion(By.cssSelector("div.mx-auto.col-sm-8.col-md-5.hide-sm"));

        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}