import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;


public class HelloWorldRCA {

    private EyesRunner runner = new ClassicRunner();
    private Eyes eyes = new Eyes(runner);
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

        eyes.open(driver, "My Hello World!", "RCA Example", new RectangleSize(1000, 600));

        // Visual checkpoint #1.
        eyes.check("Hello!", Target.window());

        // Click the "Click me!" button.
        driver.findElement(By.tagName("button")).click();

        // Visual checkpoint #2.
        eyes.check("Click!", Target.window());

        eyes.closeAsync();
    }

    @Test
    public void HelloWorldDiff() throws Exception {
        // Navigate the browser to the "hello world!" web-site.
        driver.get("https://applitools.com/helloworld");

        eyes.open(driver, "My Hello World!", "RCA Example", new RectangleSize(1000, 600));

        // Visual checkpoint #1.
        eyes.check("Hello!", Target.window());

        //Delete the Thumbs up image
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("var elem = document.querySelector('div.section.image-section'); elem.parentNode.removeChild(elem)");

        // Visual checkpoint #2.
        eyes.check("Click!", Target.window());

        eyes.closeAsync();
    }

    @After
    public void tearDown() throws Exception {
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        TestResultContainer[] results = allTestResults.getAllResults();
        for(TestResultContainer result: results){
            TestResults test = result.getTestResults();
            assertEquals(test.getName() + " has mismatches", 0, test.getMismatches());
        }

        driver.quit();
        eyes.abort();
    }
}
