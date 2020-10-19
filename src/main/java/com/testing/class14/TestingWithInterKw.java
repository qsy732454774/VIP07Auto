package com.testing.class14;

import com.alibaba.fastjson.JSONPath;
import com.testing.inter.InterKeyWord;

public class TestingWithInterKw {
    public static void main(String[] args) {
        InterKeyWord inter=new InterKeyWord();
        inter.doPost("http://www.testingedu.com.cn:8081/inter/HTTP/auth","","url");
        inter.saveParam("tokenV","$.token");
        inter.useHeader("{\"token\":\"{tokenV}\",\"User-agent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36\"}");
        inter.doPost("http://www.testingedu.com.cn:8081/inter/HTTP/login","username=roy88&password=123456","url");
        inter.saveParam("idV","$.userid");
        inter.doPost("http://www.testingedu.com.cn:8081/inter/HTTP/getUserInfo","id={idV}","url");
        inter.doPost("http://www.testingedu.com.cn:8081/inter/HTTP//logout","","url");
    }

}
