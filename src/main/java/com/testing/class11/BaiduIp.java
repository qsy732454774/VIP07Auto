package com.testing.class11;

import com.testing.common.AutoLogger;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class BaiduIp {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient will = HttpClients.createDefault();
        HttpGet ipGet = new HttpGet("https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=3.3.3.3&co=&resource_id=5809&t=1600346231689&ie=utf8&oe=gbk&cb=op_aladdin_callback&format=json&tn=baidu&cb=jQuery11020010267356615509371_1600346197625&_=1600346197636");
        CloseableHttpResponse ipResponse = will.execute(ipGet);
        System.out.println(ipResponse);
//        HttpEntity ipLetter = ipResponse.getEntity();
//        System.out.println(ipLetter);
//        String ipContent = EntityUtils.toString(ipLetter);
//        System.out.println(ipContent);
        String ipContent=EntityUtils.toString(ipResponse.getEntity(),"UTF-8");
        AutoLogger.log.info("IP接口的返回内容是"+ipContent);
        if (ipContent.contains("美国")){
            AutoLogger.log.info("IP接口测试成功");
        }else {
            AutoLogger.log.info("IP接口测试失败");
        }

        HttpPost LoginPost = new HttpPost("http://www.testingedu.com.cn:8000/index.php?m=Home&c=User&a=do_login&t=0.08634299473818041");
        StringEntity userAndpwd = new StringEntity("13800138005&password=123456&verify_code=1234","UTF-8");
        userAndpwd.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
        userAndpwd.setContentEncoding("UTF-8");
        LoginPost.setEntity(userAndpwd);
        CloseableHttpResponse loginResponse = will.execute(LoginPost);
        String LoginResult = EntityUtils.toString(loginResponse.getEntity(),"UTF-8");
        AutoLogger.log.info("登录接口返回结果是"+LoginResult);
        if (LoginResult.contains("-1")){
            AutoLogger.log.info("错误参数校验成功");

        }else {
            AutoLogger.log.info("错误参数校验失败");
        }
    }
}
