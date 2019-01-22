package com.automatedtest.sample.basepage;

import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.selenium.StitchMode;

import com.automatedtest.sample.infrastructure.driver.Setup;
import com.automatedtest.sample.infrastructure.driver.Wait;
import org.openqa.selenium.WebDriver;

public class BasePage {

    protected WebDriver driver;
    protected Eyes eyes;
    protected Wait wait;
    protected RectangleSize viewport;

    public BasePage() {
        this.driver = Setup.driver;
        this.eyes = Setup.eyes;
        this.viewport = Setup.viewport;
        this.wait = new Wait(this.driver);
    }
}