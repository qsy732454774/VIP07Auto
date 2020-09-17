package com.testing.class12;

import com.testing.inter.InterKeyWord;

public class InterTest {
    public static void main(String[] args) {
        InterKeyWord inter=new InterKeyWord();
        inter.doGet("https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php", "query=3.3.3.3&co=&resource_id=5809&ie=utf8&oe=gbk");
        inter.assertJsonPath("$.ResultCode","0");
        inter.assertJsonPath("$.Result[0].DisplayData.resultData.tplData.location","美国 亚马逊");
        inter.doPost("http://www.testingedu.com.cn:8000/index.php?m=Home&c=User&a=do_login&t=0.28132331799582766","username=13800138006&password=123456&verify_code=1","url");
        inter.assertJsonPath("$.msg","登陆成功");
        inter.doPost("https://b.zhulogic.com/designer_api/account/login_quick_pc","{\"phone\":\"13412341234\",\"code\":\"123\",\"messageType\":3,\"registration_type\":1,\"channel\":\"zhulogic\",\"unionid\":\"\"}","json");
        inter.assertJsonPath("$.message","验证码错误");
        inter.doPost("http://www.testingedu.com.cn:8000/index.php/home/Uploadify/imageUp/savepath/head_pic/pictitle/banner/dir/images.html","{\"file\":\"E:\\\\QSwork\\\\素材\\\\微信二维码.png\"}","file");
        inter.assertJsonPath("$.state","SUCCESS");
    }
}
