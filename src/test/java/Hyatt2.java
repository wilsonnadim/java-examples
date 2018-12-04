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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(Parallell.class)
public class Hyatt2 {

    private Eyes eyes = new Eyes();
    private WebDriver driver;

    protected Integer width;
    protected Integer height;
    protected String appName = "Hyatt.com";

    private static BatchInfo batch;

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
        env.add(new Integer[]{454, 675});
        env.add(new Integer[]{630, 699});
        env.add(new Integer[]{860, 640});
        env.add(new Integer[]{1212, 666});

        return env;
    }

    public Hyatt2(Integer width, Integer height) {
        this.width = width;
        this.height = height;
    }

    @BeforeClass
    //Create a Batch Object ID in the before class to apply to all tests below.
    public static void batchInitialization(){
        batch = new BatchInfo("Hyatt.com Search Results 2");
    }

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setLogHandler(new StdoutLogHandler(false));
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setBatch(batch);

        eyes.setSendDom(false);

        driver = new ChromeDriver();

        //Los Angeles Hyatts
        //driver.get("https://www.hyatt.com/search/Los%20Angeles%2C%20California%2C%20United%20States?location=Los%20Angeles%2C%20California%2C%20United%20States&checkinDate=2018-12-03&checkoutDate=2018-12-04&rooms=1&adults=1&kids=0&rate=Standard");

        //Toronto
        driver.get("https://www.hyatt.com/search/Toronto%2C%20Canada?location=Toronto%2C%20Canada&checkinDate=2019-03-19&checkoutDate=2019-03-20&rooms=1&adults=1&kids=0&rate=Standard");

        //wait a few more seconds for everything to fully load. you can replace this sleep with webdriver wait until logic...
        TimeUnit.SECONDS.sleep(5);

        //remove the Feedback widget...
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            js.executeScript("document.querySelector('body > div.QSISlider.SI_3lNUVXW14HgqRBH_SliderContainer > div:nth-child(4) > div > img').setAttribute('style', 'display:none')");
        } catch (WebDriverException e) {
            System.out.println(e);
        }
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }

    @Test
    public void HyattSearchHeaderArea() throws Exception {
        eyes.addProperty("Region", "Header");
        List<WebElement> checkinDate = driver.findElements(By.cssSelector("li"));
        for(int i=0; i<checkinDate.size(); i++) {
            try {
                if (!checkinDate.get(i).getAttribute("data-target").equals("checkinDate")) {
                    checkinDate.remove(i);
                }
            } catch(NullPointerException ex) {
                System.out.println(ex);
            }
        }
        WebElement[] checkinDateArray = new WebElement[checkinDate.size()];
        checkinDate.toArray(checkinDateArray);

        //Capture the events Header page. This is optional and can be done on another test.
        eyes.open(driver, appName, "Search Header", new RectangleSize(width, height));
        eyes.check("Search Page Top Menu", Target.region(driver.findElement(By.cssSelector("div.ui-header.b-color_bg-carbon"))));
        eyes.check("Search Page Header", Target.region(driver.findElement(By.className("search-page-header"))).layout(checkinDateArray));
        eyes.check("Search Page Sort", Target.region(driver.findElement(By.cssSelector("body > div.row.container > div > div > div.b-row.b-row_gutter-3 > div:nth-child(1) > div"))));
        eyes.close(false);
    }

    @Test
    public void HyattSearchFooterArea() throws Exception {
        eyes.addProperty("Region", "Footer");
        //Capture the events Footer page. This is optional and can be done on another test.
        eyes.open(driver, appName, "Search Footer", new RectangleSize(width, height));
        eyes.check("Brands Footer", Target.region(driver.findElement(By.className("b-brandbar"))));
        eyes.check("Footer", Target.region(driver.findElement(By.className("ui-footer"))));
        eyes.close(false);
    }

    @Test
    public void HyattSearchResults() throws Exception {
        eyes.addProperty("Region", "Hotels");
        //Put all search rows in an array
        List<WebElement> results = driver.findElements(By.cssSelector("div.p-hotel-card"));

        List<WebElement> pricing = driver.findElements(By.cssSelector("div.hotel-pricing.b-text_align-right"));
        for(int i=0; i<pricing.size(); i++) {
            if (pricing.get(i).getLocation().getX() == 0) {
                pricing.remove(i);
            }
        }

        WebElement[] pricing_array = new WebElement[pricing.size()];
        pricing.toArray(pricing_array);

        List<WebElement> locations = driver.findElements(By.cssSelector("div.b-mt2"));
        for(int i=0; i<locations.size(); i++) {
            try {
                if (!locations.get(i).getAttribute("data-js").contentEquals("location")) {
                    locations.remove(i);
                }
            } catch(NullPointerException ex) {
                System.out.println(ex);
            }
        }

        WebElement[] locations_array = new WebElement[locations.size()];
        locations.toArray(locations_array);
        for(int i=0; i<results.size(); i++){
            String hotelId = results.get(i).getAttribute("data-spirit-code");
            eyes.open(driver, appName, "Hotel: " + hotelId, new RectangleSize(width, height));
            eyes.check("Hotel: " + hotelId, Target.region(results.get(i)).layout(pricing_array).layout(locations_array));
            eyes.close(false);
        }
    }

//    @Test
//    public void HyattSearch() throws Exception {
//
//        List<WebElement> checkinDate = driver.findElements(By.cssSelector("li"));
//        for(int i=0; i<checkinDate.size(); i++) {
//            try {
//                if (!checkinDate.get(i).getAttribute("data-target").equals("checkinDate")) {
//                    checkinDate.remove(i);
//                }
//            } catch(NullPointerException ex) {
//                System.out.println(ex);
//            }
//        }
//        WebElement[] checkinDateArray = new WebElement[checkinDate.size()];
//        checkinDate.toArray(checkinDateArray);
//
//        //Capture the events Header page. This is optional and can be done on another test.
//        eyes.open(driver, appName, "Search Header", new RectangleSize(width, height));
//        eyes.check("Search Page Top Menu", Target.region(driver.findElement(By.cssSelector("div.ui-header.b-color_bg-carbon"))));
//        eyes.check("Search Page Header", Target.region(driver.findElement(By.className("search-page-header"))).layout(checkinDateArray));
//        eyes.check("Search Page Sort", Target.region(driver.findElement(By.cssSelector("body > div.row.container > div > div > div.b-row.b-row_gutter-3 > div:nth-child(1) > div"))));
//        eyes.close(false);
//
//        //Capture the events Footer page. This is optional and can be done on another test.
//        eyes.open(driver, appName, "Search Footer", new RectangleSize(width, height));
//        eyes.check("Brands Footer", Target.region(driver.findElement(By.className("b-brandbar"))));
//        eyes.check("Footer", Target.region(driver.findElement(By.className("ui-footer"))));
//        eyes.close(false);
//
//        //Put all search rows in an array
//        List<WebElement> results = driver.findElements(By.cssSelector("div.p-hotel-card"));
//
//        List<WebElement> pricing = driver.findElements(By.cssSelector("div.hotel-pricing.b-text_align-right"));
//        for(int i=0; i<pricing.size(); i++) {
//            if (pricing.get(i).getLocation().getX() == 0) {
//                pricing.remove(i);
//            }
//        }
//
//        WebElement[] pricing_array = new WebElement[pricing.size()];
//        pricing.toArray(pricing_array);
//
//        List<WebElement> locations = driver.findElements(By.cssSelector("div.b-mt2"));
//        for(int i=0; i<locations.size(); i++) {
//            try {
//                if (!locations.get(i).getAttribute("data-js").contentEquals("location")) {
//                    locations.remove(i);
//                }
//            } catch(NullPointerException ex) {
//                System.out.println(ex);
//            }
//        }
//
//        WebElement[] locations_array = new WebElement[locations.size()];
//        locations.toArray(locations_array);
//        for(int i=0; i<results.size(); i++){
//            String hotelId = results.get(i).getAttribute("data-spirit-code");
//            eyes.open(driver, appName, "Hotel: " + hotelId, new RectangleSize(width, height));
//            eyes.check("Hotel: " + hotelId, Target.region(results.get(i)).layout(pricing_array).layout(locations_array));
//            eyes.close(false);
//        }
//    }

}