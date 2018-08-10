import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.Assert.assertEquals;

public class IgnoreRegion {

    private Eyes eyes = new Eyes();
    private WebDriver driver;
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(applitoolsKey);
        driver = new FirefoxDriver();
    }

    @Test
    public void Applitools() throws Exception {
        for (int i = 0; i < 5; i++) {

            driver.get("https://www.github.com");
            eyes.open(driver, "Github.com", "Ignore Region", new RectangleSize(1200, 800));

            WebElement element = driver.findElement(By.cssSelector("a.header-logo-invertocat.my-0")); //github logo top left
            eyes.check("Fluent - Region by Selector and Element", Target.window()
                .ignore(By.cssSelector("div.mx-auto.col-sm-8.col-md-5.hide-sm")) //ignores the login form
                .ignore(element)); //ignores github logo top left

            TestResults results = eyes.close(false);
            assertEquals(true, results.isPassed());

        }
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}