package org.example;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.Random;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    public static int randomNum(){
        Random rand = new Random();
        return rand.nextInt(3001) + 1500;
    }
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        try {
            driver.get("https://login.pracuj.pl/");
            Thread.sleep(randomNum());
            WebElement email = driver.findElement(By.cssSelector("[data-test='input-email']"));
            WebElement emailInput = email.findElement(By.cssSelector("[data-test='input-email']"));
            emailInput.sendKeys("norbertzu3@gmail.com");
            Thread.sleep(randomNum());

            WebElement submitButtonLogin = driver.findElement(By.cssSelector("[type='submit']"));
            submitButtonLogin.click();
            Thread.sleep(randomNum());

            WebElement passwordInput = driver.findElement(By.cssSelector("[data-test='input-password']"));
            passwordInput.sendKeys("kQ2pVr**pE4FV4*roNxe");
            Thread.sleep(randomNum());

            WebElement submitButtonPassword = driver.findElement(By.cssSelector("[data-test='button-submit-login']"));
            submitButtonPassword.click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            driver.get("https://pracuj.pl/");
            Thread.sleep(randomNum());
            WebElement submitButtonCookie = driver.findElement(By.cssSelector("[data-test='button-submitCookie']"));
            Thread.sleep(randomNum());
            submitButtonCookie.click();
//
//            popup.click();

            driver.get("https://it.pracuj.pl/praca/junior%20software%20engineer;kw?itth=84%2C34");
            Thread.sleep(randomNum());
            WebElement popup = driver.findElement(new By.ByClassName("popup_p1c6glb0"));
            popup.click();

//            cookie.click();

        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
