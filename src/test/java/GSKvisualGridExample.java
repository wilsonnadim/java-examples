import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.rendering.Eyes;
import com.applitools.eyes.rendering.Target;
import com.applitools.eyes.visualGridClient.model.RenderingConfiguration;
import com.applitools.eyes.visualGridClient.model.TestResultSummary;
import com.applitools.eyes.visualGridClient.services.VisualGridManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class GSKvisualGridExample {

    private VisualGridManager VisualGrid = new VisualGridManager(25);
    private RenderingConfiguration renderConfig = new RenderingConfiguration();
    private Eyes eyes = new Eyes(VisualGrid);
    private WebDriver driver;
    public static String applitoolsKey = "q4UFHgi1irz3GpQKi9990DD104aM3iZwmmvPvruX104253aI110"; //System.getenv("APPLITOOLS_API_KEY");

    private static BatchInfo batch;

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo("Excedrin.com - Visual Grid Test");
    }

    @Before
    public void setUp() throws Exception {

        renderConfig.setAppName("Excedrin.com");
        renderConfig.setTestName("Home Page");
        renderConfig.addBrowser(300,  600, RenderingConfiguration.BrowserType.FIREFOX);
        renderConfig.addBrowser(300,  600, RenderingConfiguration.BrowserType.CHROME);
        renderConfig.addBrowser(600,  600, RenderingConfiguration.BrowserType.FIREFOX);
        renderConfig.addBrowser(600,  600, RenderingConfiguration.BrowserType.CHROME);
        renderConfig.addBrowser(1000,  600, RenderingConfiguration.BrowserType.FIREFOX);
        renderConfig.addBrowser(1000,  600, RenderingConfiguration.BrowserType.CHROME);
        renderConfig.addBrowser(1400,  600, RenderingConfiguration.BrowserType.FIREFOX);
        renderConfig.addBrowser(1400,  600, RenderingConfiguration.BrowserType.CHROME);

        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setApiKey(applitoolsKey);
        eyes.setBatch(batch);

        driver = new ChromeDriver();
        driver.get("https://excedrin.com/");

        JavascriptExecutor js =(JavascriptExecutor)driver;
        Long height = (Long) js.executeScript("return document.body.scrollHeight;");
        for(int i = 0 ; i < height/100 ; i++)
            js.executeScript("window.scrollBy(0,100)");
    }

    @Test
    public void ExcedinHomePage() throws Exception {

        eyes.open(driver, renderConfig);
        eyes.check("Home Page", Target.window());
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