import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResults;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.UnexpectedException;

public class TestNGLocalResponsive extends TestNGLocalBase {

    @org.testng.annotations.Test(dataProvider = "hardCodedViewPorts")
    public void verifyResponsiveDesign(Integer width, Integer height, Method method)
            throws MalformedURLException, InvalidElementStateException, UnexpectedException {

        this.createDriver(width, height, method.getName());
        WebDriver driver = this.getWebDriver();

        this.getEyes().open(driver, "Github", "Github Home Page", new RectangleSize(width, height));

        driver.get("http://www.github.com");

        this.getEyes().checkWindow("Full Page");

        TestResults results = this.getEyes().close(false);
        Assert.assertTrue(results.isPassed());
    }

}