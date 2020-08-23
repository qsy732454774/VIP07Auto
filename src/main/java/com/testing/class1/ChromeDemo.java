package com.testing.class1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ChromeDemo {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver","WebDrivers/chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        driver.get("https://www.baidu.com/");
        WebElement input = driver.findElement(By.id("kw"));
        input.clear();
        input.sendKeys("八百");
        //driver.navigate().to("https://www.baidu.com/");
        driver.findElement(By.cssSelector("#su")).click();

        //断言标题

        //断言元素文本

        //断言元素属性
       // driver.quit();
    }
}
