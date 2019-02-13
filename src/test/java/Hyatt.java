import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Hyatt {

    private Eyes eyes = new Eyes();
    private WebDriver driver;

    protected List<WebElement> bookable = new ArrayList<WebElement>();
    protected List<WebElement> soldout = new ArrayList<WebElement>();
    protected List<WebElement> notBookable = new ArrayList<WebElement>();
    protected List<WebElement> newBooking = new ArrayList<WebElement>();

    //this is your viewport setting. You can modify these to test responsive design. Ideally run in parallel with a linked list array.
    //Keep in-mind adjusting these values will create a new baseline...
    protected Integer width = 1250;
    protected Integer height = 800;
    protected String appName = "Hyatt.com";

    private static BatchInfo batch;

    @BeforeClass
    //Create a Batch Object ID in the before class to apply to all tests below.
    public static void batchInitialization(){
        batch = new BatchInfo("Hyatt.com Search Results");
    }

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setBatch(batch);
        eyes.setSendDom(false);

        driver = new ChromeDriver();

        //Los Angeles Hyatts
        driver.get("https://www.hyatt.com/search/Los%20Angeles%2C%20California%2C%20United%20States?location=Los%20Angeles%2C%20California%2C%20United%20States&checkinDate=2018-12-03&checkoutDate=2018-12-04&rooms=1&adults=1&kids=0&rate=Standard");

        //wait a few more seconds for everything to fully load. you can replace this sleep with webdriver wait until logic...
        TimeUnit.SECONDS.sleep(5);

        //Put all search rows in an array
        List<WebElement> results = driver.findElements(By.cssSelector("div.p-hotel-card"));

        for(int i=0; i<results.size(); i++){

            if ( results.get(i).getAttribute("innerHTML").contains("b-flag-new") ) {
                newBooking.add(results.get(i));
            } else {

                if (results.get(i).getAttribute("data-booking-status").equals("BOOKABLE")) {
                    bookable.add(results.get(i));
                }

                if (results.get(i).getAttribute("data-booking-status").equals("SOLD_OUT")) {
                    soldout.add(results.get(i));
                }

                if (results.get(i).getAttribute("data-booking-status").equals("NOT_BOOKABLE")) {
                    notBookable.add(results.get(i));
                }
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }

    //This test only runs once to generate your baselines.
    //Comment out this test after your baselines have been generated, you've set your layout and/or ignore regions and saved.
    @Test
    public void hyattBaselineCreation() throws Exception {

        eyes.setMatchLevel(MatchLevel.STRICT);
        //Capture the events Header page. This is optional and can be done on another test.
        eyes.open(driver, appName, "Search Header", new RectangleSize(width, height));
        eyes.check("Search Page Top Menu", Target.region(driver.findElement(By.cssSelector("div.ui-header.b-color_bg-carbon"))).fully());
        eyes.check("Search Page Header", Target.region(driver.findElement(By.className("search-page-header"))).fully());
        eyes.check("Search Page Sort", Target.region(driver.findElement(By.cssSelector("body > div.row.container > div > div > div.b-row.b-row_gutter-3 > div:nth-child(1) > div"))).fully());
        eyes.close(false);

        //Capture the events Footer page. This is optional and can be done on another test.
        eyes.open(driver, "Hyatt.com", "Search Footer", new RectangleSize(width, height));
        eyes.check("Brands Footer", Target.region(driver.findElement(By.className("b-brandbar"))).fully());
        eyes.check("Footer", Target.region(driver.findElement(By.className("ui-footer"))).fully());
        eyes.close(false);

        eyes.setMatchLevel(MatchLevel.LAYOUT2);

        if (!bookable.isEmpty()) {
            eyes.open(driver, appName, "Bookable Search Result", new RectangleSize(width, height));
            eyes.check("Bookable", Target.region(bookable.get(0)).fully());
            eyes.close(false);
        }

        if (!soldout.isEmpty()) {
            eyes.open(driver, appName, "Sold Out Search Result", new RectangleSize(width, height));
            eyes.check("Sold Out", Target.region(soldout.get(0)).fully());
            eyes.close(false);
        }

        if (!notBookable.isEmpty()) {
            eyes.open(driver, appName, "Not Bookable Search Result", new RectangleSize(width, height));
            eyes.check("Not Bookable", Target.region(notBookable.get(0)).fully());
            eyes.close(false);
        }

        if (!newBooking.isEmpty()) {
            eyes.open(driver, appName, "New Bookable Search Result", new RectangleSize(width, height));
            eyes.check("New Bookable ", Target.region(newBooking.get(0)).fully());
            eyes.close(false);
        }
    }

    //Before running this test make sure to comment out the hyattBaselineCreation test class.
    @Test
    public void HyattSearch() throws Exception {

        eyes.setMatchLevel(MatchLevel.STRICT);

        //Capture the events Header page. This is optional and can be done on another test.
        eyes.open(driver, appName, "Search Header", new RectangleSize(width, height));
        eyes.check("Search Page Top Menu", Target.region(driver.findElement(By.cssSelector("div.ui-header.b-color_bg-carbon"))).fully());
        eyes.check("Search Page Header", Target.region(driver.findElement(By.className("search-page-header"))).fully());
        eyes.check("Search Page Sort", Target.region(driver.findElement(By.cssSelector("body > div.row.container > div > div > div.b-row.b-row_gutter-3 > div:nth-child(1) > div"))).fully());
        eyes.close(false);

        //Capture the events Footer page. This is optional and can be done on another test.
        eyes.open(driver, appName, "Search Footer", new RectangleSize(width, height));
        eyes.check("Brands Footer", Target.region(driver.findElement(By.className("b-brandbar"))).fully());
        eyes.check("Footer", Target.region(driver.findElement(By.className("ui-footer"))).fully());
        eyes.close(false);

        eyes.setMatchLevel(MatchLevel.LAYOUT2);

        if (!bookable.isEmpty()) {
            System.out.println("Bookable Hotels Count: " + bookable.size());

            for (int i = 0; i < bookable.size(); i++) {
                //The eyes.open values must be exactly the same as your baseline values in the msgEventBaselineCreation test...
                eyes.open(driver, appName, "Bookable Search Result", new RectangleSize(width, height));
                eyes.check("Bookable " + i, Target.region(bookable.get(i)).fully());
                eyes.close(false);
            }
        }

        if (!soldout.isEmpty()) {
            System.out.println("Sold Out Hotels Count: " + soldout.size());

            for (int i = 0; i < soldout.size(); i++) {
                //The eyes.open values must be exactly the same as your baseline values in the msgEventBaselineCreation test...
                eyes.open(driver, appName, "Sold Out Search Result", new RectangleSize(width, height));
                eyes.check("Sold Out " + i, Target.region(soldout.get(i)).fully());
                eyes.close(false);
            }
        }

        if (!notBookable.isEmpty()) {
            System.out.println("Not Bookable Hotels Count: " + notBookable.size());

            for(int i=0; i<notBookable.size(); i++){
                //The eyes.open values must be exactly the same as your baseline values in the msgEventBaselineCreation test...
                eyes.open(driver, appName, "Not Bookable Search Result", new RectangleSize(width, height));
                eyes.check("Not Bookable " + i, Target.region(notBookable.get(i)).fully());
                eyes.close(false);
            }
        }

        if (!newBooking.isEmpty()) {
            System.out.println("New Bookable Hotels Count: " + newBooking.size());

            for(int i=0; i<newBooking.size(); i++){
                //The eyes.open values must be exactly the same as your baseline values in the msgEventBaselineCreation test...
                eyes.open(driver, appName, "New Bookable Search Result", new RectangleSize(width, height));
                eyes.check("New Bookable " + i, Target.region(newBooking.get(i)).fully());
                eyes.close(false);
            }
        }

    }

}