import com.applitools.eyes.*;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class FluentApiTest {

    private Eyes eyes = new Eyes();
    private WebDriver driver;
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");
    private static BatchInfo batch;

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo("Fluent Test");
    }

    @Before
    public void setUp() throws Exception {

        eyes.setApiKey(applitoolsKey);
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);

        eyes.setMatchLevel(MatchLevel.STRICT);

        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setBatch(batch);

        driver = new ChromeDriver();

        driver.get("http://www.github.com");
    }

    @Test
    public void FluentTest() throws Exception {
        eyes.open(driver, "Fluent Test", "Github", new RectangleSize(1000, 700));

        List<WebElement> icons = driver.findElements(By.cssSelector("img.CircleBadge-icon"));
        WebElement[] icons_array = new WebElement[icons.size()];
        icons.toArray(icons_array);

        WebElement element = driver.findElement(By.cssSelector("a.mr-4")); //github logo top left

        eyes.check("Fluent - Region by Selector and Element", Target.window()
                .ignore(icons_array)
                .ignore(element)
                .ignore(By.cssSelector("div.mx-auto.col-sm-8.col-md-5.hide-sm")));

        eyes.check("Fluent - programaticaly add content region", Target.window().ignore(By.id("YouContentRegionID")));

        eyes.check("Fluent - Ignore Displacements", Target.window().ignoreDisplacements(true));

        eyes.check("Fluent - Add Region Match Level", Target.region(By.cssSelector("myID")).matchLevel(MatchLevel.CONTENT));

        eyes.check("Fluent - Add Region Match Level", Target.frame("myFrameId"));

        TestResults results = eyes.close(false);
        //assertEquals(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}