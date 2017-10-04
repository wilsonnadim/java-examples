/**
 * Created by justin on 7/19/17.
 */

import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.images.Eyes;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ImagesSDKExample {

    private Eyes eyes = new Eyes();
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey("your applitools key");
        eyes.setMatchLevel(MatchLevel.LAYOUT2);
        eyes.setHostOS("OSX 10.12");
        eyes.setHostApp("Chrome 59.0");
        driver = new ChromeDriver();
        driver.get("https://www.github.com");
    }

    @Test
    public void GithubHomePage() throws Exception {
        eyes.open("Github", "Github Home Page", new RectangleSize(800, 600));

        File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String fileName = "github-homepage.png";

        try {
            FileUtils.copyFile(src, new File(fileName));
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());

        }

        BufferedImage img;
        try {
            img = ImageIO.read(new File(fileName));
            eyes.checkImage(img, "Github Home Page");
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());

        }

        TestResults results = eyes.close(false);
        assertEquals(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }

}
