package lab3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    private static final int DEFAULT_TIMEOUT_SEC = 15;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SEC));    
    }

    protected WebElement waitForXPath(String xpath) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    protected void clickByXPath(String xpath) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        el.click();
    }

    protected void typeByXPath(String xpath, String text) {
        WebElement el = waitForXPath(xpath);
        el.clear();
        el.sendKeys(text);
    }

    protected void scrollAndClick(String xpath){
        WebElement el = waitForXPath(xpath);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",el);
        el.click();
    }

    protected String getTextByXPath(String xpath) {
        return waitForXPath(xpath).getText().trim();
    }

    protected boolean isVisibleByXPath(String xpath) {
        try {
            return driver.findElement(By.xpath(xpath)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
