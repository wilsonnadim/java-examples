import com.applitools.eyes.RectangleSize;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.UnexpectedException;
import java.util.concurrent.TimeUnit;

public class TestNGCrossBrowserTests extends TestNGCrossBrowserBase {

    /**
     * Runs a simple test verifying if the comment input is functional.
     *
     * @throws InvalidElementStateException
     */
    @org.testng.annotations.Test(dataProvider = "hardCodedBrowsers")
    public void verifyApplitoolsSite(String browser, String version, String os, String resolution, Method method)
            throws MalformedURLException, InvalidElementStateException, UnexpectedException {

        this.createDriver(browser, version, os, resolution, method.getName());
        WebDriver driver = this.getWebDriver();

        try {
            TimeUnit.SECONDS.sleep(5);
        }
        catch(InterruptedException ex)  {
            System.out.println("Exception: " + ex);
        }

        this.getEyes().open(driver, "Applitools", "Applitools Pages",
                new RectangleSize(1050, 700));

        //Set your list of pages you want to validate in the array.
        String[] pages = {"/", "/pricing", "/customers"};

        for (String page : pages){

            System.out.println("Navigating to: https://applitools.com" + page);

            driver.get("https://applitools.com" + page);

            this.getEyes().checkWindow(page);
        }

        //throw exception when new baseline is created.
        this.getEyes().close(true);
    }
}