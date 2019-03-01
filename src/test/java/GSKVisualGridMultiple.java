import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.rendering.Eyes;
import com.applitools.eyes.rendering.Target;
import com.applitools.eyes.visualGridClient.model.RenderingConfiguration;
import com.applitools.eyes.visualGridClient.model.TestResultSummary;
import com.applitools.eyes.visualGridClient.services.VisualGridManager;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

@RunWith(Parallell.class)
public class GSKVisualGridMultiple {

    @Rule
    public TestName name = new TestName() {
        public String getMethodName() {
            return String.format("%s", super.getMethodName().substring(0, super.getMethodName().length() - 3));
        }
    };

    protected String gskUrl;

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
        env.add(new String[]{"excedrin"});
        env.add(new String[]{"nicodermcq"});
        env.add(new String[]{"nicorette"});
        env.add(new String[]{"quit"});
        env.add(new String[]{"selzentry"});
        env.add(new String[]{"sensodyne"});

        return env;
    }

    public GSKVisualGridMultiple(String gskUrl) {
        this.gskUrl = gskUrl;
    }

    private void lazyLoadPage() throws InterruptedException {
        JavascriptExecutor js =(JavascriptExecutor)driver;
        Long height = (Long) js.executeScript("return document.body.scrollHeight;");
        for(int i = 0 ; i < height/100 ; i++)
            js.executeScript("window.scrollBy(0,100)");

        TimeUnit.SECONDS.sleep(1);
        js.executeScript("window.scrollTo(0, 0);");
        TimeUnit.SECONDS.sleep(1);
    }

    private VisualGridManager VisualGrid = new VisualGridManager(50);
    private RenderingConfiguration renderConfig = new RenderingConfiguration();
    private Eyes eyes = new Eyes(VisualGrid);
    private WebDriver driver;
    public static String applitoolsKey = "q4UFHgi1irz3GpQKi9990DD104aM3iZwmmvPvruX104253aI110";//System.getenv("APPLITOOLS_API_KEY");

    private static BatchInfo batch;

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo("GSK Sites - Visual Grid Test");
    }

    @Before
    public void setUp() throws Exception {

        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setApiKey(applitoolsKey);
        eyes.setBatch(batch);

        driver = new ChromeDriver();
        driver.get("https://www." + gskUrl + ".com");
        lazyLoadPage();
    }

    @Test
    public void GSKSites() throws Exception {
        renderConfig.setAppName(gskUrl + ".com");
        renderConfig.setTestName(gskUrl + " - Home Page");
        renderConfig.addBrowser(500,  600, RenderingConfiguration.BrowserType.FIREFOX);
        renderConfig.addBrowser(500,  600, RenderingConfiguration.BrowserType.CHROME);
        renderConfig.addBrowser(800,  600, RenderingConfiguration.BrowserType.FIREFOX);
        renderConfig.addBrowser(800,  600, RenderingConfiguration.BrowserType.CHROME);
        renderConfig.addBrowser(1100,  600, RenderingConfiguration.BrowserType.FIREFOX);
        renderConfig.addBrowser(1100,  600, RenderingConfiguration.BrowserType.CHROME);
        renderConfig.addBrowser(1400,  600, RenderingConfiguration.BrowserType.FIREFOX);
        renderConfig.addBrowser(1400,  600, RenderingConfiguration.BrowserType.CHROME);

        eyes.open(driver, renderConfig);
        eyes.check(gskUrl, Target.window().fully());
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