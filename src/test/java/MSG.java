import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MSG {

    private Eyes eyes = new Eyes();
    private WebDriver driver;

    private void loadPage(int heightSize, int suspensionDuration) throws InterruptedException {
        RectangleSize viewport = eyes.getViewportSize(driver);
        for (int j = 0; j < heightSize; j += viewport.getHeight() - 20) {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + j + ")");
            Thread.sleep(suspensionDuration);
        }
    }

    private int getEntireHeight() {
        JavascriptExecutor js =(JavascriptExecutor)driver;
        Integer clientHeight = Integer.parseInt(js.executeScript("return document.documentElement.clientHeight").toString());
        Integer bodyClientHeight = Integer.parseInt(js.executeScript("return document.body.clientHeight").toString());
        Integer scrollHeight = Integer.parseInt(js.executeScript("return document.documentElement.scrollHeight").toString());
        Integer bodyScrollHeight = Integer.parseInt(js.executeScript("return document.body.scrollHeight").toString());
        Integer maxDocElementHeight = Math.max(clientHeight, scrollHeight);
        Integer maxBodyHeight = Math.max(bodyClientHeight, bodyScrollHeight);
        return Math.max(maxDocElementHeight, maxBodyHeight);
    }

    private void removeDynamicContent() {
        //Clean up the page by removing the dynamic ads. This is optional for this test but for fullpage tests it's highly recommended.
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, 0);");
            js.executeScript("document.getElementById('div-id-for-top-leaderboard').setAttribute('style', 'display:none')");
            js.executeScript("document.querySelector('div._3Qg81').setAttribute('style', 'display:none')");

            List<WebElement> ads=driver.findElements(By.className("ad-container"));
            for(int i=0; i<ads.size(); i++){
                js.executeScript("arguments[0].setAttribute('style', 'display:none')", ads.get(i));
            }
        } catch (WebDriverException e) {
            System.out.println(e);
        }
    }

    protected List<WebElement> datedEvents = new ArrayList<WebElement>();
    protected List<WebElement> nonDatedEvents = new ArrayList<WebElement>();
    protected List<WebElement> featuredEvents = new ArrayList<WebElement>();

    //this is your viewport setting. You can modify these to test responsive design. Ideally run in parallel with a linked list array.
    //Keep in-mind adjusting these values will create a new baseline...
    protected Integer width = 1250;
    protected Integer height = 800;

    private static BatchInfo batch;

    @BeforeClass
    //Create a Batch Object ID in the before class to apply to all tests below.
    public static void batchInitialization(){
        batch = new BatchInfo("Madison Square Garden Events");
    }

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setBatch(batch);
        eyes.setMatchTimeout(5000);

        driver = new ChromeDriver();

        //all events
        //driver.get("https://www.msg.com/calendar?happening=9%2F12%2F2018-7%2F31%2F2021");

        //this week
        //driver.get("https://www.msg.com/calendar?happening=09%2F12%2F2018-09%2F16%2F2018");

        //this month
        //driver.get("https://www.msg.com/calendar?happening=09%2F12%2F2018-09%2F30%2F2018");

        //next month
        driver.get("https://www.msg.com/calendar?happening=10%2F01%2F2018-10%2F31%2F2018");

        //wait a few more seconds for everything to fully load. you can replace this sleep with webdriver wait until logic...
        TimeUnit.SECONDS.sleep(5);

        //Get the event count from page
        String upcomingEvents = driver.findElement(By.cssSelector("h1._1b9Lj.headline")).getText();
        String[] splitString = upcomingEvents.split("\\s+");
        int eventCount = Integer.parseInt(splitString[0]);
        System.out.println("Upcoming Events: " + eventCount);

        //msg ONLY loads 407 events total even though the upcoming events count says 584 when doing a date range + 2 years...
        if (eventCount > 407) {
            System.out.println("Event Count > 407. Setting eventCount == 400");
            eventCount = 400;
        }

        //Lazy load page until event rows match event count listed on the page... Unless it's over 407 rows...
        while((driver.findElements(By.className("_3LcCX")).size() + driver.findElements(By.className("_3mYdI")).size()) < eventCount){
            try {
                Integer height = getEntireHeight();
                loadPage(height, 60);
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Current Feature Event Count: " + driver.findElements(By.className("_3mYdI")).size());
                System.out.println("Current Event Count: " + driver.findElements(By.className("_3LcCX")).size());
            } catch(InterruptedException ex) {
                System.out.println(ex);
            }

        }

        removeDynamicContent();

        //Put all event rows in an array
        List<WebElement> events = driver.findElements(By.tagName("article"));
        events.remove(events.size() - 1); //Remove the last object in array which is in the footer...

        for(int i=0; i<events.size(); i++){
            //Get only rows with dates on left side
            if ( events.get(i).getAttribute("innerHTML").contains("date-box") ) {
                datedEvents.add(events.get(i));
            } else {
                //Get all other rows without dates on left side
                nonDatedEvents.add(events.get(i));
            }
        }

        //Put all featured event rows in an array
        featuredEvents = driver.findElements(By.className("_3mYdI"));
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }

    //This test only runs once to generate your baselines for dated rows, non-dated rows and featured events.
    //Comment out this test after your baselines have been generated, you've set your layout and/or ignore regions and saved.
//    @Test
//    public void msgEventBaselineCreation() throws Exception {
//        eyes.open(driver, "Madison Square Garden Events", "Dated Events", new RectangleSize(width, height));
//        removeDynamicContent();
//        //get the first event with a date on the left side as the baseline
//        eyes.check("Dated Event", Target.region(datedEvents.get(0)).fully());
//        eyes.close(false);
//
//        eyes.open(driver, "Madison Square Garden Events", "Non-Dated Events", new RectangleSize(width, height));
//        removeDynamicContent();
//        //get the first event without a date on the left side as the baseline
//        eyes.check("Non-Dated Event", Target.region(nonDatedEvents.get(0)).fully());
//        eyes.close(false);
//
//        eyes.open(driver, "Madison Square Garden Events", "Featured Events", new RectangleSize(width, height));
//        removeDynamicContent();
//        //get the first featured event as the baseline
//        eyes.check("Featured Event", Target.region(featuredEvents.get(0)).fully());
//        eyes.close(false);
//
//        //Capture the events Header page. This is optional and can be done on another test.
//        eyes.open(driver, "Madison Square Garden Events", "Events Header", new RectangleSize(width, height));
//        removeDynamicContent();
//        eyes.check("Events Header", Target.region(driver.findElement(By.className("_3tRXX"))).fully());
//        eyes.check("Events Header Events Count", Target.region(driver.findElement(By.className("_3yiCj"))).fully());
//        eyes.close(false);
//
//        //Capture the events Footer page. This is optional and can be done on another test.
//        eyes.open(driver, "Madison Square Garden Events", "Events Footer", new RectangleSize(width, height));
//        removeDynamicContent();
//        eyes.check("Events Footer Social Media", Target.region(driver.findElement(By.className("_2cQHF"))).fully());
//        eyes.check("Events Footer", Target.region(driver.findElement(By.className("_1JUDs"))).fully());
//        eyes.close(false);
//    }

    @Test
    public void msgEventsPage() throws Exception {

        System.out.println("Dated Events Count: " + datedEvents.size());

        for(int i=0; i<datedEvents.size(); i++){
            //The eyes.open values must be exactly the same as your baseline values in the msgEventBaselineCreation test...
            eyes.open(driver, "Madison Square Garden Events", "Dated Events", new RectangleSize(width, height));
            //removeDynamicContent();
            eyes.check("Dated Event " + i, Target.region(datedEvents.get(i)).fully());
            eyes.close(false);
        }

        System.out.println("Non-Dated Events Count: " + nonDatedEvents.size());

        for(int i=0; i<nonDatedEvents.size(); i++){
            //The eyes.open values must be exactly the same as your baseline values in the msgEventBaselineCreation test...
            eyes.open(driver, "Madison Square Garden Events", "Non-Dated Events", new RectangleSize(width, height));
            //removeDynamicContent();
            eyes.check("Non-Dated Event " + i, Target.region(nonDatedEvents.get(i)).fully());
            eyes.close(false);
        }

        System.out.println("Featured Events Count: " + featuredEvents.size());

        for(int i=0; i<featuredEvents.size(); i++){
            //The eyes.open values must be exactly the same as your baseline values in the msgEventBaselineCreation test...
            eyes.open(driver, "Madison Square Garden Events", "Featured Events", new RectangleSize(width, height));
            //removeDynamicContent();
            eyes.check("Featured Event " + i, Target.region(featuredEvents.get(i)).fully());
            eyes.close(false);
        }

        //Capture the events Header page. This is optional and can be done on another test.
        eyes.open(driver, "Madison Square Garden Events", "Events Header", new RectangleSize(width, height));
        //removeDynamicContent();
        eyes.check("Events Header", Target.region(driver.findElement(By.className("_3tRXX"))).fully());
        eyes.check("Events Header Events Count", Target.region(driver.findElement(By.className("_3yiCj"))).fully());
        eyes.close(false);

        //Capture the events Footer page. This is optional and can be done on another test.
        eyes.open(driver, "Madison Square Garden Events", "Events Footer", new RectangleSize(width, height));
        //removeDynamicContent();
        eyes.check("Events Footer Social Media", Target.region(driver.findElement(By.className("_2cQHF"))).fully());
        eyes.check("Events Footer", Target.region(driver.findElement(By.className("_1JUDs"))).fully());
        eyes.close(false);
    }

}