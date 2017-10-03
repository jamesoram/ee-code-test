package io.equalexperts.hotel_test.misc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;

public class Wait extends WebDriverWait {

    public Wait(WebDriver driver, long timeOutInSeconds) {
        super(driver, timeOutInSeconds);
    }

    public void forElementAdded(final String cssLocator, final int originalCount) {
        until(new ExpectedCondition<Boolean>() {
            @Nullable
            public Boolean apply(@Nullable WebDriver driver) {
                return driver.findElements(By.cssSelector(cssLocator)).size() == originalCount + 1;
            }
        });
    }

    public void forElementRemoved(final String cssLocator, final int originalCount) {
        forElementAdded(cssLocator, -originalCount);
    }
}
