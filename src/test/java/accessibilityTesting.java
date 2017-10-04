import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class accessibilityTesting {
    private static String WAVE_ACCESSIBILITY = "http://wave.webaim.org/report#/%s";

    public static void main(String[] args) throws InterruptedException {
        Eyes eyes = new Eyes();
        eyes.setApiKey("your-applitools-key");
        WebDriver driver = new ChromeDriver();
        eyes.setMatchLevel(MatchLevel.LAYOUT2);

        try {
            eyes.open(driver, "Familysearch.org", "Accessibility Testing", new RectangleSize(1500, 1200));
            validateAccessibility(driver, eyes, "familysearch.org");
            eyes.close();
        } finally {
            eyes.abortIfNotClosed();
            driver.close();
        }
    }

    private static void validateAccessibility(WebDriver driver, Eyes eyes, String site) throws InterruptedException {
        driver.get(String.format(WAVE_ACCESSIBILITY, site));
        Thread.sleep(5000);
        eyes.checkWindow("Accessibility");
    }
}