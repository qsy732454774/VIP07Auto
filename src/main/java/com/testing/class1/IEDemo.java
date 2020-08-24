package com.testing.class1;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class IEDemo {
    public static void main(String[] args) {

        //指定webdriver的路径，用相对路径，方便项目移植给别人用
        System.setProperty("webdriver.ie.driver","webDrivers/IEDriverServer.exe");
        //实例化driver完成webdriver的启动。
        //有些同学可能firefox没装在c盘默认路径会需要加上如下代码指定firefox的启动路径，或者直接将firefox扔进环境变量path
//        System.setProperty("webdriver.firefox.bin","");
        //启动浏览器
        WebDriver driver=new InternetExplorerDriver();
        //访问被测网页
        driver.get("https://www.baidu.com");
//        driver.navigate().to("https://www.qq.com/");
        WebElement input = driver.findElement(By.cssSelector("#kw"));
        //先清空输入框元素中的内容
        input.clear();
        //向元素中输入内容
        input.sendKeys("特斯汀");
        //获取元素的坐标（左上角）
        Point location = input.getLocation();
        System.out.println("input元素的坐标是"+location.toString());
        //点击百度一下元素
        driver.findElement(By.cssSelector("#su")).click();
        //强制等待2秒。
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //断言执行是否成功
        System.out.println("-----------------------------------------------------------------");
        //1、断言标题内容
        System.out.println("当前的url是"+driver.getCurrentUrl());
        System.out.println("标题是"+driver.getTitle());
        if("特斯汀_百度搜索".equals(driver.getTitle())){
            System.out.println("测试成功");
        }
        System.out.println("-----------------------------------------------------------------");
        //2、断言页面中是否包含某些字符
        if (driver.getPageSource().contains("特斯汀")) {
            System.out.println("断言包含字符成功");
            System.out.println(driver.getPageSource());
        }
        System.out.println("-----------------------------------------------------------------");
        //3、断言某个元素的文本内容
        String text = driver.findElement(By.cssSelector("#content_left > div:nth-child(2)")).getText();
        System.out.println(text);
        if(text.contains("特斯汀")){
            System.out.println("验证成功");
        }
        //4、断言某个元素的属性值。
        String srcid = driver.findElement(By.cssSelector("#content_left > div:nth-child(2)")).getAttribute("srcid");
        if(srcid.equals("1599")){
            System.out.println("验证成功");
        }
        else {
            System.out.println(srcid);
        }

        driver.findElement(By.cssSelector("#content_left > div:nth-child(2) a")).click();
        //driver.close只关闭当前窗口，如果只有一个窗口浏览器就关掉了，但是不会关掉webdriver应用
//        driver.close();
        //quit关闭浏览器，不管有多少个窗口，并且会将chromedriver.exe进程关闭
        //代码最后一句记得加上driver.quit释放资源关闭浏览器。
        driver.quit();

    }
}
