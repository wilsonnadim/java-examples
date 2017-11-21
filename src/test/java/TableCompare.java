import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.LinkedList;

@RunWith(Parallell.class)
public class TableCompare {
    protected Integer width;
    protected Integer height;

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
//        env.add(new Integer[]{454, 675});
        //env.add(new Integer[]{950, 489});
        env.add(new Integer[]{964, 680});
        env.add(new Integer[]{1115, 680});

        return env;
    }

    public TableCompare(Integer width, Integer height) {
        this.width = width;
        this.height = height;
    }

    private Eyes eyes = new Eyes();
    private WebDriver driver;
    public static String applitoolsKey = "your-applitools-key";

    private static BatchInfo batch;

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo("Table Test");
    }

    @Before
    public void setUp() throws Exception {

        eyes.setApiKey(applitoolsKey);
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.LAYOUT2);
        eyes.setBatch(batch);
        eyes.setBaselineEnvName("Table");

        driver = new ChromeDriver();
        driver.get("https://datatables.net/examples/basic_init/scroll_y.html");
    }

    @Test
    public void NetAppHomeResponsive() throws Exception {
        eyes.open(driver, "Table Example2", "Validate Scrollable Table2", new RectangleSize(width, height));

        String w = Integer.toString(width);
        String h = Integer.toString(height);

        eyes.check("Table", Target.region(By.cssSelector("div.dataTables_scrollBody")).fully());

        eyes.close(true);

    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}