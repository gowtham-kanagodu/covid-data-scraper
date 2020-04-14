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
import java.util.*;

import static java.lang.Thread.sleep;

public class Scrapper {

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver_80.exe");
        ChromeOptions option = new ChromeOptions();
        option.addArguments("--headless");

        Charset utf8 = StandardCharsets.UTF_8;
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://covid19-scoreboard.unacast.com/");
            sleep(5000);
            driver.manage().window().fullscreen();
            sleep(2000);

            WebElement stateElement = driver.findElement(By.xpath("//*[@id=\"root\"]/main/div[1]/div[3]/div[2]"));
            List<Map> output = getElements(stateElement, driver);

            Files.write(Paths.get("src/output/states.csv"), output.get(0).values(), utf8);
            Files.write(Paths.get("src/output/counties.csv"), output.get(0).values(), utf8);


        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    public static List getElements(WebElement element, WebDriver driver) throws InterruptedException {
        Map states = new HashMap<String, String>();
        Map counties = new HashMap<String, String>();

        //get all state names
        WebElement stateNameElement = driver.findElement(By.xpath("//*[@id=\"root\"]/main/div[1]/div[3]/div[2]"));
        List<WebElement> stateNames = stateNameElement.findElements(By.xpath("./div"));
        List<String> statesList = new ArrayList<String>();
        for (WebElement st : stateNames) {
            String stateName = st.getText().split("\n")[0].trim();
            System.out.println("stateName -> " + stateName);
            statesList.add(stateName);
        }
        System.out.println("Total states -> " + statesList.size());
        //loop through state by state.
        for (String state : statesList) {
            System.out.println("Processing - " + state);

            WebElement stateElement = driver.findElement(By.xpath("//*[@id=\"root\"]/main/div[1]/div[3]/div[2]"));
            List<WebElement> oneState = stateElement.findElements(By.xpath("//*[text()='" + state + "']"));

            String[] desc = oneState.get(0).getText().split("\n");
            String stateName = desc[0];
            String stateRank = desc[1];

            System.out.println("*******" + stateName);

            List<WebElement> countyProperties = oneState.get(0).findElements(By.xpath("./div/div[2]"));
            String stateDesc = countyProperties.get(0).getAttribute("title");
            System.out.println("*******" + stateDesc);

            states.put(stateName, stateRank + "," + stateDesc);

            countyProperties.get(0).click();
            sleep(5000);

            WebElement countyElement = driver.findElement(By.xpath("//*[@id=\"root\"]/main/div[1]/div[3]/div[2]"));
            List<WebElement> countyElements = countyElement.findElements(By.xpath("./div"));
            System.out.println("Number Of Counties" + countyElements.size());

            for (WebElement ee : countyElements) {
                String[] countyDesc = ee.getText().split("\n");
                String countyName = countyDesc[0];
                String countyRank = countyDesc[1];
                counties.put(stateName, stateName + "," + countyName + "," + countyRank + "," + countyDesc);
                System.out.println(stateName + " -> " + countyName);
            }

            //go back to state view
            driver.findElement(By.xpath("//*[@id=\"root\"]/main/div[2]/div[2]/div[1]/div/div[1]")).click();
            sleep(5000);
        }
        return Arrays.asList(states, counties);
    }
}
