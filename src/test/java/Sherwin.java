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

import static org.junit.Assert.assertEquals;

public class Sherwin {

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
        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setBatch(batch);

        driver = new ChromeDriver();

        driver.get("https://www.sherwin-williams.com/painting-contractors/products/exterior-paint-coatings/deck-products/finishes#facet:&productBeginIndex:0&orderBy:&pageView:grid&minPrice:&maxPrice:&pageSize:45&");
    }

    @Test
    public void FluentTest() throws Exception {
        eyes.open(driver, "Sherwin Williams", "Fluent Test", new RectangleSize(1200, 800));

        List<WebElement> ratings = driver.findElements(By.className("prod-shelf__product__ratings-wrapper"));
        WebElement[] ratings_array = new WebElement[ratings.size()];
        ratings.toArray(ratings_array);

        eyes.check("Fluent - Region and Ignore array", Target.region(driver.findElement(By.id("ProductShelfContainer"))).ignore(ratings_array));

        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}