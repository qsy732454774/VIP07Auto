package com.testing.class13;

import com.testing.inter.HttpClientKw;

public class VIP07LoginWithKw {
    public static void main(String[] args) {
        HttpClientKw inter=new HttpClientKw();
        inter.useCookie();
        inter.doPost("http://localhost:8080/VIP07Login/LoginMock","user=Roy&pwd=123456","url");
        inter.doPost("http://localhost:8080/VIP07Login/GetUserInfo","","url");
        inter.notUseCookie();
        inter.doPost("http://localhost:8080/VIP07Login/GetUserInfo","","url");
        inter.useCookie();
        inter.doPost("http://localhost:8080/VIP07Login/GetUserInfo","","url");


    }
}
