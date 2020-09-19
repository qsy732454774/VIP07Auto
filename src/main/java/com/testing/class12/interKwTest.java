package com.testing.class12;

import com.testing.inter.InterKeyWord;

public class interKwTest {
    public static void main(String[] args) {
        InterKeyWord inter =new InterKeyWord();
        //inter.useCookie();
        inter.doPost("http://oms-dev.xk12.cn/login","name=qiansiyuan&password=qian891109.com","url");
        inter.assertJsonPath("$.msg","操作成功");
        inter.doPost("http://oms-dev.xk12.cn/appcenter/appVer/uploadExcel","{\"file\":\"D:\\\\学校ID.xlsx\"}","file");
        inter.assertJsonPath("$.msg","获取数据成功！");
    }
}
