package com.testing.class13;

import com.alibaba.fastjson.JSONPath;
import com.testing.inter.HttpClientKw;

public class TestingWithKw {
    public static void main(String[] args) {
        HttpClientKw kw=new HttpClientKw();
        String auth=kw.doPost("http://www.testingedu.com.cn:8081/inter/HTTP/auth","","url");
        String tokenV = JSONPath.read(auth, "$.token").toString();
        //kw.useHeader("token",tokenV);
        kw.notUseHeader();
        kw.doPost("http://www.testingedu.com.cn:8081/inter/HTTP//register","username=roy88&pwd=123456&nickname=roys&describe=royteache","url");
        String login=kw.doPost("http://www.testingedu.com.cn:8081/inter/HTTP/login","username=roy88&password=123456","url");
        String userIdV = JSONPath.read(login, "$.userid").toString();
        kw.doPost("http://www.testingedu.com.cn:8081/inter/HTTP/getUserInfo","id="+userIdV,"url");
        kw.doPost("http://www.testingedu.com.cn:8081/inter/HTTP//logout","","url");

    }



}
