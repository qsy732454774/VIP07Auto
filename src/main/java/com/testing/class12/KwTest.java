package com.testing.class12;

import com.alibaba.fastjson.JSONPath;
import com.testing.common.AutoLogger;
import com.testing.inter.HttpClientKw;


public class KwTest {
    public static void main(String[] args) {
        HttpClientKw roy=new HttpClientKw();
        String ipResult = roy.doGet("https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php", "query=3.3.3.3&co=&resource_id=5809&ie=utf8&oe=gbk");
        String shopLogin=roy.doPost("http://www.testingedu.com.cn:8000/index.php?m=Home&c=User&a=do_login&t=0.28132331799582766","username=13800138006&password=123456&verify_code=1","url");
        String jsonLogin=roy.doPostJson("https://b.zhulogic.com/designer_api/account/login_quick_pc","{\"phone\":\"13412341234\",\"code\":\"123\",\"messageType\":3,\"registration_type\":1,\"channel\":\"zhulogic\",\"unionid\":\"\"}");
        String upload=roy.doPost("http://www.testingedu.com.cn:8000/index.php/home/Uploadify/imageUp/savepath/head_pic/pictitle/banner/dir/images.html","{\"file\":\"E:\\\\QSwork\\\\素材\\\\微信二维码.png\"}","file");
        AutoLogger.log.info("ip接口结果是："+ipResult);
        String resultCode=JSONPath.read(ipResult,"$.ResultCode").toString();
        AutoLogger.log.info("resultcode的结果是"+resultCode);
        String dataLocation=JSONPath.read(ipResult,"$.data[0].location").toString();
        AutoLogger.log.info("data中的location的结果是"+dataLocation);
        String resultLocation=JSONPath.read(ipResult,"$.Result[0].DisplayData.resultData.tplData.location").toString();
        AutoLogger.log.info("result中的location的结果是"+resultLocation);

        AutoLogger.log.info("商城登录接口结果是："+shopLogin);
        String msg=JSONPath.read(shopLogin,"$.msg").toString();
        AutoLogger.log.info("登录的msg是"+msg);

        AutoLogger.log.info("json登录接口结果是："+jsonLogin);
        String jsonMsg=JSONPath.read(jsonLogin,"$.message").toString();
        AutoLogger.log.info("json的message是："+jsonMsg);

        AutoLogger.log.info("上传接口的结果是："+upload);
        String uploadState=JSONPath.read(upload,"$.state").toString();
        AutoLogger.log.info("upload的state是："+uploadState);
    }
}
