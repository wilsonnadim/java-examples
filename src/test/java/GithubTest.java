//package com.example.applitools;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Arrays;
import java.util.Collection;


interface UIStateChangeable{
    public void FireUIStateChanged(String tag);
}

@RunWith(Parameterized.class)
public class GithubTest extends TestCase implements UIStateChangeable
{
    private static BatchInfo batchInfo;
    private WebDriver driver;
    private Eyes eyes;
    private int width;

    @Parameters
    public static Collection<Integer> data() {
        return Arrays.asList(new Integer[] { 1050, 800, 400});
    }

    public GithubTest(Integer width) {
        this.width = width;
    }

    @BeforeClass
    public static void createBatch(){
        batchInfo = new BatchInfo("Responsive");
    }

    @Before
    public void setUp() throws Exception {
        initializeEyes();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver();
        driver = eyes.open(driver, "GitHub", "Pages", new RectangleSize(this.width, 600));
    }

    public void initializeEyes() {

        eyes = new Eyes();
        String apiKey = "Your_API_KEY";

        eyes.setApiKey(apiKey);

        eyes.setBatch(batchInfo);
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
    }

    @After
    public void tearDown() throws Exception {
        eyes.close();
        driver.quit();
    }

    @Test
    public void testResponsiveness() {
        driver.get("https://github.com");

        HomePage homePage = new HomePage(driver, this);
        eyes.checkWindow(homePage.getName());
        FeaturesPage featuresPage = homePage.goToFeaturesPage();
        eyes.checkWindow(featuresPage.getName());
        ExplorePage explorePage = featuresPage.goToExplorePage();
        eyes.checkWindow(explorePage.getName());
    }


    public abstract class BasePage {

        protected WebDriver driver;
        private UIStateChangeable uiStateChangedHandler;

        protected final By navMenuLocator = By.className("octicon-three-bars");
        protected final By featuresLocator = By.cssSelector("a[href='/features']");
        protected final By exploreLocator = By.cssSelector("a[href='/explore']");

        private String name;


        public BasePage(String pageName, WebDriver driver, UIStateChangeable uiStateChangedHandler) {
            this.driver = driver;
            this.name = pageName;
            this.uiStateChangedHandler = uiStateChangedHandler;
        }

        protected void clickNavButton(By locator) {
            WebElement navMenu = driver.findElement(navMenuLocator);
            if (navMenu.isDisplayed()) {
                navMenu.click();
                this.uiStateChangedHandler.FireUIStateChanged("Navigation Menu");
            }

            driver.findElement(locator).click();
        }

        public String getName()
        {
            return this.name;
        }

        public FeaturesPage goToFeaturesPage() {
            clickNavButton(featuresLocator);
            return new FeaturesPage(this.driver, this.uiStateChangedHandler);
        }

        public ExplorePage goToExplorePage() {
            clickNavButton(exploreLocator);

            return new ExplorePage(this.driver, this.uiStateChangedHandler);
        }
    }

    public class HomePage extends BasePage {

        public HomePage(WebDriver driver, UIStateChangeable uiStateChangedHandler) {
            super("Home", driver, uiStateChangedHandler);
        }
    }

    public class FeaturesPage extends BasePage {

        public FeaturesPage(WebDriver driver, UIStateChangeable uiStateChangedHandler) {
            super("Features", driver, uiStateChangedHandler);
        }
    }

    public class ExplorePage extends BasePage {

        public ExplorePage(WebDriver driver, UIStateChangeable uiStateChangedHandler) {
            super("Explore", driver, uiStateChangedHandler);
        }
    }

    public void FireUIStateChanged(String tag) {
        eyes.checkWindow(tag);
    }
}