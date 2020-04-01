import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class kw {

    private static void ScrollToTop(WebDriver driver) {
        WebElement modal = driver.findElement(By.cssSelector("div.modal__content"));
        System.out.println("\nScolling to top of modal....\n");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollTo(0, 0);", modal);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        System.out.println("\nScrolled to top of modal....\n");
    };

    public static void main(String[] args) {

        // Open a Chrome browser.
        WebDriver driver = new ChromeDriver();
        //WebDriver driver = new FirefoxDriver();

        // Initialize the eyes SDK and set your private API key.
        Eyes eyes = new Eyes();
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setHideScrollbars(false);
        eyes.setWaitBeforeScreenshots(3000);
        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        try{
             //Username: kelle123412345
            //Password: w2EgJsFy
            //https://console-qa.command.kw.com/command/opportunities/details?contactId=511480&id=161959&teamId=
            // Navigate the browser
            driver.get("https://console-qa.command.kw.com/command/contacts/3792604");

            //Login an go to contacts page...
            //Your Code HERE

            // Start the test
            eyes.open(driver, "kw.com", "modal", new RectangleSize(1200, 800));

            //Open the Edit Contact modal popup
            //Your Code HERE

            //Click the Add More Information button...
            //Your Code HERE

            //Get the dropdown elements
            List<WebElement> dropdowns = driver.findElements(By.className("txt-h4"));

            //click the Additional Contact Information Section
            dropdowns.get(0).click();
            ScrollToTop(driver);
            eyes.check("Additional Contact Information!", Target.region(By.cssSelector("div.modal__content")).fully(true));

            //click the About Section
            dropdowns.get(1).click();
            ScrollToTop(driver);
            eyes.check("About", Target.region(By.cssSelector("div.modal__content")).fully(true));

            //click the Sales Pipeline Section
            dropdowns.get(2).click();
            ScrollToTop(driver);
            eyes.check("Sales Pipeline", Target.region(By.cssSelector("div.modal__content")).fully(true));

            //click the Custom Section
            dropdowns.get(3).click();
            ScrollToTop(driver);
            eyes.check("Custom", Target.region(By.cssSelector("div.modal__content")).fully(true));

            // End the test.
            TestResults results = eyes.close(false);
            //assertEquals(true, results.isPassed());

        } finally {

            // Close the browser.
            driver.quit();

            // If the test was aborted before eyes.close was called, ends the test as aborted.
            eyes.abortIfNotClosed();
        }

    }

}