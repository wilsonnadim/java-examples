import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.TestResults;
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

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CreditCards {

    private Eyes eyes = new Eyes();
    private WebDriver driver;

    //Keep in-mind adjusting these values will create a new baseline...
    protected Integer width = 1200;
    protected Integer height = 1100;

    private static BatchInfo batch;

    @BeforeClass
    //Create a Batch Object ID in the before class to apply to all tests below.
    public static void batchInitialization(){
        batch = new BatchInfo("Creditcards.com");
    }

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setBatch(batch);
        eyes.setMatchTimeout(5000);

        driver = new ChromeDriver();
        driver.get("https://www.creditcards.com/student/");

        //Expand all card details...
        driver.findElement(By.cssSelector("h3.product__highlights-title.bcc")).click(); //expand featured details
        List<WebElement> cardDetails = driver.findElements(By.className("product__show-more"));
        for(int i=0; i<cardDetails.size(); i++){
            cardDetails.get(i).click();
        }
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }

    @Test
    public void studentCreditCards() throws Exception {

        WebElement featuredCard = driver.findElement(By.cssSelector("div.product-box.product-box--featured.product-box--masked--featured"));
        String featuredId = featuredCard.getAttribute("data-product-id");

        eyes.open(driver, "Creditcards.com", "Card: " + featuredId, new RectangleSize(width, height));

        eyes.check("Card: " + featuredId, Target.region(featuredCard).fully());

        TestResults featuredResults = eyes.close(false);
        assertEquals(true, featuredResults.isPassed());

        //Put all remaining non-featured cards in an array
        List<WebElement> cards = driver.findElements(By.className("product-box"));
        cards.remove(0); //Remove the first object in array which is in the featured card...

        for(int i=0; i<cards.size(); i++){
            String cardId = cards.get(i).getAttribute("data-product-id");
            eyes.open(driver, "Creditcards.com", "Card: " + cardId, new RectangleSize(width, height));
            eyes.check("Card: " + cardId, Target.region(cards.get(i)).fully());

            TestResults results = eyes.close(false);
            assertEquals(true, results.isPassed());
        }
    }
}