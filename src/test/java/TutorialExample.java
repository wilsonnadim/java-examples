import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.Assert.assertEquals;

public class TutorialExample {

    EyesRunner runner = new ClassicRunner();
    private Eyes eyes = new Eyes(runner);
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {

        eyes.setServerUrl("https://eyesapi.applitools.com");
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        driver = new ChromeDriver();
    }

    @Test
    public void Demo() throws Exception {
        driver.get("https://demo.applitools.com");
        eyes.open(driver, "Demo App", "Login Page", new RectangleSize(800, 600));
        eyes.check("Login Window", Target.window());
        eyes.closeAsync();
    }

    @After
    public void tearDown() throws Exception {
        TestResultsSummary allTestResults = runner.getAllTestResults();
        TestResultContainer[] results = allTestResults.getAllResults();
        for(TestResultContainer result: results){
            TestResults test = result.getTestResults();
            assertEquals(test.getName() + " has mismatches", 0, test.getMismatches());
        }

        driver.quit();
        eyes.abortIfNotClosed();
    }
}