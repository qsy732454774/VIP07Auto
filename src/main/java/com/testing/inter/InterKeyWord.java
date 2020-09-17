package com.testing.inter;

import com.alibaba.fastjson.JSONPath;
import com.testing.common.AutoLogger;

public class InterKeyWord {
    //声明成员变量 返回的内容
    public String responseResult;

    public void doGet(String url,String param){
        HttpClientKw key =new HttpClientKw();
        responseResult=key.doGet(url,param);
    }

    public void doPost(String url,String param,String type){
        HttpClientKw key =new HttpClientKw();
        responseResult=key.doPost(url,param,type);
    }

    //基于jsonPath进行断言。
    public void assertJsonPath(String jsonPath,String expectResult){
        String actualResult = JSONPath.read(responseResult, jsonPath).toString();
        AutoLogger.log.info("解析的实际结果是："+actualResult);
        if(actualResult!=null){
            if(actualResult.equals(expectResult)){
                AutoLogger.log.info("校验成功");
            }
            else{
                AutoLogger.log.error("校验失败");
            }
        }
        else{
            AutoLogger.log.error("解析出的结果是空");
        }

    }

}