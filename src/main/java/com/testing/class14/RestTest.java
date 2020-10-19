package com.testing.class14;

import com.testing.inter.InterKeyWord;

public class RestTest {
    public static void main(String[] args) {
        InterKeyWord rest=new InterKeyWord();
        rest.doPost("http://www.testingedu.com.cn:8081/inter/REST/auth","","text");
        rest.saveParam("token","$.token");
        rest.useHeader("{\"token\":\"{token}\"}");
        rest.doPost("http://www.testingedu.com.cn:8081/inter/REST/user/register","{\"username\":\"roy91\",\"pwd\":\"123456\",\"nickname\":\"roy09\",\"describe\":\"123456\"}","url");
        rest.doPost("http://www.testingedu.com.cn:8081/inter/REST/login/roy8/123456","","url");
        rest.saveParam("id","$.userid");
        rest.doPost("http://www.testingedu.com.cn:8081/inter/REST/login/{id}","","url");
        rest.doPost("http://www.testingedu.com.cn:8081/inter/REST/login","","text");

    }
}
