package com.testing.inter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.google.gson.JsonObject;
import com.testing.common.AutoLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InterKeyWord {
    //声明成员变量 返回的内容
    public String responseResult;
    //声明成员变量HttpClientKw对象
    public HttpClientKw key;

    //用于存储测试过程中获取到的参数的paramMap
    public Map<String,String> paramMap;

    public InterKeyWord(){

        key=new HttpClientKw();
        paramMap=new HashMap<String,String>();
    }

    public void doGet(String url,String param){
        url=toParam(url);
        param=toParam(param);
        responseResult=key.doGet(url,param);
    }

    public void doPost(String url,String param,String type){
        url=toParam(url);
        param=toParam(param);
        responseResult=key.doPost(url,param,type);
        AutoLogger.log.info("请求的结果是"+responseResult);
    }
    public void doPostUrl(String url,String param){
        url=toParam(url);
        param=toParam(param);
        responseResult=key.doPostUrl(url,param);
        AutoLogger.log.info("请求的结果是"+responseResult);
    }

    //基于jsonPath进行断言。
    public void assertJsonPath(String jsonPath,String expectResult){
        try {
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
        } catch (Exception e) {
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("解析Json并断言失败");
        }
    }

    public void useCookie(){
        key.useCookie();
    }

    public void notUseCookie(){
        key.notUseCookie();
    }

    public void clearCookie(){
        key.clearCookie();
    }


    /**
     * 输入json格式的头域键值对集合，将其解析并且逐个添加到httpclientkw存储的头域map中，方便后续调用。
     * @param headerJson
     */
    public void useHeader(String headerJson){
        //先替换参数的值，再去进行解析和头域添加。
        headerJson=toParam(headerJson);
        JSONObject headers= JSON.parseObject(headerJson);
        //针对json中的每个头域，调用一下添加头域的方法
        for (String headerKey : headers.keySet()) {
            //调用httpclientkw中的添加头域关键字。
            key.addHeader(headerKey,headers.get(headerKey).toString());
        }
        key.useHeader();
    }

    /**
     * 不使用头域信息
     */
    public void notUseHeader(){
        key.notUseHeader();
        key.notUseHeader();
    }

    /**
     * 清空保存的头域map。
     */
    public void clearHeader(){
        key.clearHeader();
    }

    /**
     * 将从之前的返回中通过jsonpath获取到的结果，取名name，存到map中去
     * @param name   存储的参数名
     * @param jsonPath  从返回中获取需要结果的jsonpath
     */
    public void saveParam(String name,String jsonPath){
        try {
            String paramValue=JSONPath.read(responseResult,jsonPath).toString();
            paramMap.put(name,paramValue);
        } catch (Exception e) {
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("解析Json内容失败，请检查表达式");
        }
    }

    /**
     * 替换输入字符中所有符合{参数名}格式的内容，参数名从paramMap中找。
     * @param origin
     */
    public String toParam(String origin){
        for(String key:paramMap.keySet()){
            origin=origin.replaceAll("\\{"+key+"\\}",paramMap.get(key));
        }
        return origin;
    }

    /**
     * 基于指定的格式通过正则解析返回内容中xml格式里的具体返回内容。
     * @param pattern
     */
    public void parseSOAP(String pattern){
        if(responseResult!=null&&responseResult.startsWith("<")) {
            Pattern resultp = Pattern.compile(pattern);
            Matcher resultm = resultp.matcher(responseResult);
            if (resultm.find()) {
                responseResult=resultm.group(1);
                AutoLogger.log.info("解析的结果是：" + responseResult);
            }
            else{
                AutoLogger.log.error("解析使用的正则表达式有问题，请检查！");
            }
        }
        else {
            AutoLogger.log.error("返回不是xml格式，请检查请求过程。");
        }

    }


}
