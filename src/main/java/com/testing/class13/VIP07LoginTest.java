package com.testing.class13;

import com.testing.common.AutoLogger;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VIP07LoginTest {
    public static void main(String[] args) throws IOException {
        //创建一个cookie池，用于存放cookie，类似于一个包
        CookieStore bag=new BasicCookieStore();
        //roy老婆去进行访问的时候带上包，放房卡。
        CloseableHttpClient roywife = HttpClients.custom().setDefaultCookieStore(bag).build();
        HttpPost login=new HttpPost("http://localhost:8080/VIP07Login/LoginMock");
        StringEntity loginParam=new StringEntity("user=Roy&pwd=123456","UTF-8");
        loginParam.setContentEncoding("utf-8");
        loginParam.setContentType("application/x-www-form-urlencoded");
        login.setEntity(loginParam);
        CloseableHttpResponse loginResponse = roywife.execute(login);
        //登录完成之后，cookie就会保存在指定的cookie池也就是bag中。
        int statusCode = loginResponse.getStatusLine().getStatusCode();
        //获取返回头中的Set-Cookie
        Header[] allHeaders = loginResponse.getAllHeaders();
        //遍历所有的头，找到Set-Cookie头的值，存到一个Map里面
        Map<String,String> cookieMap=new HashMap<>();
        for (Header header : allHeaders) {
            System.out.println(header);
            System.out.println(header.getName()+"的值是："+header.getValue());
            if(header.getName().equals("Set-Cookie")){
                    if(header.getValue().contains("JSESSIONID"))
                    cookieMap.put("Cookie",header.getValue());
            }
        }
        System.out.println("cookieMap的结果是："+cookieMap);

        AutoLogger.log.info("状态码是"+statusCode);
        HttpEntity loginEntity = loginResponse.getEntity();
        String loginResult = EntityUtils.toString(loginEntity, "utf-8");
        AutoLogger.log.info("登录请求的结果是"+loginResult);
        //获取cookie池中的所有cookie。
        List<Cookie> loginCookies = bag.getCookies();
        for (Cookie loginCookie : loginCookies) {
            System.out.println("Cookie对象："+loginCookie);
            System.out.println(loginCookie.getName()+"的值是"+loginCookie.getValue());
        }

        //1、直接通过使用同一个cookie池达到继承cookie使用的效果
        CloseableHttpClient roy = HttpClients.createDefault();//custom().setDefaultCookieStore(bag).build();
        HttpPost userInfo=new HttpPost("http://localhost:8080/VIP07Login/GetUserInfo");
        //2、通过获取Set-Cookie头域的值，将这些cookie值在请求的Cookie头域中添加。
        for (String headerName : cookieMap.keySet()) {
            userInfo.setHeader(headerName,cookieMap.get(headerName));
        }
        CloseableHttpResponse userResponse = roy.execute(userInfo);
        String userInfoResult = EntityUtils.toString(userResponse.getEntity(), "utf-8");
        AutoLogger.log.info("用户信息的结果是"+userInfoResult);




    }
}
