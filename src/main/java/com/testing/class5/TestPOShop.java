package com.testing.class5;

import com.testing.common.AutoLogger;
import com.testing.pageAdmin.AddGoodsPage;
import com.testing.pageAdmin.AdminLoginPage;
import com.testing.pageShop.HomePage;
import com.testing.pageShop.LoginPage;
import com.testing.web.WebKeyword;

public class TestPOShop {
    public static void main(String[] args) {
        //打开浏览器
        WebKeyword web=new WebKeyword();
        web.openBrowser("chrome");
        //使用po模式，逐个调用页面进行测试
        AutoLogger.log.info("----------------------后台测试--------------------");
        //登录流程
        //页面实例化
        AdminLoginPage adminLoginPage=new AdminLoginPage(web.getDriver());
        //页面加载完成元素定位
        adminLoginPage.load();
        //使用这个登录页面完成登录操作
        adminLoginPage.login();
        //添加商品流程
        AddGoodsPage addGoodsPage=new AddGoodsPage(web);
        addGoodsPage.load();
        addGoodsPage.addGoods();
        AutoLogger.log.info("----------------------前台测试--------------------");
        //传递web的目的是为了让页面使用同一个浏览器。
        LoginPage loginPage=new LoginPage(web);
        loginPage.load();
        loginPage.login();
        //购买商品
        HomePage homePage=new HomePage(web);
        homePage.load();
        homePage.joinCart();

        //关闭浏览器
        web.closeBrowser();


    }
}
