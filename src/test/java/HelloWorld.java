import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;

public class HelloWorld {

    WebDriver driver = new ChromeDriver();
    EyesRunner runner = new ClassicRunner();
    Eyes eyes = new Eyes(runner);
    private BatchInfo batchInfo = new BatchInfo();

    @Before
    public void setUp() throws Exception {

        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setServerUrl("https://eyesapi.applitools.com");
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        //eyes.setProxy(new ProxySettings("http://192.168.1.155", 8000));
        batchInfo.setNotifyOnCompletion(true);
        eyes.setBatch(batchInfo);
    }

    @Test
    public void HelloWorldTest() throws Exception {

        // Navigate the browser to the "hello world!" web-site.
        driver.get("https://applitools.com/helloworld");

        // Start the test and set the browser's viewport size to 800x600.
        eyes.open(driver, "Hello World", "My first Selenium Java test!", new RectangleSize(1200, 800));

        // Visual checkpoint #1.
        eyes.check("Hello!", Target.window());

        // Click the "Click me!" button.
        driver.findElement(By.tagName("button")).click();

        // Visual checkpoint #2.
        eyes.check("Click!", Target.window());

        eyes.closeAsync();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();

        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        TestResultContainer[] results = allTestResults.getAllResults();
        for(TestResultContainer result: results){
            TestResults test = result.getTestResults();
            assertEquals(test.getName() + " has mismatches", 0, test.getMismatches());
        }

        eyes.abort();
    }

}