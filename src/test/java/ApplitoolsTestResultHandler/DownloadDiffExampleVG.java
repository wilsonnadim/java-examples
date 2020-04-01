package ApplitoolsTestResultHandler;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.image.BufferedImage;
import java.util.List;

public class DownloadDiffExampleVG {

    private static String downloadDir = "/Users/justin/repos/applitools/results";
    private static String viewKey = "YourReadViewKey";

    public static void main(String[] args) throws Exception {


        WebDriver driver = new ChromeDriver();
        EyesRunner runner = new VisualGridRunner(2);
        Eyes eyes = new Eyes(runner);
        eyes.setApiKey("YourAPIExecutionKey");
        eyes.setServerUrl("https://eyessapi.applitools.com");

        Configuration config = eyes.getConfiguration();
        config.setAppName("Hello World");
        config.setTestName("Visual Grid");
        config.addBrowser(1200, 800, BrowserType.FIREFOX);
        config.addBrowser(1200, 800, BrowserType.CHROME);
        eyes.setConfiguration(config);

        try {
            // Start visual testing with browser viewport set to 800x600.
            // Make sure to use the returned driver from this point on.
            eyes.open(driver);

            // Navigate the browser to the "hello world!" web-site.
            driver.get("https://applitools.com/helloworld");

            // Visual checkpoint #1.
            eyes.check("Hello!", Target.window());

            // Click the "Click me!" button.
            driver.findElement(By.tagName("button")).click();

            // Visual checkpoint #2.
            eyes.check("Click!", Target.window());

            // End Visual Test
            eyes.closeAsync();

        }

        finally {

            TestResultsSummary allTestResults = runner.getAllTestResults(false);
            TestResultContainer[] results = allTestResults.getAllResults();
            for(TestResultContainer result: results){
                TestResults test = result.getTestResults();

                ApplitoolsTestResultsHandler testResultHandler = new ApplitoolsTestResultsHandler(test, viewKey);

                List<BufferedImage>  base = testResultHandler.getBaselineBufferedImages(); // get Baseline Images as BufferedImage
                List<BufferedImage>  curr = testResultHandler.getCurrentBufferedImages();  // get Current Images as BufferedImage
                List<BufferedImage> diff = testResultHandler.getDiffsBufferedImages();     // get Diff Images as BufferedImage

                // Optional Setting this prefix will determine the structure of the repository for the downloaded images.
                testResultHandler.SetPathPrefixStructure("TestName/AppName/viewport/hostingOS/hostingApp");

                //Link to test/step result
                System.out.println("This is the url to the first step " + testResultHandler.getLinkToStep(1));

                testResultHandler.downloadImages(downloadDir);         // Download both the Baseline and the Current images to the folder specified in Path.
                testResultHandler.downloadBaselineImages(downloadDir); // Download only the Baseline images to the folder specified in Path.
                testResultHandler.downloadCurrentImages(downloadDir);  // Download only the Current images to the folder specified in Path.
                testResultHandler.downloadDiffs(downloadDir);          // Download Diffs to the folder specified in Path.
                testResultHandler.downloadAnimateGiff(downloadDir);    // Download Animated GIf to the folder specified in Path.
            }

            // Abort Session in case of an unexpected error.
            eyes.abortIfNotClosed();
            driver.close();
        }
    }
}