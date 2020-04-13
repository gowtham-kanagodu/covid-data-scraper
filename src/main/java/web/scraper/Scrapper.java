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
import java.util.ArrayList;
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
            
            WebElement e1 = driver.findElement(By.xpath("//*[@id=\"root\"]/main/div[1]/div[3]/div[2]"));

            //Step 2: get all child elemnets of this `div`. this will only give 1st level children
            List<WebElement> counties = e1.findElements(By.xpath("./div"));

            System.out.println("number of counties:" + counties.size());
            List<String> output = new ArrayList<>();
            //Step 3: loop through each `div`. each item contains county level details.
            for (WebElement county : counties) {
                String[] countyDesc = county.getText().split("\n");
                String countyName = countyDesc[0];
                String countyRank = countyDesc[1];

                //Step 4: get the description for the county.
                List<WebElement> countyProperties = county.findElements(By.xpath("./div/div[2]"));
                String desc = countyProperties.get(0).getAttribute("title");
                output.add(countyName + "," + countyRank + "," + desc);

            }
            output.forEach(System.out::println);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
