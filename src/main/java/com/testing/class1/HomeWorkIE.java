package com.testing.class1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class HomeWorkIE {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.ie.driver","WebDrivers/IEDriverServer.exe");
        WebDriver driver = new InternetExplorerDriver();
        driver.get("https://www.baidu.com/");
        WebElement input = driver.findElement(By.id("kw"));
        input.clear();
        input.sendKeys("八百");
        driver.findElement(By.cssSelector("#su")).click();
        Thread.sleep(2000);
        //断言标题
        if ("八百_百度搜索".equals(driver.getTitle()));{ System.out.println("---------------");
            System.out.println(driver.getTitle());
        }

        driver.quit();
    }
}
