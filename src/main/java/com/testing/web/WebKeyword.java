package com.testing.web;

import com.testing.driver.FFDriver;
import com.testing.driver.GoogleDriver;
import com.testing.driver.IEDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class WebKeyword {

    //成员变量driver，用于各个方法使用浏览器对象进行操作。
    private WebDriver driver;

    /**
     * 打开浏览器的方法，可以是通过输入浏览器类型启动对应的浏览器
     * chrome firefox IE
     */
    public void openBrowser(String browserType) {
        switch (browserType) {
            case "chrome":
                GoogleDriver gg = new GoogleDriver("webDrivers/chromedriver.exe");
                driver = gg.getdriver();
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                break;
            case "firefox":
                FFDriver ff = new FFDriver("", "webDrivers/geckodriver.exe");
                driver = ff.getdriver();
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                break;
            case "ie":
                IEDriver ie = new IEDriver("webDrivers/IEDriverServer.exe");
                driver = ie.getdriver();
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                break;
            default:
                GoogleDriver defaultB = new GoogleDriver("webDrivers/chromedriver.exe");
                driver = defaultB.getdriver();
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                break;
        }
    }

    /**
     * 最大化浏览器
     */
    public void maximize() {
        try {
            driver.manage().window().maximize();
        } catch (Exception e) {
            System.out.println("最大化浏览器失败");
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
        } catch (NumberFormatException e) {
            System.out.println("设置浏览器大小失败");
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
        } catch (Exception e) {
            System.out.println("访问网页" + url + "失败");
            e.printStackTrace();
        }
    }

    /**
     * 基于css选择器进行元素定位并且输入内容
     *
     * @param css
     * @param content
     */
    public void inputByCss(String css, String content) {
        try {
            WebElement element = driver.findElement(By.cssSelector(css));
            element.clear();
            element.sendKeys(content);
        } catch (Exception e) {
            System.out.println("向" + css + "元素输入内容失败");
            e.printStackTrace();
        }
    }

    /**
     * 基于method的值，来选择定位的方式，并且使用locator作为定位表达式。
     * 例如：<input type="text" class="s_ipt" name="wd" id="kw" maxlength="100" autocomplete="off">
     * @param method 方法类型
     * @param locator  定位表达式
     */
    public void input(String method,String locator,String content){
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
        assert element != null;
        element.clear();
        element.sendKeys(content);

    }

    /**
     * 输出元素的坐标位置,并返回
     */
    public Point showLocation(String css) {
        WebElement element = driver.findElement(By.cssSelector(css));
        Point location = null;
        try {
            location = element.getLocation();
            System.out.println(css + "元素的位置是" + location);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
        }
        return location;
    }

    /**
     * 通过css定位元素并且进行点击。
     */
    public void clickByCss(String css) {
        try {
            driver.findElement(By.cssSelector(css)).click();
        } catch (Exception e) {
            System.out.println("点击" + css + "元素失败");
            e.printStackTrace();
        }
    }
    //通过id元素点击
    public void clickById(String id){
        try {
            driver.findElement(By.name(id));
        } catch (Exception e) {
            System.out.println("点击"+id+"元素失败");
            e.printStackTrace();
        }
    }
    //通过xpath元素点击
    public void clickByXpath(String xpath){
        try {
            driver.findElement(By.xpath(xpath)).click();
        } catch (Exception e) {
            System.out.println("点击" + xpath + "元素失败");
            e.printStackTrace();
        }

    }
    //通过name素点击
    public void clickByName(String name){
        try {
            driver.findElement(By.name(name)).click();
        } catch (Exception e) {
            System.out.println("点击" + name + "元素失败");
            e.printStackTrace();
        }

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
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        System.out.println("元素已经可用了");
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
            System.out.println("断言网页源码中包含" + expect + "成功");
            return true;
        } else {
            System.out.println("断言网页源码中包含" + expect + "失败");
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
                    System.out.println("断言标题中包含" + expect + "成功");
                    result = true;
                } else {
                    System.out.println("断言标题中包含" + expect + "失败");
                    result = false;
                }
                break;
            case "相等":
                if (title.equals(expect)) {
                    System.out.println("断言标题为" + expect + "成功");
                    result = true;
                } else {
                    System.out.println("断言标题为" + expect + "失败");
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
            System.out.println("获取标题失败");
        }
        boolean result = false;
        if (title.equals(expect)) {
            System.out.println("断言标题为" + expect + "成功");
            result = true;
        } else {
            System.out.println("断言标题为" + expect + "失败");
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
            System.out.println("断言标题中包含" + expect + "成功");
            result = true;
        } else {
            System.out.println("断言标题中包含" + expect + "失败");
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
                        System.out.println("包含预期结果" + s);
                    } else {
                        System.out.println("不包含预期结果" + s);
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
                    System.out.println(property + "属性的值" + actualAttrValue + "与预期一致");
                } else {
                    System.out.println(property + "属性的值" + actualAttrValue + "与预期不同");
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

}
