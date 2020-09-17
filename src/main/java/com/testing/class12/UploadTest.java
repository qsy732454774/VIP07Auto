package com.testing.class12;

import com.testing.common.AutoLogger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class UploadTest {
    public static void main(String[] args) throws IOException {
//        HttpHost proxy=new HttpHost("localhost",8888,"http");
//        CloseableHttpClient client= HttpClients.custom().setProxy(proxy).build();
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost upload=new HttpPost("http://www.testingedu.com.cn:8000/index.php/home/Uploadify/imageUp/savepath/head_pic/pictitle/banner/dir/images.html");
        //使用 multipartEntityBuilder来进行请求体的构造。
        MultipartEntityBuilder meb=MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
        //添加文件部分的参数。
        meb.addBinaryBody("file",new File("E:\\QSwork\\素材\\微信二维码.png"), ContentType.IMAGE_PNG,"微信二维码.png");
        //添加文本部分的参数。
        meb.addTextBody("id","WU_FILE_0");
        meb.addTextBody("name","roy.png");

        //生成请求实体
        HttpEntity fileEntity = meb.build();
        upload.setEntity(fileEntity);
        CloseableHttpResponse uploadRes = client.execute(upload);
        String result = EntityUtils.toString(uploadRes.getEntity(), "UTF-8");
        AutoLogger.log.info("上传结果："+result);



    }
}
