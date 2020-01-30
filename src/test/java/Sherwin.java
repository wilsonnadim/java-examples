import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
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

    private EyesRunner runner = new ClassicRunner();
    private Eyes eyes = new Eyes(runner);
    private WebDriver driver;
    private static BatchInfo batch;

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo("Fluent Test");
    }

    @Before
    public void setUp() throws Exception {

        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
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
        eyes.closeAsync();
    }

    @After
    public void tearDown() throws Exception {
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        TestResultContainer[] results = allTestResults.getAllResults();
        for (TestResultContainer result : results) {
            TestResults test = result.getTestResults();
            assertEquals(test.getName() + " has mismatches", 0, test.getMismatches());
        }

        driver.quit();
        eyes.abort();
    }
}