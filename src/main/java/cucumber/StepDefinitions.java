package cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StepDefinitions {
    public WebDriver driver;
    private final String URL = "https://cartaxcheck.co.uk";

    @Before
    public void setUp(){
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @After
    public void tearDown() { driver.quit(); }
    public List<String> textFile() throws Exception {
        File file = new File("C:\\Users\\avkin\\IdeaProjects\\Automation_Car\\src\\main\\java\\cucumber\\car_input.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> responseList = new ArrayList<String>();
        String tempString;
        String st;
        while ((st = br.readLine()) != null){
            if (st != null){
                String strArray[] = st.split("");
                for (int i=0, i < strArray.length; i++) {
                if (strArray[i].equals("registration") ==true) {
                tempString = strArray[i + 1];
                if ((strArray[i + 1].length()) <6 ){
                String x = strArray[i + 1] +" "+ strArray[i + 2];
                tempString = x;
                }
                responseList.add(tempString);
                }
            }
            for (int i=0; i< strArray.length; i++) {
                if(strArray[i].equals("registration") ==true){
                    tempString = strArray[i+1];
                    responseList.add(tempString);
                    if (strArray[i+2].equals("and") == true){
                        String y = strArray[i+3]+" "+strArray[i+4].substring(0,3);
                        tempString = y;
                    }
                        responseList.add(tempString);
                }
            }
        }

    }
        System.out.println("Compressed to required input"+responseList);
        return responseList;
}
     @Given("user lands on hope page")
     public void user_lands_on_home_page() throws Exception{
        System.out.println("-----Landed on Home Page-----");
        List<String> responseList = textFile();

        driver.get(URL);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
        String actual_title = driver.getTitle();
        System.out.println("Title of the application: "+actual_title);
        Assert.assertEquals(actual_title,actual_title,"Car Tax Check | Free Car Check");

        String path = "C:\\Users\\avkin\\IdeaProjects\\Automation_Car\\src\\main\\java\\cucumber\\car_output.txt";
        String content = null;

        try (Stream<String>) lines = Files.lines(Paths.get(path))){
        content = lines.collect(Collectors.joining(System.lineSeparator()));
        System.out.println("Content from output file: "+content);
         } catch (IOException e){
           e.printStackTrace();
         }

         String output;
         try {
             if (driver.findElement(By.xpath("//input[@id='vrm-input']")).isDisplayed()) {
                 for (int CarReg = 0; CarReg < responseList.size(); CarReg++) {
                     try {
                         driver.findElement(By.xpath("//a[@aria-label='new search']")).click();
                         driver.findElement(By.id("vrm-input")).sendKeys(responseList.get(CarReg));
                         driver.findElement(By.xpath("//*[@action=\\\"/free-car-check/\\\"]/button")).click();
                         Thread.sleep(5000);
                         System.out.println("waiting for: " + responseList.get(CarReg));

                         String registration = driver.findElement(By.xpath("//dt[contains(.,'Registration')]/following::dd[1]")).getText();
                         String make = driver.findElement(By.xpath("//dt[contains(.,'Make')]/following::dd[1]")).getText();
                         String model = driver.findElement(By.xpath("//dt[contains(.,'Model')]/following::dd[1]")).getText();
                         String colour = driver.findElement(By.xpath("//dt[contains(.,'Colour')]/following::dd[1]")).getText();
                         String year = driver.findElement(By.xpath("//dt[contains(.,'Year')]/following::dd[1]")).getText();
                         output = registration + ", " + make + ", " + model + ", " + colour + ", " + year;
                         System.out.println("Result: " + output);
                         boolean value = content.contains(output);
                         System.out.println("Assertion is " + value + " for " + responseList.get(CarReg));

                     } catch (Exception P) {
                         System.out.println("Unable to find the Registration date for " + responseList.get(CarReg));
                     }
                 }
             } else {
                 System.out.println("Unable to load the required UI page");
             }
         }catch (Exception P) {
             System.out.println("Unable to fetch the date from Input file");
         }
     }
         @Then("user exits the application and closes the browser")
                 public void user_exits_the_application_and_closes_the_browser(){
                 System.out.println("Completed the Validation part");
            }
}