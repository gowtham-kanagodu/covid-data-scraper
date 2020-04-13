package web.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.time.Duration;
import java.util.List;

public class Scrapper {

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://covid19-scoreboard.unacast.com/");
            Thread.sleep(5000);

            WebElement countyTag = driver.findElement(By.linkText("Counties"));
            countyTag.findElement(By.xpath("./..")).click();
            Thread.sleep(5000);

//          Step 1:

//            WebElement elements = driver.findElement(By.xpath("//*[@id=\"root\"]/main/div[1]/div[3]/div[2]/div[1]"));
//            System.out.println("county: " + elements.getText());
//            List<WebElement> e2 = elements.findElements(By.tagName("div"));
//            System.out.println(e2.size());
//            for (WebElement element : e2) {
//                System.out.println("Paragraph text:" + element.getText());
//                System.out.println("Paragraph text:" + element.getAttribute("title"));
//            }
            List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"root\"]/main/div[1]/div[3]/div[2]/div[1]"));
            System.out.println("Text:" + elements.get(0).getText());
            List<WebElement> e2 = elements.get(0).findElements(By.xpath("./div/div[2]"));
            System.out.println("Size:" + e2.size());
            for (WebElement element : e2) {
                System.out.println("Paragraph text:" + element.getText());
                System.out.println("Paragraph text:" + element.getAttribute("title"));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
