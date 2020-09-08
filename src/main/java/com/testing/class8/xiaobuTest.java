package com.testing.class8;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class xiaobuTest {
    public static void main(String[] args) throws MalformedURLException {
        //填写capalities
        DesiredCapabilities xiaobu = new DesiredCapabilities();
        xiaobu.setCapability("deviceName","0123456789ABCDEF");
        xiaobu.setCapability("platformName","Android");
        xiaobu.setCapability("platformVersion","5.1");
        xiaobu.setCapability("appPackage","com.xiaobu121.xiaobu.xiaobu_android");
        xiaobu.setCapability("appActivity","com.xiaobu121.xiaobu.xiaobu_android.icon_default");
        xiaobu.setCapability("noReset",true);
        //填写需要连接的服务器
        URL serverURL = new URL("http://127.0.0.1:4723/wb/hub");
        AndroidDriver driver=new AndroidDriver(serverURL,xiaobu);
    }
}
