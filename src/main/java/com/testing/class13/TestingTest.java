package com.testing.class13;

import com.alibaba.fastjson.JSONPath;
import com.testing.common.AutoLogger;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class TestingTest {

    public static CloseableHttpClient client;
    public static void main(String[] args) throws IOException {
        client = HttpClients.createDefault();
        //auth请求获取token
        HttpPost auth=new HttpPost("http://www.testingedu.com.cn:8081/inter/HTTP/auth");
        String authResult=getResult(auth);
        AutoLogger.log.info(authResult);
        String tokenV = JSONPath.read(authResult, "$.token").toString();
        System.out.println(tokenV);
        //register请求
        HttpPost register=new HttpPost("http://www.testingedu.com.cn:8081/inter/HTTP//register");
        StringEntity registerEn = doEntity("username=roy88&pwd=123456&nickname=roys&describe=royteache");
        register.setEntity(registerEn);
        //添加token头域
        register.setHeader("token",tokenV);
        String registerResult = getResult(register);
        System.out.println(registerResult);

        //login请求
        HttpPost login= new HttpPost("http://www.testingedu.com.cn:8081/inter/HTTP/login");
        //设置请求体
        StringEntity loginEn= doEntity("username=roy88&password=123456");
        login.setEntity(loginEn);
        login.setHeader("token",tokenV);
        String loginResult = getResult(login);
        System.out.println(loginResult);
        //login接口返回的id值，之后getUserInfo接口要用，所以获取一下，这就是关联
        String userIdV=JSONPath.read(loginResult,"$.userid").toString();

        //getUserInfo请求
        HttpPost userInfo=new HttpPost("http://www.testingedu.com.cn:8081/inter/HTTP/getUserInfo");
        StringEntity userEn=doEntity("id="+userIdV);
        userInfo.setEntity(userEn);
        userInfo.setHeader("token",tokenV);
        String userResult=getResult(userInfo);
        System.out.println(userResult);

        //logout请求
        HttpPost logout=new HttpPost("http://www.testingedu.com.cn:8081/inter/HTTP//logout");
        logout.setHeader("token",tokenV);
        String logoutResult=getResult(logout);
        System.out.println(logoutResult);







    }

    /**
     * 生成请求体，并且设置请求的content-type
     * @param param
     * @return
     * @throws UnsupportedEncodingException
     */
    public static StringEntity doEntity(String param) throws UnsupportedEncodingException {
        StringEntity entity=new StringEntity(param);
        entity.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        entity.setContentEncoding("utf-8");
        return entity;
    }

    /**
     * 解析返回体，得到最终结果
     * @param post
     * @return
     * @throws IOException
     */
    public static String getResult(HttpPost post) throws IOException {
        CloseableHttpResponse response = client.execute(post);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        return result;
    }

}
