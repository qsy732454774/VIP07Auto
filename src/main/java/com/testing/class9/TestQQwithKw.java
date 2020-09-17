package com.testing.class9;

import com.testing.app.AppKeyword;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

public class TestQQwithKw {
    public static void main(String[] args) {
        AppKeyword app=new AppKeyword();
        //启动appium服务器
        app.startAppiumServer("12345","10");
        //连接服务器启动app应用
        app.runAPP("127.0.0.1:62001","5.1.1","com.tencent.mobileqq",".activity.SplashActivity","12345","10");
//        app.getDriver().findElement(By.id("com.tencent.mobileqq:id/btn_login")).click();
//        app.getDriver().findElementByAccessibilityId("请输入QQ号码或手机或邮箱").sendKeys("2798145476");
//        app.getDriver().findElement(By.xpath("//android.widget.EditText[@resource-id='com.tencent.mobileqq:id/password']")).sendKeys("roy12345");
//        app.getDriver().findElement(MobileBy.AccessibilityId("登 录")).click();
//        app.multiByclick("xpath","//*[@text='同意']");
//        app.halt("3");
//        app.click("//*[@content-desc='登 录']");
//        app.halt("60");
        app.click("//android.widget.EditText[@content-desc=\"搜索\"]");
        app.input("//*[@text='搜索']","Roy");
        app.click("//*[@text='青鸿']");
        app.click("//android.widget.ImageView[@content-desc=\"聊天设置\"]");
        app.multiByclick("id","com.tencent.mobileqq:id/clw");
        app.click("//android.widget.LinearLayout[@content-desc=\"QQ空间\"]");
        app.halt("10");
        app.appiumSwipe("400","1300","400","500");

        app.quitApp();
        app.killAppium();
    }
}
