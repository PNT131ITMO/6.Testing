package lab3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    private static final int DEFAULT_TIMEOUT_SEC = 20;
    private static final int SHORT_TIMEOUT_SEC = 8;

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
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", el);
            el.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    protected boolean clickFirstPresentByXPath(String xpath) {
        List<WebElement> elements = driver.findElements(By.xpath(xpath));
        if (elements.isEmpty()) {
            return false;
        }

        for (WebElement el : elements) {
            try {
                String href = "";
                try {
                    href = el.getAttribute("href");
                } catch (Exception ignored) {
                }

                if (href != null && !href.isBlank() && !href.startsWith("javascript")) {
                    driver.get(href);
                    return true;
                }

                try {
                    ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({block: 'center'});", el);
                } catch (Exception ignored) {
                }

                try {
                    el.click();
                    return true;
                } catch (Exception ignored) {
                }

                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                return true;
            } catch (Exception ignored) {
            }
        }

        return false;
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

    protected boolean isPresentByXPath(String xpath) {
        try {
            return !driver.findElements(By.xpath(xpath)).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean waitForAnyVisible(String... xpaths) {
        try {
            return wait.until(d -> {
                for (String xpath : xpaths) {
                    List<WebElement> elements = d.findElements(By.xpath(xpath));
                    for (WebElement element : elements) {
                        try {
                            if (element.isDisplayed()) {
                                return true;
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
                return false;
            });
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean waitForUrlContains(String... fragments) {
        try {
            return wait.until(d -> {
                try {
                    String currentUrl = d.getCurrentUrl().toLowerCase(Locale.ROOT);
                    for (String fragment : fragments) {
                        if (currentUrl.contains(fragment.toLowerCase(Locale.ROOT))) {
                            return true;
                        }
                    }
                    return false;
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (Exception e) {
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

    protected boolean pageSourceContainsIgnoreCase(String text) {
        try {
            return driver.getPageSource().toLowerCase(Locale.ROOT)
                    .contains(text.toLowerCase(Locale.ROOT));
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean pageSourceContainsAny(String... texts) {
        try {
            String source = driver.getPageSource().toLowerCase(Locale.ROOT);
            for (String text : texts) {
                if (source.contains(text.toLowerCase(Locale.ROOT))) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean waitForPageSourceContainsAny(String... texts) {
        try {
            return wait.until(d -> {
                String source = d.getPageSource().toLowerCase(Locale.ROOT);
                for (String text : texts) {
                    if (source.contains(text.toLowerCase(Locale.ROOT))) {
                        return true;
                    }
                }
                return false;
            });
        } catch (TimeoutException e) {
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