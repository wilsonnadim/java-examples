import com.applitools.eyes.RectangleSize;
import Pages.GuineaPigPage;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.UnexpectedException;
import java.util.UUID;

public class TestNGTextInputSauceLabs extends TestNGSauceLabsBase {

    /**
     * Runs a simple test verifying if the comment input is functional.
     * @throws InvalidElementStateException
     */
    @org.testng.annotations.Test(dataProvider = "hardCodedBrowsers")
    public void verifyCommentInputTest(String browser, String version, String os, String resolution, Method method)
            throws MalformedURLException, InvalidElementStateException, UnexpectedException {

        this.createDriver(browser, version, os, resolution, method.getName());
        WebDriver driver = this.getWebDriver();

        this.getEyes().open(driver, "TestNGTextInputSauceLabs", "GuineaPig Page", new RectangleSize(1050, 700));

        String commentInputText = UUID.randomUUID().toString();

        this.annotate("Visiting GuineaPig page...");
        GuineaPigPage page = GuineaPigPage.visitPage(driver);

        this.annotate(String.format("Submitting comment: \"%s\"", commentInputText));
        page.submitComment(commentInputText);

        this.annotate(String.format("Asserting submitted comment is: \"%s\"", commentInputText));
        Assert.assertTrue(page.getSubmittedCommentText().contains(commentInputText));

        this.getEyes().checkWindow("Full Page");
        this.getEyes().close();
    }

}