package myprojects.automation.testng.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

/**
 * 11.10.2017
 * 20:04
 * Created by Roxy
 */
public class EventHandler implements WebDriverEventListener {
    public void beforeAlertAccept(WebDriver webDriver) {

    }

    public void afterAlertAccept(WebDriver webDriver) {

    }

    public void afterAlertDismiss(WebDriver webDriver) {
        System.out.println("Alert was dismissed");
    }

    public void beforeAlertDismiss(WebDriver webDriver) {
        System.out.println("Alert was shown");
    }

    public void beforeNavigateTo(String s, WebDriver webDriver) {

    }

    public void afterNavigateTo(String s, WebDriver webDriver) {
        System.out.println("Open url: " + s);
    }

    public void beforeNavigateBack(WebDriver webDriver) {

    }

    public void afterNavigateBack(WebDriver webDriver) {

    }

    public void beforeNavigateForward(WebDriver webDriver) {

    }

    public void afterNavigateForward(WebDriver webDriver) {

    }

    public void beforeNavigateRefresh(WebDriver webDriver) {

    }

    public void afterNavigateRefresh(WebDriver webDriver) {

    }

    public void beforeFindBy(By by, WebElement webElement, WebDriver webDriver) {
        System.out.println("Search for element: " + by.toString());
    }

    public void afterFindBy(By by, WebElement webElement, WebDriver webDriver) {
        System.out.println("Element found successfully");
    }

    public void beforeClickOn(WebElement webElement, WebDriver webDriver) {
        System.out.println("Click on element: " + webElement.getTagName() + " " + webElement.getAttribute("name"));
    }

    public void afterClickOn(WebElement webElement, WebDriver webDriver) {
        System.out.println("Element successfully clicked");
    }

    public void beforeChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {
        System.out.println("Fill input " + webElement.getAttribute("id") + " with value " + charSequences.toString());
    }

    public void afterChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {
        System.out.println("Value successfully changed");
    }

    public void beforeScript(String s, WebDriver webDriver) {
        System.out.println("Script will start");
    }

    public void afterScript(String s, WebDriver webDriver) {
        System.out.println("Script ended");
    }

    public void onException(Throwable throwable, WebDriver webDriver) {
        System.out.println("Exception thrown: " + throwable.getMessage());
    }
}
