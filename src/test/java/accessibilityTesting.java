import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;

public class accessibilityTesting {
    private static String WAVE_ACCESSIBILITY = "http://wave.webaim.org/report#/%s";

    public static void main(String[] args) throws InterruptedException {
        EyesRunner runner = new ClassicRunner();
        Eyes eyes = new Eyes(runner);
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        WebDriver driver = new ChromeDriver();
        eyes.setForceFullPageScreenshot(true);

        try {

            eyes.open(driver, "Familysearch.org", "Accessibility Testing", new RectangleSize(1500, 1200));
            validateAccessibility(driver, eyes, "familysearch.org");
            eyes.closeAsync();

        } finally {

            TestResultsSummary allTestResults = runner.getAllTestResults(false);

            TestResultContainer[] results = allTestResults.getAllResults();
            for(TestResultContainer result: results){
                TestResults test = result.getTestResults();

                assertEquals(test.getName() + " has mismatches", 0, test.getMismatches());
            }

            eyes.abortIfNotClosed();
            driver.close();

        }

    }

    private static void validateAccessibility(WebDriver driver, Eyes eyes, String site) throws InterruptedException {
        driver.get(String.format(WAVE_ACCESSIBILITY, site));
        Thread.sleep(5000);
        eyes.check("Accessibility", Target.window());
    }
}