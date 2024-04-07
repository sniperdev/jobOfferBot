package org.example;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.MongoException;
//import com.mongodb.ServerApi;
//import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
//import java.util.Arrays;
import java.util.Date;
//import org.bson.types.ObjectId;

public class Main {

    public static void connectTest() {
        MongoClient mongoClient = MongoClients.create("mongodb+srv://Norbert:Zubrzak@joboffer.hifyopr.mongodb.net/?retryWrites=true&w=majority&appName=JobOffer");
        MongoDatabase database = mongoClient.getDatabase("JobOffer");
        MongoCollection<Document> collection = database.getCollection("Links");

        try{
            Document ping = new Document("ping", 1);
            database.runCommand(ping);
            System.out.println("Połączono z bazą danych");
        } catch (Exception e) {
            System.out.println("Nie połączono z bazą danych: " + e.getMessage());
        }

//        Document ogloszenie1 = createOgloszenieDocument("Programista Java", true, new Date(), "Warszawa, ul. Przykładowa 123", "Umowa o pracę", "Pełny etat", "http://przykladowy.link/do/ogloszenia");
//        Document ogloszenie2 = createOgloszenieDocument("Specjalista ds. SEO", false, null, "Kraków, ul. Kolejowa 47", "B2B", "Część etatu", "http://przykladowy.link/do/ogloszenia2");
//        Document ogloszenie3 = createOgloszenieDocument("Project Manager", true, new Date(), "Gdańsk, ul. Morska 88", "Kontrakt", "Pełny etat", "http://przykladowy.link/do/ogloszenia3");
//
//        collection.insertMany(Arrays.asList(ogloszenie1, ogloszenie2, ogloszenie3));

        mongoClient.close();
    }
    private static Document createOgloszenieDocument(String tytulOgloszenia, boolean czyWyslanoCV, Date dataWyslaniaCV, String adres, String rodzajUmowy, String wymiarPracy, String linkDoOgloszenia) {
        Document doc = new Document("tytulOgloszenia", tytulOgloszenia)
                .append("czyWyslanoCV", czyWyslanoCV)
                .append("dataWyslaniaCV", dataWyslaniaCV)
                .append("adres", adres)
                .append("rodzajUmowy", rodzajUmowy)
                .append("wymiarPracy", wymiarPracy)
                .append("linkDoOgloszenia", linkDoOgloszenia);
        return doc;
    }

    private static String EMAIL;
    private static String PASSWORD;

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(fis);

            EMAIL = prop.getProperty("email");
            PASSWORD = prop.getProperty("password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final WebDriver driver = new ChromeDriver();
    private static int randomNum(){
        Random rand = new Random();
        return rand.nextInt(3001) + 1500;
    }

    private static void waitForAction(){
        try{
            Thread.sleep(randomNum());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void clickElement(String cssSelector){
        try{
            driver.findElement(By.cssSelector(cssSelector)).click();
            waitForAction();
        } catch (NoSuchElementException ignored) {
            System.out.println("Element not found - " + cssSelector);
        }
    }


    private static void cookies(){
        String[] selectors = new String[]{
                "[data-test='button-accept']",
                "[data-test='button-submitCookie']"
        };
        for(String selector : selectors){
            clickElement(selector);
        }
    }

    public static void main(String[] args) {
        connectTest();

        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        driver.get("https://login.pracuj.pl/");
        waitForAction();
        WebElement email = driver.findElement(By.cssSelector("[data-test='input-email']"));
        WebElement emailInput = email.findElement(By.cssSelector("[data-test='input-email']"));
        emailInput.sendKeys(EMAIL);
        waitForAction();

        clickElement("[type='submit']");

        WebElement passwordInput = driver.findElement(By.cssSelector("[data-test='input-password']"));
        passwordInput.sendKeys(PASSWORD);
        waitForAction();

        clickElement("[data-test='button-submit-login']");

        driver.get("https://pracuj.pl/");
        cookies();
        driver.get("https://it.pracuj.pl/praca?et=17%2C4&its=frontend%2Cfullstack&itth=77%2C38%2C76%2C33%2C34%2C42");
//        driver.get("https://it.pracuj.pl/praca/junior%20software%20engineer;kw?itth=84%2C34");




        List<WebElement> offers = driver.findElements(new By.ByClassName("c1fljezf"));
        List<WebElement> fastButtonOffers = new ArrayList<>();
        waitForAction();
        for (WebElement offer : offers){
            try{
                offer.findElement(By.cssSelector("[data-test='text-one-click-apply']"));
                fastButtonOffers.add(offer);
            }
            catch(NoSuchElementException ignored){}
        }
        System.out.println(fastButtonOffers.getFirst());
        List<String> offersLinks = new ArrayList<>();
        for(WebElement fastButtonOffer : fastButtonOffers){
//            WebElement linkElement = fastButtonOffer.findElement(By.cssSelector("[data-test='link-offer']"));
            WebElement linkElement = fastButtonOffer.findElement(new By.ByClassName("core_n194fgoq"));
            String hrefValue = linkElement.getAttribute("href");
            offersLinks.add(hrefValue);
        }
        System.out.println("--------------------");
        System.out.println("-----LINKI DO OFERT-----");
        System.out.println("--------------------");
        System.out.println(offersLinks);

//        for (String offerLink : offersLinks){
//            driver.get(offerLink);
//            waitForAction();
//            cookies();
//            WebElement linkElement = driver.findElement(By.cssSelector("[data-test='anchor-apply']"));
//            String hrefValue = linkElement.getAttribute("href");
//            driver.get(hrefValue);
//            WebElement submitButton = driver.findElement(new By.ByClassName("b14qiyz3"));
//            submitButton.click();
//        }
    }
}
