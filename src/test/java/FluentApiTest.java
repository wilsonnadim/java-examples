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

public class FluentApiTest {

    private Eyes eyes = new Eyes();
    private WebDriver driver;
    public static String applitoolsKey = "your-applitools-key";

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
    public void NetAppHomeResponsive() throws Exception {
        eyes.open(driver, "Fluent Test", "Github", new RectangleSize(1000, 700));

        WebElement element = driver.findElement(By.cssSelector("a.header-logo-invertocat.my-0")); //github logo top left
        eyes.check("Fluent - Region by Selector and Element", Target.window()
                .ignore(By.cssSelector("div.mx-auto.col-sm-8.col-md-5.hide-sm"))
                .ignore(element).fully());

        eyes.close();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}