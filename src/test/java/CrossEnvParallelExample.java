import com.applitools.eyes.*;
import com.applitools.eyes.selenium.Eyes;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

@RunWith(Parallell.class)
public class CrossEnvParallelExample {

    //AppName should really stay static.
    private String appName = "Applitools";

    //Dynamically get the testName from the test class name.
    @Rule
    public TestName name = new TestName() {
        public String getMethodName() {
            return String.format("%s", super.getMethodName().substring(0, super.getMethodName().length() - 3));
        }
    };

    //Keep the viewport size consistent. Only change these if you need to test responsiveness of your site.
    //Keep in mind that changing these values will generate a new baseline...
    private RectangleSize viewport = new RectangleSize(1300, 900);

    //Set your variables...
    private Eyes eyes = new Eyes();
    private static BatchInfo batch;
    private WebDriver driver;

    private String  browser;
    private Boolean baseline;

    public static class myBrowsers{
        public String browser = "Chrome";
        Boolean baseline = true;
        Boolean execute  = false;

        public myBrowsers(String browser, Boolean baseline, Boolean execute) {
            this.browser  = browser;
            this.baseline = baseline;
            this.execute  = execute;
        }
    }

    //Note: The first time you run this. Set which browser you want to be your baseline by setting it to true and execute to true. Set all other browsers to false, false.
    //Run your baseline tests... After completion you now have baselines for this environment. e.g. My-MacOSX-Chrome
    //Now define the other browsers you want to compare against (e.g. firefox and safari) and set baseline to false and execute to true.
    //Its a good practice to run your baseline environment is conjunction with your cross-env browsers so you can update the baseline if needed.
    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList<myBrowsers> environments = new LinkedList<myBrowsers>();
        environments.add(new myBrowsers("Chrome",   true,   true));
        environments.add(new myBrowsers("Firefox",  false,  true));
        environments.add(new myBrowsers("Safari",   false,  true));
//        env.add(new Object[]{"Chrome",  true,  true});

        LinkedList env = new LinkedList();
        for(int i=0; i<environments.size(); i++){
            if (!environments.get(i).execute) {
                environments.remove(environments.get(i));
            } else {
                env.add(new Object[]{environments.get(i).browser, environments.get(i).baseline});
            }
        }

        return env;
    }

    public CrossEnvParallelExample(String browser, Boolean baseline) {
        this.browser  = browser;
        this.baseline = baseline;
    }

    private void VisualCheck(String url) throws InterruptedException {
        driver.get("https://www.applitools.com" + url);
        eyes.open(this.driver, this.appName, name.getMethodName(), this.viewport);
        eyes.checkWindow("isBaseline: " + baseline);
        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo("Cross-Environment-Tests");
    }

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setLogHandler(new StdoutLogHandler(true));
        //Identify your environment baseline name with a name you can easily recognize...
        //See Environments here: https://eyes.applitools.com/app/environments?accountId=<your-account-id~~>
        eyes.setBaselineEnvName("My-MacOSX-Chrome");
        eyes.setBatch(batch);

        //Layout should be used on cross-env tests.
        //Almost everything will fail when using a different match level otherwise.
        eyes.setMatchLevel(MatchLevel.LAYOUT2);

        //Optional... add a customized property to filter baseline results
        String booleanString = String.valueOf(baseline);
        eyes.addProperty("Baselines", booleanString);

        //You should avoid ever using these methods unless absolutely necessary.
        //Let the SDK detect the OS/Browser and always set the viewport in the eyes.open method...
        //eyes.setExplicitViewportSize(new RectangleSize(1300, 900));
        //eyes.setHostApp("Chrome");
        //eyes.setHostOS("Mac OS X 10.14");

        if (browser.equals("Chrome")) {
            driver = new ChromeDriver();
        } if (browser.equals("Firefox")) {
            driver = new FirefoxDriver();
        } if (browser.equals("Safari")) {
            driver = new SafariDriver();
        };
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }

    @Test
    public void HomePage() throws Exception {
        VisualCheck("/");
    }

    @Test
    public void HelloWorldPage() throws Exception {
        VisualCheck("/helloworld");
    }

    @Test
    public void BlogPage() throws Exception {
        VisualCheck("/blog");
    }

    @Test
    public void CustomersPage() throws Exception {
        VisualCheck("/customers");
    }
}