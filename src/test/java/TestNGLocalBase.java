import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.UnexpectedException;

public class TestNGLocalBase {

    public String buildTag = System.getenv("BUILD_TAG");

    public String username = System.getenv("SAUCE_USERNAME");

    public String accesskey = System.getenv("SAUCE_ACCESS_KEY");


    private ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
    public WebDriver getWebDriver() {
        return webDriver.get();
    }

    private ThreadLocal<Eyes> myEyes = new ThreadLocal<Eyes>();
    public Eyes getEyes() { return myEyes.get(); }

    @DataProvider(name = "hardCodedViewPorts", parallel = true)
    public static Object[][] viewportDataProvider(Method testMethod) {
        return new Object[][]{
                new Object[]{454,  675},
                new Object[]{630,  699},
                new Object[]{860,  640},
                new Object[]{1212, 666},

        };
    }

    protected void createDriver(Integer width, Integer height, String methodName)
            throws MalformedURLException, UnexpectedException {

        webDriver.set(new ChromeDriver());

        myEyes.set(new Eyes());
        getEyes().setApiKey(System.getenv("APPLITOOLS_KEY"));
        getEyes().setHideScrollbars(true);
        getEyes().setForceFullPageScreenshot(true);
        getEyes().setStitchMode(StitchMode.CSS);

    }

    @AfterMethod
    public void tearDown(ITestResult result) throws Exception {
        webDriver.get().quit();
    }

    protected void annotate(String text) {
        ((JavascriptExecutor) webDriver.get()).executeScript(text);
    }
}
