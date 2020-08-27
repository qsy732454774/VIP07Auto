package com.testing.class3;

import com.testing.web.WebKeyword;

public class TpshopTest {
    public static void main(String[] args) {
        WebKeyword web =new WebKeyword();
        web.openBrowser("chrome");
        web.maximize();
        web.visitURL("http://www.testingedu.com.cn:8000/index.php");
        web.halt("1");
        web.clickByXpath("//a[text()='登录']");
        web.halt("1");
        web.input("xpath","//*[@autofocus='autofocus']","732454774@qq.com");
        web.halt("1");
        web.input("xpath","//*[@type='password']","123456");
        web.halt("1");
        web.input("xpath","//*[@id='verify_code']","1234");
        web.halt("1");
        web.clickByXpath("//*[@class='J-login-submit']");
        web.halt("1");
        web.assertTitleContains("我的账户-开源商城");
        //web.clickByXpath("//a[text()='首页']");
        web.halt("3");
        web.closeBrowser();
    }
}
