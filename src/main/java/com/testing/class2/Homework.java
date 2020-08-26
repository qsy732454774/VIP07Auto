package com.testing.class2;

import com.testing.web.WebKeyword;

public class Homework {
    public static void main(String[] args) {
        WebKeyword web = new WebKeyword();
        web.openBrowser("chrome");
        web.maximize();
        web.visitURL("http://www.testingedu.com.cn:8000/Home/user/login.html");
        web.halt("1");
        web.input("id","username","13800138006");
        web.halt("1");
        web.input("name","password","123456");
        web.halt("1");
        web.input("name","verify_code","1234");
        web.halt("1");
        web.clickByName("sbtbutton");
        web.halt("5");
        web.assertTitleContains("我的账户-开源商城");
        web.halt("1");
        web.click("class","home");
        web.halt("1");
        //web.input("class","ecsc-search-input","vivo");
        //web.click("xpath","/html/body/div[4]/div/div[2]/div[2]/ul/li[1]");
        web.click("xpath","/html/body/div[4]/div[2]/div[2]/a[1]/div[1]");
        web.halt("1");
        web.click("class","paybybill buy_button");
        web.halt("1");
        web.assertPageContains("购物车结算-开源商城");
        web.halt("1");
        web.closeBrowser();
    }
}
