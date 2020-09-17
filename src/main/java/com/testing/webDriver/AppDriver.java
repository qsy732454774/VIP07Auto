package com.testing.webDriver;

import com.testing.common.AutoLogger;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AppDriver {
    public AndroidDriver driver;

    public AppDriver(String device, String version, String app, String activity, String appiumPort){
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("deviceName",device);
        cap.setCapability("platformName","Android");
        cap.setCapability("platformVersion",version);
        cap.setCapability("appPackage",app);
        cap.setCapability("appActivity",activity);
        cap.setCapability("noReset",true);
        //非必填项
        cap.setCapability("unicodeKeyboard",true);
        cap.setCapability("resetKeyboard",true);
        cap.setCapability("noSign",true);
        cap.setCapability("udid",device);
        URL serverURL = null;
        try {
            serverURL = new URL("http://127.0.0.1:" + appiumPort + "/wd/hub");
            driver=new AndroidDriver(serverURL,cap);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            AutoLogger.log.error("app启动失败，请检查appium日志信息。");
        }
        AndroidDriver driver=new AndroidDriver(serverURL,cap);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public AndroidDriver getDriver(){
        return this.driver;
    }

}
