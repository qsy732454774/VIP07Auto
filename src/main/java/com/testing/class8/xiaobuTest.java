package com.testing.class8;

import com.testing.webDriver.AppDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.URL;

public class xiaobuTest {
    public static void main(String[] args) throws IOException {
        //填写capalities
        Runtime.getRuntime().exec("cmd /c start appium");

        AppDriver xiaobu = new AppDriver("0123456789ABCDEF","5.1","com.xiaobu121.xiaobu.xiaobu_android","com.xiaobu121.xiaobu.xiaobu_android.splash.activity.SplashActivity","4723");
//        DesiredCapabilities xiaobu = new DesiredCapabilities();
//        xiaobu.setCapability("deviceName","0123456789ABCDEF");
//        xiaobu.setCapability("platformName","Android");
//        xiaobu.setCapability("platformVersion","5.1");
//        xiaobu.setCapability("appPackage","com.xiaobu121.xiaobu.xiaobu_android");
//        xiaobu.setCapability("appActivity","com.xiaobu121.xiaobu.xiaobu_android.splash.activity.SplashActivity");
//        xiaobu.setCapability("noReset",true);
//        //填写需要连接的服务器
//        URL serverURL = new URL("http://127.0.0.1:4723/wd/hub");
//        AndroidDriver driver=new AndroidDriver(serverURL,xiaobu);
        AndroidDriver driver = xiaobu.getDriver();

    }
}
