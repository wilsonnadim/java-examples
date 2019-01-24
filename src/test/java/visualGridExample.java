import com.applitools.eyes.rendering.Eyes;
import com.applitools.eyes.rendering.Target;
import com.applitools.eyes.visualGridClient.model.RenderingConfiguration;
import com.applitools.eyes.visualGridClient.model.TestResultSummary;
import com.applitools.eyes.visualGridClient.services.VisualGridManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class visualGridExample {

    private VisualGridManager VisualGrid = new VisualGridManager(10);
    private RenderingConfiguration renderConfig = new RenderingConfiguration();
    private Eyes eyes = new Eyes(VisualGrid);
    private WebDriver driver;
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Before
    public void setUp() throws Exception {

        renderConfig.setTestName("Hello World");
        renderConfig.setAppName("Rendering Grid Test");
        renderConfig.addBrowser(800,  600, RenderingConfiguration.BrowserType.CHROME);
        renderConfig.addBrowser(700,  500, RenderingConfiguration.BrowserType.FIREFOX);
        renderConfig.addBrowser(1200, 800, RenderingConfiguration.BrowserType.CHROME);

        eyes.setApiKey(applitoolsKey);

        driver = new ChromeDriver();
        driver.get("https://applitools.com/helloworld");
    }

    @Test
    public void HelloWorld() throws Exception {

        eyes.open(driver, renderConfig);
        eyes.check("first check", Target.window().withName("Step 1 A"));
        eyes.check("second check", Target.window().fully(false).withName("Step 1 B"));
        driver.findElement(By.tagName("button")).click();
        eyes.check("third check", Target.window().withName("Step 2 A"));
        eyes.check("forth check", Target.window().fully(false).withName("Step 2 B"));

        eyes.close();

        TestResultSummary allTestResults = VisualGrid.getAllTestResults();
        System.out.println("Results: " + allTestResults);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}