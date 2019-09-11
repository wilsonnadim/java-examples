import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
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

        eyes.setServerUrl("https://eyes.applitools.com");
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        driver = new ChromeDriver();
    }

    @Test
    public void GithubHomePage() throws Exception {

        eyes.open(driver, "Demo App", "Smoke Test", new RectangleSize(800, 600));

        driver.get("https://demo.applitools.com");

        eyes.checkWindow("Login Window");

        eyes.closeAsync();

        TestResultsSummary allTestResults = runner.getAllTestResults();

        TestResultContainer[] results = allTestResults.getAllResults();
        for(TestResultContainer result: results){
            TestResults test = result.getTestResults();
            assertEquals(test.getName() + " has mismatches", 0, test.getMismatches());
        }

    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}