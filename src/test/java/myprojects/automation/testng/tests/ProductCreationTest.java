package myprojects.automation.testng.tests;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * 07.10.2017
 * 14:38
 * Created by Roxy
 */
public class ProductCreationTest {
    WebDriver driver;
    EventFiringWebDriver wrappedDriver;
    Random rand = new Random();
    String randName;
    int randomQuantity;
    double randomPrice;
    SoftAssert softAssertion= new SoftAssert();
    List<String> products = new ArrayList<String>();

    /*@Parameters("browser")
    @BeforeClass
    public void setup(String browser) {
        if (browser.equalsIgnoreCase("firefox")) {
            System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver.exe");
            driver = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("ie")) {
            System.setProperty("webdriver.ie.driver", "src/test/resources/IEDriverServer.exe");
            driver = new InternetExplorerDriver();
        } else if (browser.equalsIgnoreCase("chrome")){
            System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
            driver = new ChromeDriver();
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wrappedDriver = new EventFiringWebDriver(driver);
        wrappedDriver.register(new EventHandler());
    }*/

    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wrappedDriver = new EventFiringWebDriver(driver);
        wrappedDriver.register(new EventHandler());
    }

    @AfterClass
    public void afterClass() {
        driver.quit();
    }

    @DataProvider
    public Object[][] getLoginData() {
        return new String[][] {
                {"webinar.test@gmail.com", "Xcg7299bnSmMuRLp9ITw"},
        };
    }

    @Test (dataProvider = "getLoginData")
    public void loginTest(String email, String password) {
        wrappedDriver.navigate().to("http://prestashop-automation.qatestlab.com.ua/admin147ajyvk0/");
        wrappedDriver.findElement(By.id("email")).sendKeys(email);
        wrappedDriver.findElement(By.id("passwd")).sendKeys(password);
        wrappedDriver.findElement(By.name("submitLogin")).click();

        WebDriverWait wait = new WebDriverWait(wrappedDriver, 5);
        wait.until(ExpectedConditions.titleContains("Dashboard"));
    }

    @Test(dependsOnMethods = "loginTest")
    public void checkProductCreation() {
        Reporter.log("Open Catalog section <br />");
        WebElement catalogTabElement = wrappedDriver.findElement(By.id("subtab-AdminCatalog"));
        Actions actions = new Actions(wrappedDriver);
        actions.moveToElement(catalogTabElement).build().perform();
        catalogTabElement.findElements(By.cssSelector("li")).get(0).click();
        wrappedDriver.findElement(By.id("page-header-desc-configuration-add")).click();
        wrappedDriver.findElement(By.name("form[step1][name][1]")).sendKeys(getProductName(10));
        wrappedDriver.findElement(By.id("form_step1_qty_0_shortcut")).sendKeys(Integer.toString(randomQuantity()));
        wrappedDriver.findElement(By.id("form_step1_price_shortcut")).sendKeys(Double.toString(randomPrice()));
        wrappedDriver.findElement(By.cssSelector("div.col-lg-5 > div")).click();
        verifyAlert();
        wrappedDriver.findElement(By.cssSelector("div > button.btn.btn-primary.js-btn-save"));
        verifyAlert();
    }

    @Test(dependsOnMethods = "checkProductCreation")
    public void verifyCreatedProduct() {
        wrappedDriver.navigate().to("http://prestashop-automation.qatestlab.com.ua/");
        wrappedDriver.findElement(By.cssSelector("#content > section > a")).click();
        WebElement element = wrappedDriver.findElement(By.xpath("//*[@id=\"js-product-list-top\"]/div[1]/p"));
        String prod = element.getText();
        prod = prod.replaceAll("\\D+","");
        int productsQ = 0;
        try {
            productsQ = Integer.parseInt(prod);
        } catch (NumberFormatException e) {
            Reporter.log("Number Format Exception <br />");
        }
        for (int i = 0; i < productsQ; i++) {
            List<WebElement> elements = wrappedDriver.findElements(By.xpath("//h1/a"));
            products.add(elements.get(i).getText());
        }
        if (products.contains(randName)) {
            Reporter.log("Created product was found <br />");
        } else {
            Reporter.log("Created product was NOT found <br />");
        }

        WebDriverWait wait = new WebDriverWait(wrappedDriver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1/a[.='" + randName + "']")));
        wrappedDriver.findElement(By.xpath("//h1/a[.='" + randName + "']")).click();
        WebElement nameEl = wrappedDriver.findElement(By.cssSelector("div.row > div:nth-child(2) > h1"));
        String name = nameEl.getText();
        softAssertion.assertEquals(name, randName, "Incorrect name: ");

        String priceEl = wrappedDriver.findElement(By.cssSelector("div.product-price.h5 > div > span")).getText();
        priceEl = priceEl.replaceAll("\\D+","");
        int price = 0;
        try {
            price = Integer.parseInt(priceEl);
        } catch (NumberFormatException e) {
            Reporter.log("Price Format Exception");
        }
        softAssertion.assertEquals(price, randomPrice, "Incorrect price: ");

        String quantityEl = wrappedDriver.findElement(By.xpath("//*[@id=\"product-details\"]/div[1]/span")).getText();
        quantityEl = quantityEl.replaceAll("\\D+","");
        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityEl);
        } catch (NumberFormatException e) {
            Reporter.log("Quantity Format Exception");
        }
        softAssertion.assertEquals(quantity, randomQuantity, "Incorrect quantity: ");
        softAssertion.assertAll();
    }

    public void verifyAlert() {
        try {
            WebDriverWait wait = new WebDriverWait(wrappedDriver, 2);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = wrappedDriver.switchTo().alert();
            String message = alert.getText();
            softAssertion.assertEquals(message, "Настройки обновлены.", "Alert message is incorrect");
            alert.dismiss();
        } catch (Exception e) {
            Reporter.log("Alert not present! <br />");
        }
    }

    public String getProductName(int count) {
        String chars = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int index = (int) (rand.nextDouble() * chars.length());
            builder.append(chars.charAt(index));
        }
        randName = builder.toString();
        return randName;
    }

    public int randomQuantity() {
        randomQuantity = new RandomDataGenerator().nextInt(1, 100);
        return randomQuantity;
    }

    public double randomPrice() {
        randomPrice = 0.1 + rand.nextDouble() * (100 - 0.1);
        return randomPrice;
    }
}
