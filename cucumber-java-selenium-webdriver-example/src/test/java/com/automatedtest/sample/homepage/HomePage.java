package com.automatedtest.sample.homepage;

import com.applitools.eyes.TestResults;

import com.automatedtest.sample.basepage.BasePage;
import cucumber.api.java.Before;
import cucumber.api.junit.Cucumber;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HomePage extends BasePage{

    private static final String HOME_PAGE_URL = "https://www.google.";

    @FindBy(css = "#hplogo")
    private WebElement logo;

    @FindBy(css = "input[name=q]")
    private WebElement searchInput;

    HomePage() {
        PageFactory.initElements(driver, this);
    }

    void goToHomePage(String country){
        driver.get(HOME_PAGE_URL + country);
        wait.forLoading(5);
    }

    void openEyes(String testName){
        eyes.open(driver, "Google.com", testName, viewport);
    }

    void eyesCheck(String testName){
        openEyes(testName);
        eyes.checkWindow(testName);
        eyesClose();
    }

    void eyesClose(){
        try {
            TestResults results = this.eyes.close(false);
            assertTrue(results.isPassed());
        } finally {
            this.eyes.abortIfNotClosed();
        }
    }

    void clickLink(String selector){
        driver.findElement(By.cssSelector(selector)).click();
    }

    void checkLogoDisplay() {
        wait.forElementToBeDisplayed(5, this.logo, "Logo");
    }

    void checkTitle(String title) {
        String displayedTitle = driver.getTitle();
        Assert.assertTrue("Displayed title is " + displayedTitle + " instead of " + title,
                title.equals(displayedTitle));
    }

    void checkSearchBarDisplay() {
        wait.forElementToBeDisplayed(10, this.searchInput, "Search Bar");
    }

    void searchFor(String searchValue) {
        this.searchInput.sendKeys(searchValue);
        this.searchInput.sendKeys(Keys.ENTER);
    }
}
