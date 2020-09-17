package com.testing.class5;

import com.testing.class3.TpshopTest;
import com.testing.class4.ShopAdminTest;
import com.testing.common.AutoLogger;
import com.testing.web.WebKeyword;

public class ShopAllTest {
    public static void main(String[] args) {
        //打开浏览器
        WebKeyword keyword =new WebKeyword();
        keyword.openBrowser("chrome");
        AutoLogger.log.info("-------------------商城后台测试执行------------------");
        ShopAdminTest.adminLogin(keyword);
        ShopAdminTest.addGoods(keyword);
        AutoLogger.log.info("-------------------商城前台测试执行------------------");
      //  TpshopTest.shopLogin(keyword);
        //TpshopTest.buyGoods(keyword);
        //关闭浏览器
        keyword.closeBrowser();


    }
}
