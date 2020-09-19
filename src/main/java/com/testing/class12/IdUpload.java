package com.testing.class12;

import com.testing.common.AutoLogger;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

//D:\学校ID.xlsx
public class IdUpload {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost upload = new HttpPost("http://oms-dev.xk12.cn/appcenter/appVer/uploadExcel");
        MultipartEntityBuilder web = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
        web.addBinaryBody("file",new File("D:\\\\学校ID.xlsx"),ContentType.TEXT_PLAIN,"学校ID.xlsx");

        HttpEntity fileEntity = web.build();
        upload.setEntity(fileEntity);
        CloseableHttpResponse upLoadRes = client.execute(upload);
        String result = EntityUtils.toString(upLoadRes.getEntity(),"UTF-8");
        AutoLogger.log.info("上传结果："+result);
    }
}
