package com.testing.class4;

import com.testing.common.AutoLogger;
import com.testing.web.WebKeyword;

import java.util.Random;

public class ShopAdminTest {
    public static void main(String[] args) {
        AutoLogger.log.info("----------------web自动化开始执行------------------");
        //打开浏览器
        WebKeyword web =new WebKeyword();
        web.openBrowser("chrome");
        //测试商城登录页面
        adminLogin(web);
        //测试商城管理页面
        addGoods(web);
        //关闭浏览器
//        web.closeBrowser();
    }

    public static void addGoods(WebKeyword web) {
        web.visitURL("http://www.testingedu.com.cn:8000/index.php/Admin/Index/index");
        //截图测试
//        web.saveScrshot("logs/screenshot/login.png");
        //2、点击添加商品
        web.click("//a[text()='商城']");
        web.switchIframe("//iframe[@id='workspace']");
        web.click("//span[text()='添加商品']");

        //3、输入必填项
        Random random=new Random();
        int id=random.nextInt(100);
        web.input("//input[@name=\"goods_name\"]","VIP07测试商品"+id);
        web.input("//input[@name=\"shop_price\"]","1000");
        web.input("//input[@name=\"market_price\"]","1500");

        //三个下拉框选择商品类型
        web.select("//select[@id='cat_id']","value","31");
        web.halt("0.5");
        web.select("//select[@id='cat_id_2']","value","35");
        web.halt("0.5");
        web.select("//select[@id='cat_id_3']","value","85");

        //点击选择上传，弹出上传图片的弹出框
        web.click("//input[@class='type-file-file' and contains(@title,('预览图'))]");
        //这里有个iframe
        web.switchIframe("//iframe[contains(@id,'layui')]");
        //完成文件上传。
        web.input("//div[text()='点击选择文件']/following-sibling::div//input[@name='file']","E:\\GitWorkSpace\\VIP07HTML\\computer.png");
        web.click("//div[@class='saveBtn']");

        //由于切到了文件上传的layui这个iframe中操作完之后，需要切出来。
        web.switchUpIframe();
        //点击是否包邮的是
        web.click("//label[text()='是']");

        //富文本框输入内容，这个富文本框在iframe中的p元素
        //先切换iframe
        web.switchIframe("//iframe[contains(@id,'ueditor')]");
        //1、有些富文本框，虽然不是input，但是也可以使用sendkeys方法输入内容
        web.input("//p","Roy的测试商品");
        //2、不能sendkeys的时候，可以考虑用js执行
        web.runJs("document.getElementsByTagName(\"p\")[0].innerText=\"修改之后的商品描述\"");
        //切换到根层级
        web.switchOutIframe();
        //再从根层级切到workspace
        web.switchIframe("//iframe[@id='workspace']");
        //确认提交
        web.click("//a[@id='submit']");
        web.getEleText("//*[@id=\"flexigrid\"]/table/tbody/tr[1]/td[4]/div","VIP07");
    }

    public static void adminLogin(WebKeyword web) {
        web.visitURL("http://www.testingedu.com.cn:8000/index.php/Admin/Admin/login");
        //1、完成后台登录
        web.input("//input[@name='username']","admin");
        web.input("//input[@name='password']","123456");
        web.input("//input[@name='vertify']","1");
        web.click("//input[@name='submit']");
    }
}
