package com.automatedtest.sample.searchresultpage;

import cucumber.api.java.en.Then;

public class SearchResultPageSteps {

    private SearchResultPage searchResultPage;

    public SearchResultPageSteps() {
        this.searchResultPage = new SearchResultPage();
    }

    @Then("^\"([^\"]*)\" is displayed in the first \"([^\"]*)\" results$")
    public void isDisplayedInTheFirstResults(String expectedResultUrl, int nbOfResultsToSearch) {
        this.searchResultPage.checkExpectedUrlInResults(expectedResultUrl, nbOfResultsToSearch);
    }

    @Then("^I visually check my page \"([^\"]*)\"$")
    public void openCheck(String tag) {
        this.searchResultPage.eyesCheck(tag);
    }
}
