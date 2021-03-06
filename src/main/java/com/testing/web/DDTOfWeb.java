package com.testing.web;

import com.google.common.io.Files;
import com.testing.common.AutoLogger;
import com.testing.common.ExcelWriter;
import com.testing.common.MysqlUtils;
import com.testing.webDriver.FFDriver;
import com.testing.webDriver.GoogleDriver;
import com.testing.webDriver.IEDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DDTOfWeb {

    //成员变量driver，用于各个方法使用浏览器对象进行操作。
    public WebDriver driver;
    //excelwriter对象，用于每个方法执行的时候，完成结果的写入。
    public ExcelWriter results;
    //用于记录当前操作的行号
    public int writeLine;
    //记录写入的列，固定为10
    public static final int RES_COL=10;
    public static final String PASS="pass";
    public static final String FAIL="fail";


    //传参完成excelwriter对象的赋值。
    public DDTOfWeb(ExcelWriter result){
        results=result;
    }
    //设置当前操作的行号
    public void setLine(int line){
        writeLine=line;
    }

    /**
     * 打开浏览器的方法，可以是通过输入浏览器类型启动对应的浏览器
     * chrome firefox IE
     */
    public void openBrowser(String browserType) {
        try {
            switch (browserType) {
                case "chrome":
                    GoogleDriver gg = new GoogleDriver("webDrivers/chromedriver.exe");
                    driver = gg.getdriver();
                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                    results.writeCell(writeLine,RES_COL,PASS);
                    break;
                case "firefox":
                    FFDriver ff = new FFDriver("", "webDrivers/geckodriver.exe");
                    driver = ff.getdriver();
                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                    results.writeCell(writeLine,RES_COL,PASS);
                    break;
                case "ie":
                    IEDriver ie = new IEDriver("webDrivers/IEDriverServer.exe");
                    driver = ie.getdriver();
                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                    results.writeCell(writeLine,RES_COL,PASS);
                    break;
                default:
                    GoogleDriver defaultB = new GoogleDriver("webDrivers/chromedriver.exe");
                    driver = defaultB.getdriver();
                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                    results.writeCell(writeLine,RES_COL,PASS);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            results.writeFailCell(writeLine,RES_COL,FAIL);
        }
    }

    /**
     * 最大化浏览器
     */
    public void maximize() {
        try {
            driver.manage().window().maximize();
            results.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            AutoLogger.log.info("最大化浏览器失败");
            results.writeFailCell(writeLine,RES_COL,FAIL);
            e.printStackTrace();
        }
    }

    /**
     * 设置浏览器窗口
     */
    public void setWindowSize(String startx, String starty, String width, String height) {
        try {
            int x = Integer.parseInt(startx);
            int y = Integer.parseInt(starty);
            int xwidth = Integer.parseInt(width);
            int yheight = Integer.parseInt(height);
            //设置浏览器左上角坐标
            driver.manage().window().setPosition(new Point(x, y));
            //设置浏览器的长宽
            driver.manage().window().setSize(new Dimension(xwidth, yheight));
            results.writeCell(writeLine,RES_COL,PASS);
        } catch (NumberFormatException e) {
            AutoLogger.log.info("设置浏览器大小失败");
            results.writeFailCell(writeLine,RES_COL,FAIL);
            e.printStackTrace();
        }

    }

    /**
     * 访问被测网页
     *
     * @param url
     */
    public void visitURL(String url) {
        try {
            driver.get(url);
            results.writeCell(writeLine,RES_COL,PASS);
            AutoLogger.log.info("访问网页" + url );
        } catch (Exception e) {
            AutoLogger.log.info("访问网页" + url + "失败");
            results.writeFailCell(writeLine,RES_COL,FAIL);
            e.printStackTrace();
        }
    }

    /**
     * 基于css选择器进行元素定位并且输入内容
     *
     * @param css
     * @param content
     */
    public boolean inputByCss(String css, String content) {
        boolean result=false;
        try {
            WebElement element = driver.findElement(By.cssSelector(css));
            element.clear();
            element.sendKeys(content);
            results.writeCell(writeLine,RES_COL,PASS);
            result=true;
        } catch (Exception e) {
            AutoLogger.log.info("向" + css + "元素输入内容失败");
            e.printStackTrace();
            results.writeFailCell(writeLine,RES_COL,FAIL);
            result=false;
        }finally {
            return result;
        }

    }

    /**
     * 基于method的值，来选择定位的方式，并且使用locator作为定位表达式。
     * 例如：<input type="text" class="s_ipt" name="wd" id="kw" maxlength="100" autocomplete="off">
     * @param method 方法类型
     * @param locator  定位表达式
     */
    public void input(String method,String locator,String content){
        try {
            WebElement element=null;
            switch (method){
                //基于元素的id属性进行定位，实际上用的就是#id通过css选择器定位。  用kw.
                case "id":
                   element= driver.findElement(By.id(locator));
                   break;
                   //基于元素name属性定位。  用 wd
                case "name":
                    element=driver.findElement(By.name(locator));
                    break;
                    //基于元素标签名定位，就是input。
                case "tagname":
                    element=driver.findElement(By.tagName(locator));
                    break;
                    //基于css样式class属性定位， 就是"s_ipt"
                case "classname":
                    element=driver.findElement(By.className(locator));
                    break;
                    //基于超链接的文本内容定位，只能用于a元素
                case "linktext":
                    element= driver.findElement(By.linkText(locator));
                    break;
                    //基于超链接的部分文本内容定位，只能用于a元素
                case "partiallinktext":
                    element=driver.findElement(By.partialLinkText(locator));
                    break;
                    //xpath定位，最大优势，可以用text()文本定位
                case "xpath":
                    element=driver.findElement(By.xpath(locator));
                    break;
                    //css选择器定位。 速度快，语法简洁。
                case "css":
                    element=driver.findElement(By.cssSelector(locator));
                    break;
            }
            element.clear();
            element.sendKeys(content);
            results.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            e.printStackTrace();
            results.writeFailCell(writeLine,RES_COL,FAIL);
        }

    }

    /**
     *重载一个input方法，默认使用xpath进行定位输入。
     */
    public void input(String xpath,String content){
        try {
            driver.findElement(By.xpath(xpath)).clear();
            driver.findElement(By.xpath(xpath)).sendKeys(content);
            results.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            AutoLogger.log.info("向"+xpath+"元素中输入内容失败");
            saveErrShot("input方法"+xpath.replace("/","!"));
            results.writeFailCell(writeLine,RES_COL,FAIL);
            e.printStackTrace();
        }
    }


    /**
     * 输出元素的坐标位置,并返回
     */
    public Point showLocation(String css) {
        WebElement element = driver.findElement(By.cssSelector(css));
        Point location = null;
        try {
            location = element.getLocation();
            AutoLogger.log.info(css + "元素的位置是" + location);
            results.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            e.printStackTrace();
            AutoLogger.log.info("没有定位到元素"+css);
            results.writeFailCell(writeLine,RES_COL,FAIL);
        }
        return location;
    }

    /**
     * 通过css定位元素并且进行点击。
     */
    public void clickByCss(String css) {
        try {
            driver.findElement(By.cssSelector(css)).click();
            results.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            AutoLogger.log.info("点击" + css + "元素失败");
            e.printStackTrace();
            results.writeFailCell(writeLine,RES_COL,FAIL);
        }
    }

    /**
     * 通过xpath定位元素并且进行点击。
     * @param xpath
     */
    public boolean click(String xpath){
        try {
            driver.findElement(By.xpath(xpath)).click();
            AutoLogger.log.info("点击"+xpath+"元素");
            results.writeCell(writeLine,RES_COL,PASS);
            return true;
        } catch (Exception e) {
            AutoLogger.log.info("点击"+xpath+"元素失败");
            e.printStackTrace();
            saveErrShot("click方法"+xpath.replace("/","!"));
            results.writeFailCell(writeLine,RES_COL,FAIL);
            return false;
        }
    }

    /**
     * 鼠标移动到某个元素上去悬停
     */
    public void hover(String xpath){
        try {
            Actions act =new Actions(driver);
            act.moveToElement(driver.findElement(By.xpath(xpath))).perform();
            results.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            AutoLogger.log.info("移动到"+xpath+"元素悬停失败");
            results.writeFailCell(writeLine,RES_COL,FAIL);
            e.printStackTrace();
        }
    }

    /**
     * 通过浏览器页面的标题来进行窗口的切换
     * @param expectedTitle
     */
    public void switchWindowByTitle(String expectedTitle){
        try {
            //先获取所有的句柄
            Set<String> windowHandles = driver.getWindowHandles();
            //遍历所有的句柄，尝试切换窗口获取它的标题，如果标题和预期一致就是需要的窗口
            for (String windowHandle : windowHandles) {
                driver.switchTo().window(windowHandle);
                //判断当前窗口的标题是否等于预期标题
                if(driver.getTitle().equals(expectedTitle)){
                    break;
                }
            }
            results.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            e.printStackTrace();
            results.writeFailCell(writeLine,RES_COL,FAIL);
        }
    }

    /**
     * 基于xpath切换Iframe
     * @param xpath
     */
    public void switchIframe(String xpath){
        try {
            driver.switchTo().frame(driver.findElement(By.xpath(xpath)));
            results.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            AutoLogger.log.info("切换iframe"+xpath+"失败");
            results.writeFailCell(writeLine,RES_COL,FAIL);
            e.printStackTrace();
        }
    }

    /**
     * 切换到父级iframe
     */
    public void switchUpIframe(){
        try {
            driver.switchTo().parentFrame();
            results.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            e.printStackTrace();
            AutoLogger.log.info("切换父级iframe失败");
            results.writeFailCell(writeLine,RES_COL,FAIL);
        }
    }

    /**
     * 切换到html根层级
     */
    public void switchOutIframe(){
        try {
            driver.switchTo().defaultContent();
            results.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            e.printStackTrace();
            AutoLogger.log.info("切换到根层级失败");
            results.writeFailCell(writeLine,RES_COL,FAIL);
        }
    }

    /**
     * 直接执行js脚本
     * @param jsScript
     */
    public void runJs(String jsScript){
        //用强转的方式，将driver转换为js执行器类
        try {
            JavascriptExecutor jsRunner=(JavascriptExecutor)driver;
            jsRunner.executeScript(jsScript);
            results.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            AutoLogger.log.info("执行js脚本"+jsScript+"失败");
            e.printStackTrace();
            results.writeFailCell(writeLine,RES_COL,FAIL);
        }
    }

    /**
     * 通过via参数来选择select选择选项的方式
     * @param via
     * @param xpath
     * @param content  选择的参数内容。
     */
    public void select(String xpath,String via,String content){
        try {
            WebElement selele = driver.findElement(By.xpath(xpath));
            Select sel=new Select(selele);
            switch(via){
                case "value":
                    sel.selectByValue(content);
                    break;
                case "text":
                    sel.selectByVisibleText(content);
                    break;
            }
            results.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            e.printStackTrace();
            results.writeFailCell(writeLine,RES_COL,FAIL);
        }

    }

    /**
     * 基于value值来进行下拉框选择。
     * @param xpath
     * @param Value
     */
    public void selectByValue(String xpath,String Value){
        try {
            WebElement selele = driver.findElement(By.xpath(xpath));
            Select sel=new Select(selele);
            sel.selectByValue(Value);
            results.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            e.printStackTrace();
            results.writeFailCell(writeLine,RES_COL,FAIL);
        }
    }

    /**
     * 定位元素并且通过元素来进行相应操作。
     * 因为document语法定位元素不好用，当然可以用document.quereSelector通过css选择器定位
     */
    public void runJsWithElement(String xpath,String jsScript){
        try {
            //先定位元素
            WebElement element = driver.findElement(By.xpath(xpath));
            JavascriptExecutor jsRunner=(JavascriptExecutor)driver;
            //使用元素时，jsScript中用arguments[0]调用后面的参数列表中的第1个元素
            jsRunner.executeScript(jsScript,element);
            results.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            AutoLogger.log.info("执行js脚本"+jsScript+"失败");
            e.printStackTrace();
            results.writeFailCell(writeLine,RES_COL,FAIL);
        }
    }

    /**
     * 输入xpath用于批量进行元素定位并且将元素的text内容遍历存储到map中
     * 通过foreach循环进行list的遍历
     * @param xpath
     * @return
     */
    public List<Map<String,String>> getAllGoodsName(String xpath){
        List<WebElement> elements = driver.findElements(By.xpath(xpath));
        //创建一个list用于存储数据
        List<Map<String,String>> webData=new ArrayList<>();
        //针对每一个数据，存一个map并且加到list中去
        for (WebElement element : elements) {
            //创建一个map用于存储商品名称键值对
            Map<String,String> lidata=new HashMap<>();
            lidata.put("goods_name",element.getText());
            //将每个商品的名称键值对放到list里面去。
            webData.add(lidata);
        }
        AutoLogger.log.info("web端获取到的数据是"+webData);
        return webData;
    }

    /**
     * 基于xpath拼接，遍历各个商品元素，获取名称和价格，存到list<map>中。
     * @return
     */
    public List<Map<String,String>> getGoodsNameandPrice(){
        //获取一共多少个商品，也就是获取有多少个li元素
        List<WebElement> liList = driver.findElements(By.xpath("//div[@class='shop-list-splb p']/ul/li"));
        int goodsCount=liList.size();
        List<Map<String,String>> webDatas=new ArrayList<>();
        //基于xpath遍历每个元素获取它的商品名和价格
        for(int goodsNum=1;goodsNum<=goodsCount;goodsNum++) {
            //获取对应下标的商品的名称
            String goodsName=driver.findElement(By.xpath("//div[@class='shop-list-splb p']/ul/li["+goodsNum+"]//div[@class='shop_name2']")).getText();
            //获取对应下表的商品的价格
            String price=driver.findElement(By.xpath("//div[@class='shop-list-splb p']/ul/li[" + goodsNum + "]//div[@class='price-tag']/span[@class='now']/em[not(@class)]")).getText();
            Map<String,String> goodsdata=new HashMap<>();
            goodsdata.put("goods_name",goodsName);
            goodsdata.put("shop_price",price);
            webDatas.add(goodsdata);
        }
        AutoLogger.log.info("页面上获取到商品名称和价格数据是："+webDatas);
        return  webDatas;

    }

    /**
     * 对比页面上的商品和数据库上的商品数据
     * @param sql
     * @return
     */
    public boolean compareDbAndWebData(String sql){
        boolean result=false;
        //获取页面中的数据
        List<Map<String, String>> webDatas = getGoodsNameandPrice();
        //查找数据库中的内容
        MysqlUtils roy=new MysqlUtils();
        roy.createConnection();
        List<Map<String, String>> dbDatas = roy.queryDatas(sql);
        AutoLogger.log.info("数据库查到的数据是"+dbDatas);
        //如果两个列表互相包含，说明数据是一模一样的
        if(dbDatas.containsAll(webDatas)&&webDatas.containsAll(dbDatas)){
            AutoLogger.log.info("数据库和页面查询数据一致");
            result=true;
        }
        return result;
    }

    /**
     * 输入需要等待的秒数，为了之后进行数据驱动的管理方便，不需要去判断数据类型，统一作为字符串，在方法内部处理。
     *
     * @param second 输入的等待秒数
     */
    public void halt(String second) {
        //将字符串转换为浮点数类型
        float seconds = Float.parseFloat(second);
        //将秒数*1000转换为毫秒
        int millis = (int) (seconds * 1000);
        try {
            Thread.sleep(millis);
            results.writeCell(writeLine,RES_COL,PASS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            results.writeFailCell(writeLine,RES_COL,FAIL);
        }
    }


    /**
     * 通过显式等待，等待某个元素文本内容是expected
     *
     * @param xpath
     * @param expected
     */
    public void explicitlyWait(String xpath, String expected) {
        //先设置心理预期最多等多少秒
        WebDriverWait ewait = new WebDriverWait(driver, 10);
        //直到某个事情发生，则停止等待，去公交车站，不一定是等公交车。
        ewait.until(ExpectedConditions.textToBe(By.xpath(xpath), expected));

    }

    /**
     * 自定义显式等待方法，等待某个元素可用
     * @param xpath
     */
    public void comWait(String xpath) {
        WebDriverWait ewait = new WebDriverWait(driver, 10);
        ewait.until(new ExpectedCondition<Boolean>() {
            //自定义重写apply方法，返回Boolean类型的结果：某个元素的isEnabled状态，是可用。
                        public Boolean apply(WebDriver d) {
                            return d.findElement(By.xpath(xpath)).isEnabled();
                        }
                    }
        );
    }


    /**
     * 判断元素是否已经可用了
     *
     * @param css
     * @return
     */
    public boolean isAbled(String css) {
        boolean isAbled = driver.findElement(By.cssSelector(css)).isEnabled();
        AutoLogger.log.info("元素已经可用了");
        return isAbled;
    }


    /**
     * 断言网页中是否
     *
     * @param expect
     * @return
     */
    public boolean assertPageContains(String expect) {
        String pageSrc = driver.getPageSource();
        if (pageSrc.contains(expect)) {
            AutoLogger.log.info("断言网页源码中包含" + expect + "成功");
            results.writeCell(writeLine,RES_COL,PASS);
            return true;
        } else {
            AutoLogger.log.info("断言网页源码中包含" + expect + "失败");
            results.writeFailCell(writeLine,RES_COL,FAIL);
            return false;

        }
    }

    /**
     * 基于输入判断用标题包含或者等于预期结果
     *
     * @param containsOrEqual 输入 相等或者包含
     * @param expect          预期结果
     * @return
     */
    public boolean assertTitle(String containsOrEqual, String expect) {
        String title = driver.getTitle();
        boolean result = false;
        switch (containsOrEqual) {
            case "包含":
                if (title.contains(expect)) {
                    AutoLogger.log.info("断言标题中包含" + expect + "成功");
                    results.writeCell(writeLine,RES_COL,PASS);
                    result = true;
                } else {
                    AutoLogger.log.info("断言标题中包含" + expect + "失败");
                    results.writeFailCell(writeLine,RES_COL,FAIL);
                    result = false;
                }
                break;
            case "相等":
                if (title.equals(expect)) {
                    AutoLogger.log.info("断言标题为" + expect + "成功");
                    results.writeCell(writeLine,RES_COL,PASS);
                    result = true;
                } else {
                    AutoLogger.log.info("断言标题为" + expect + "失败");
                    results.writeFailCell(writeLine,RES_COL,FAIL);
                    result = false;
                }
                break;
        }
        return result;
    }

    /**
     * 只断言标题相等
     *
     * @param expect
     * @return
     */
    public boolean assertTitleIs(String expect) {
        String title = "";
        try {
            title = driver.getTitle();
        } catch (Exception e) {
            e.printStackTrace();
            AutoLogger.log.info("获取标题失败");
        }
        boolean result = false;
        if (title.equals(expect)) {
            AutoLogger.log.info("断言标题为" + expect + "成功");
            result = true;
        } else {
            AutoLogger.log.info("断言标题为" + expect + "失败");
            result = false;
        }
        return result;
    }

    /**
     * 仅判断标题包含内容
     *
     * @param expect
     * @return
     */
    public boolean assertTitleContains(String expect) {
        String title = driver.getTitle();
        boolean result = false;
        if (title.contains(expect)) {
            AutoLogger.log.info("断言标题中包含" + expect + "成功");
            results.writeCell(writeLine,RES_COL,PASS);
            result = true;
        } else {
            AutoLogger.log.info("断言标题中包含" + expect + "失败");
            results.writeFailCell(writeLine,RES_COL,FAIL);
            result = false;
        }
        return result;
    }


    /**
     * 基于xpath表达式获取元素的文本内容，并且如果输入了预期结果，判断文本内容是否包含所有的预期结果
     * 如果没有输入，则只返回文本内容。
     *
     * @param xpath
     * @param expect String...  可变参数， 可以输入0到n个，作为一个数组使用。
     *               判断的时候遍历每个参数，都进行是否包含的判断
     * @return
     */
    public String getEleText(String xpath, String... expect) {
        try {
            String actual = driver.findElement(By.xpath(xpath)).getText();
            //如果输入了预期结果的参数，则进行判断，如果没有，则只是获取text的值。
            if (expect.length > 0) {
                //取可变参数部分输入的第一个参数作为判断的预期结果。
                for (String s : expect) {
                    if (actual.contains(s)) {
                        results.writeCell(writeLine,RES_COL,PASS);
                        AutoLogger.log.info("包含预期结果" + s);
                    } else {
                        AutoLogger.log.info("不包含预期结果" + s);
                        results.writeFailCell(writeLine,RES_COL,FAIL);
                    }
                }
            }
            return actual;
        } catch (Exception e) {
            e.printStackTrace();
            return "错误";
        }
    }


    /**
     * 基于xpath表达式获取元素的指定属性值，
     * 并且如果输入了预期结果，判断预期结果是否和获取属性值相等，如果没有输入，则只返回属性值。
     *
     * @param xpath  元素定位xpath变道时
     * @param expect String ...
     * @return
     */
    public String getEleAttr(String xpath, String property, String... expect) {
        try {
            String actualAttrValue = driver.findElement(By.xpath(xpath)).getAttribute(property);
            //如果输入了预期结果的参数，则进行判断，如果没有，则只是获取属性的值。
            if (expect.length == 1) {
                //取可变参数部分输入的第一个参数作为判断的预期结果。
                if (actualAttrValue.equals(expect[0])) {
                    AutoLogger.log.info(property + "属性的值" + actualAttrValue + "与预期一致");
                } else {
                    AutoLogger.log.info(property + "属性的值" + actualAttrValue + "与预期不同");
                }
            }
            return actualAttrValue;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取属性失败";
        }
    }



    /**
     * 关闭浏览器
     */
    public void closeBrowser() {
        driver.quit();
    }

    /**
     * 获取实例化的driver。
     * @return
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * 截图方法
     */
    public void saveScrshot(String filename){
        try {
            //创建一个文件用于存放截图
            File scrshot=new File(filename);
            //强转driver为截图对象
            TakesScreenshot ts=(TakesScreenshot)driver;
            //使用临时文件存储截图出来的内容
            File tmp=ts.getScreenshotAs(OutputType.FILE);
            Files.copy(tmp,scrshot);
        } catch (IOException e) {
            e.printStackTrace();
            AutoLogger.log.info("对于整个浏览器截图失败");
        }

    }

    public void saveErrShot(String info){
        //1、记录出错的时间，作为文件名的一部分
        //获取当前时间
        Date now=new Date();
        //创建日期格式的模板
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd-HHmmss");
        //格式化日期模板
        String nowtime=sdf.format(now);
        //2、拼接文件名
        String fileName="logs/screenshot/"+info+nowtime+".png";
        //3、调用方法截图
        saveScrshot(fileName);

    }

}
