package com.testing.class12;

import com.testing.common.AutoLogger;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class JsonPostTest {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost jsonLogin= new HttpPost("https://b.zhulogic.com/designer_api/account/login_quick_pc");
        StringEntity params=new StringEntity("{\"phone\":\"13412341234\",\"code\":\"123\",\"messageType\":3,\"registration_type\":1,\"channel\":\"zhulogic\",\"unionid\":\"\"}","UTF-8");
        params.setContentType("application/json;charset=UTF-8");
        params.setContentEncoding("UTF-8");
        jsonLogin.setEntity(params);
        //设置请求的头信息，stringentity设置完了之后，这个头域会自动添加，不用画蛇添足。
        jsonLogin.setHeader("Content-Type","application/json;charset=UTF-8");
        CloseableHttpResponse jsonResponse = client.execute(jsonLogin);
        String jsonResult = EntityUtils.toString(jsonResponse.getEntity(), "UTF-8");
        AutoLogger.log.info(jsonResult);
        if (jsonResult.contains("验证码错误")){
            AutoLogger.log.info(
                    "测试成功"
            );
        }

    }
}
