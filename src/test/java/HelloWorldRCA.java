import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertTrue;


public class HelloWorldRCA {

    private Eyes eyes = new Eyes();
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {

        //eyes.setServerUrl("YourApplitoolsServer");
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setHideScrollbars(true);
        eyes.setHideCaret(true);
        eyes.setSendDom(true);
        eyes.setLogHandler(new StdoutLogHandler(true));

        BatchInfo batch = new BatchInfo("Hello World RCA");
        eyes.setBatch(batch);

        driver = new ChromeDriver();
    }

    @Test
    public void HelloWorld() throws Exception {
        // Navigate the browser to the "hello world!" web-site.
        driver.get("https://applitools.com/helloworld");

        eyes.open(driver, "My Hello World!", "RCA Example", new RectangleSize(1200, 800));

        // Visual checkpoint #1.
        eyes.checkWindow("Hello!");

        // Click the "Click me!" button.
        driver.findElement(By.tagName("button")).click();

        // Visual checkpoint #2.
        eyes.checkWindow("Click!");

        // End the test.
        TestResults results = eyes.close(false);
        assertTrue(results.isPassed());
    }

    @Test
    public void HelloWorldDiff() throws Exception {
        // Navigate the browser to the "hello world!" web-site.
        driver.get("https://applitools.com/helloworld");

        eyes.open(driver, "My Hello World!", "RCA Example", new RectangleSize(1200, 800));

        // Visual checkpoint #1.
        eyes.checkWindow("Hello!");

        // Click the "Click me!" button.
        driver.findElement(By.tagName("button")).click();

        //Delete the Thumbs up image
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("var elem = document.querySelector('div.section.image-section'); elem.parentNode.removeChild(elem)");

        // Visual checkpoint #2.
        eyes.checkWindow("Click!");

        // End the test.
        TestResults results = eyes.close(false);
        assertTrue(results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}
