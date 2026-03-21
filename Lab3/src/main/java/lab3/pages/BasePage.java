package lab3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    private static final int DEFAULT_TIMEOUT_SEC = 15;
    private static final int SHORT_TIMEOUT_SEC = 4;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SEC));    
    }

    protected WebElement waitForXPath(String xpath) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    protected void clickByXPath(String xpath) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        try {
            el.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    protected void typeByXPath(String xpath, String text) {
        WebElement el = waitForXPath(xpath);
        el.clear();
        el.sendKeys(text);
    }

    protected void scrollAndClick(String xpath) {
        WebElement el = waitForXPath(xpath);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", el);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    protected String getTextByXPath(String xpath) {
        return waitForXPath(xpath).getText().trim();
    }

    protected boolean isVisibleByXPath(String xpath) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_TIMEOUT_SEC));
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isClickableByXPath(String xpath) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_TIMEOUT_SEC));
            return shortWait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean waitForUrlContains(String... fragments) {
        try {
            return wait.until(driver -> {
                String currentUrl = driver.getCurrentUrl().toLowerCase();
                for (String fragment : fragments) {
                    if (currentUrl.contains(fragment.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            });
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean pageSourceContains(String text) {
        try {
            return driver.getPageSource().contains(text);
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
