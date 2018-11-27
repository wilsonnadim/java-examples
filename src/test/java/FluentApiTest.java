import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.RectangleSize;
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
        eyes.setMatchLevel(MatchLevel.LAYOUT2);
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

        eyes.check("Fluent - Region by Selector and Element", Target.window().ignore(icons_array)
                .ignore(By.cssSelector("div.mx-auto.col-sm-8.col-md-5.hide-sm"))
                .ignore(element));

        eyes.close();
    }


    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}