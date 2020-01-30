import com.applitools.eyes.*;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.Assert.assertEquals;

public class vgSimple {

    private EyesRunner vgRunner = new VisualGridRunner(10);
    private Eyes eyes = new Eyes(vgRunner);
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setServerUrl("https://eyesapi.applitools.com");
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setLogHandler(new StdoutLogHandler(true));

        Configuration config = eyes.getConfiguration();
        config.setAppName("VG");
        config.setTestName("Simple");
        config.addBrowser(1200,  700, BrowserType.CHROME);
        config.addBrowser(1200,  700, BrowserType.FIREFOX);
        config.addDeviceEmulation(DeviceName.iPhone_6_7_8, ScreenOrientation.PORTRAIT);
        eyes.setConfiguration(config);

        driver = new ChromeDriver();
    }

    @Test
    public void Google() throws Exception {
        driver.get("https://www.google.com");
        eyes.open(driver);
        eyes.check("check", Target.window().fully());
        eyes.closeAsync();
    }

    @After
    public void tearDown() throws Exception {
        TestResultsSummary allTestResults = vgRunner.getAllTestResults();
        TestResultContainer[] results = allTestResults.getAllResults();
        for(TestResultContainer result: results){
            TestResults test = result.getTestResults();
            assertEquals(test.getName() + " has mismatches", 0, test.getMismatches());
        }

        driver.quit();
        eyes.abortIfNotClosed();
    }
}
