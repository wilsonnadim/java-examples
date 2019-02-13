import com.applitools.eyes.FileLogger;
import com.applitools.eyes.rendering.Eyes;
import com.applitools.eyes.rendering.Target;
import com.applitools.eyes.visualGridClient.model.RenderingConfiguration;
import com.applitools.eyes.visualGridClient.model.TestResultSummary;
import com.applitools.eyes.visualGridClient.services.VisualGridManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class visualGridExampleJNJ {

    private VisualGridManager VisualGrid = new VisualGridManager(10);
    private RenderingConfiguration renderConfig = new RenderingConfiguration();
    private Eyes eyes = new Eyes(VisualGrid);
    private WebDriver driver;
    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");

    @Before
    public void setUp() throws Exception {

        renderConfig.setAppName("Reactine.ca"); //define a static App name. This should rarely change...
        renderConfig.setTestName("Reactine Visual Grid Test"); //Define a test name. This should be unique...
        renderConfig.addBrowser(800,  600, RenderingConfiguration.BrowserType.CHROME);
        renderConfig.addBrowser(700,  500, RenderingConfiguration.BrowserType.FIREFOX);
        renderConfig.addBrowser(1200, 800, RenderingConfiguration.BrowserType.CHROME);

        eyes.setApiKey(applitoolsKey);
        //Capture logs...
        //eyes.setLogHandler(new StdoutLogHandler(true));
        //or
        eyes.setLogHandler(new FileLogger("/Users/justin/visualGrid.log",true,true));

        driver = new ChromeDriver();
        driver.get("https://www.reactine.ca/");
    }

    @Test
    public void ReactineHomePage() throws Exception {

        JavascriptExecutor js =(JavascriptExecutor)driver;
        js.executeScript("var elem = document.querySelector('#fb-root > div > div:nth-child(1) > iframe'); elem.parentNode.removeChild(elem);");

        //close popup
        driver.findElement(By.cssSelector("button.agree-button.click-processed")).click();

        eyes.open(driver, renderConfig);
        eyes.check("Reactine Home Page", Target.window());
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