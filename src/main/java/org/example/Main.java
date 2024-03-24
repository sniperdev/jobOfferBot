package org.example;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.Random;

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
            WebElement popup = driver.findElement(new By.ByClassName("popup_p1c6glb0"));
            popup.click();
            Thread.sleep(randomNum());
//            cookie.click();

        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
