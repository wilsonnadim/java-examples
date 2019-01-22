package com.automatedtest.sample.searchresultpage;

import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResults;
import com.automatedtest.sample.basepage.BasePage;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

public class SearchResultPage extends BasePage {

    private static final String RESULTS_URL_SELECTOR = "cite";

    @FindBy(css = RESULTS_URL_SELECTOR)
    private List<WebElement> results;

    SearchResultPage() {
        PageFactory.initElements(driver, this);
    }

    void checkExpectedUrlInResults(String expectedUrl, int nbOfResultsToSearch) {
        wait.forPresenceOfElements(5, By.cssSelector(RESULTS_URL_SELECTOR), "Result url");
        Integer indexOfLink = IntStream.range(0, Math.min(this.results.size(), nbOfResultsToSearch))
                .filter(index -> expectedUrl.equals(this.results.get(index).getText()))
                .findFirst()
                .orElse(-1);
        Assert.assertTrue("Url " + expectedUrl + " wasn't found in the results.",
                !indexOfLink.equals(-1));
    }

    private void openEyes(String testName){
        eyes.open(driver, "Google.com", testName, viewport);
    }

    private void eyesClose(){
        try {
            TestResults results = this.eyes.close(false);
            assertTrue(results.isPassed());
        } finally {
            this.eyes.abortIfNotClosed();
        }
    }

    void eyesCheck(String testName){
        openEyes(testName);
        eyes.checkWindow(testName);
        eyesClose();
    }
}

