package org.example;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.time.Duration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
//import java.util.Arrays;
import java.util.Date;
//import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

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
//        Document ogloszenie3 = createOgloszenieDocument("Project Manager", true, new Date(), new Date(12, kwi, 2024), "Gdańsk, ul. Morska 88", "Kontrakt", "Pełny etat", "http://przykladowy.link/do/ogloszenia3");
//
//        collection.insertMany(Arrays.asList(ogloszenie1, ogloszenie2, ogloszenie3));
//        collection.insertOne(ogloszenie3);

//        mongoClient.close();
    }
    private static Document createOgloszenieDocument(String tytulOgloszenia, String nazwaFirmy, boolean czyWyslanoCV, Date dataWyslaniaCV, Date waznoscOgloszenia, String adres, String rodzajUmowy, String wymiarPracy, String linkDoOgloszenia) {
        Document doc = new Document("tytulOgloszenia", tytulOgloszenia)
                .append("nazwaFirmy", nazwaFirmy)
                .append("czyWyslanoCV", czyWyslanoCV)
                .append("dataWyslaniaCV", dataWyslaniaCV)
                .append("waznoscOgloszenia", waznoscOgloszenia)
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
            System.out.println(EMAIL);
            PASSWORD = prop.getProperty("password");
            System.out.println(PASSWORD);
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

    private static LocalDate parseDate(String dateString) {
        // Definicja wzorców dat
        DateTimeFormatter[] formatters = new DateTimeFormatter[]{
                DateTimeFormatter.ofPattern("dd-MM-yyyy"), // DD-MM-RRRR
                DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH), // 12 Apr 2024
                DateTimeFormatter.ofPattern("d LLL yyyy", new Locale("pl", "PL")), // 12 kwi 2024
                // Możesz dodać więcej formatów zgodnie z potrzebami
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                // Usuwanie prefixów "do: " i "until: "
                String normalizedDate = dateString.replaceAll("^(do: |until: )", "").trim();
                return LocalDate.parse(normalizedDate, formatter);
            } catch (DateTimeParseException e) {
                // Ignorowanie błędów i próbowanie kolejnych formatów
            }
        }

        throw new DateTimeParseException("Nie udało się sparsować daty: " + dateString, dateString, 0);
    }

    private static void dateOfPage(String detailedlink){

        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(detailedlink);
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
        cookies();
        waitForAction();
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("offer-viewnqE8MW")));

        String title = driver.findElement(By.className("offer-viewkHIhn3")).getText();
        System.out.println(title);
        WebElement element = driver.findElement(By.cssSelector("[data-test='text-employerName']"));
        String fullText = element.getText();
        String companyName = fullText.replace("O firmie", "").trim();
        System.out.println(companyName);
//        System.out.println(sendCV);
//        System.out.println(cvSendingDate);
        String expiryDate = driver.findElement(By.className("offer-viewDZ0got")).getText();
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); // Format wyjściowy
        try {
            LocalDate parsedDate = parseDate(expiryDate);
            expiryDate = parsedDate.format(outputFormatter); // Formatowanie daty do wyjściowego formatu
            System.out.println(expiryDate);
        } catch (DateTimeParseException e) {
            //System.out.println(e.getMessage());
        }
        String address = driver.findElement(By.cssSelector("[data-test='text-benefit']")).getText();
        System.out.println(address);
        String contractType = driver.findElement(By.cssSelector("[data-test='sections-benefit-work-modes-text']")).getText();
        System.out.println(contractType);
        String workingTime = driver.findElement(By.cssSelector("[data-test='sections-benefit-contracts-text']")).getText();
        System.out.println(workingTime);
        System.out.println(detailedlink);


        driver.close();

    }

    public static void main(String[] args) {
        //connectTest();

        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        driver.get("https://login.pracuj.pl/");
        waitForAction();
        //dateOfPage("https://www.pracuj.pl/praca/software-development-manager-warszawa,oferta,1003267369?sug=list_top_cr_bd_10_tname_202_tgroup_A&s=1f7c2c91&searchId=MTcxMjg3NDEyNTE4Ny44MjMy");
//        String hrefValue = linkElement.getAttribute("href");
//        waitForAction();


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
