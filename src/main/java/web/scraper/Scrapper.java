package web.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Scrapper {

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver_2_29.exe");
        ChromeOptions option = new ChromeOptions();
        option.addArguments("--headless");

        Charset utf8 = StandardCharsets.UTF_8;
        WebDriver driver = new ChromeDriver(option);

        try {
            driver.get("https://covid19-scoreboard.unacast.com/");
            Thread.sleep(5000);

            //get county metrics
            //step1: click on counties
            WebElement countyTag = driver.findElement(By.linkText("Counties"));
            countyTag.findElement(By.xpath("./..")).click();
            Thread.sleep(5000);

            WebElement countyElement = driver.findElement(By.xpath("//*[@id=\"root\"]/main/div[1]/div[3]/div[2]"));
            List counties = getElements(countyElement);
            Files.write(Paths.get("src/output/county.csv"), counties, utf8);

            //get state level metrics
            WebElement stateTag = driver.findElement(By.linkText("States"));
            stateTag.findElement(By.xpath("./..")).click();
            Thread.sleep(5000);

            WebElement stateElement = driver.findElement(By.xpath("//*[@id=\"root\"]/main/div[1]/div[3]/div[2]"));
            List states = getElements(stateElement);
            Files.write(Paths.get("src/output/states.csv"), states, utf8);


        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    public static List getElements(WebElement element) {
        List<WebElement> childElements = element.findElements(By.xpath("./div"));
        System.out.println("Size of Child Elements" + childElements.size());

        return childElements.stream().map(e -> {
            String[] countyDesc = e.getText().split("\n");
            String countyName = countyDesc[0];
            String countyRank = countyDesc[1];

            //Step 4: get the description for the county.
            List<WebElement> countyProperties = e.findElements(By.xpath("./div/div[2]"));
            String desc = countyProperties.get(0).getAttribute("title");
            return countyName + "," + countyRank + "," + desc;
        }).collect(Collectors.toList());
    }
}
