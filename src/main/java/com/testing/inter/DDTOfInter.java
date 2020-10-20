package com.testing.inter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.testing.common.AutoLogger;
import com.testing.common.ExcelWriter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DDTOfInter {
    //声明成员变量 返回的内容
    public String responseResult;
    //声明成员变量HttpClientKw对象
    public HttpClientKw key;
    //用于写入结果的excelWriter对象
    public ExcelWriter interResult;

    //用于存储测试过程中获取到的参数的paramMap
    public Map<String,String> paramMap;

    public int writeLine;

    //设置当前操作行数
    public void setLine(int line){
        writeLine = line;
    }

    //记录写入的列，固定为10
    public static final int RES_COL=10;
    //实际返回结果写入
    public static final int ACT_COL=11;
    public static final String PASS="pass";
    public static final String FAIL="fail";

    //构造方法完成需要调用的对象的实例化
    public DDTOfInter(ExcelWriter result){
        key=new HttpClientKw();
        paramMap=new HashMap<String,String>();
        interResult = result;
    }

    public void doGet(String url,String param){
        try {
            url=toParam(url);
            param=toParam(param);
            responseResult=key.doGet(url,param);
            //返回结果记录到结果列
            interResult.writeCell(writeLine,ACT_COL,responseResult);
        } catch (Exception e) {
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("执行Get请求失败");
        }
    }

    public void doPost(String url,String param,String type){
        try {
            url=toParam(url);
            param=toParam(param);
            responseResult=key.doPost(url,param,type);
            interResult.writeCell(writeLine,ACT_COL,responseResult);
        } catch (Exception e) {
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("执行Post请求失败");
        }
    }
    public void post(String url,String param){
        try {
            url=toParam(url);
            param=toParam(param);
            responseResult=key.doPostUrl(url,param);
            interResult.writeCell(writeLine,ACT_COL,responseResult);
        } catch (Exception e) {
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("执行PostUrl请求失败");
        }
    }
    public void postJson(String url,String param){
        try {
            url=toParam(url);
            param=toParam(param);
            responseResult=key.doPostUrl(url,param);
            interResult.writeCell(writeLine,ACT_COL,responseResult);
        } catch (Exception e) {
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("执行PostJson请求失败");
        }
    }

    //基于jsonPath进行断言。
    public void assertJsonSame(String jsonPath,String expectResult){
        try {
            String actualResult = JSONPath.read(responseResult, jsonPath).toString();
            AutoLogger.log.info("解析的实际结果是："+actualResult);
            if(actualResult!=null){
                if(actualResult.equals(expectResult)){
                    AutoLogger.log.info("校验成功");
                    interResult.writeCell(writeLine,RES_COL,PASS);
                }
                else{
                    interResult.writeFailCell(writeLine,RES_COL,FAIL);
                }
            }
            else{
                AutoLogger.log.error("解析出的结果是空");
                interResult.writeFailCell(writeLine,RES_COL,FAIL);
            }
        } catch (Exception e) {
            interResult.writeFailCell(writeLine,RES_COL,FAIL);
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("解析Json并断言失败");

        }
    }

    /**
     * 断言解析的Json中包含某内容
     * @param jsonPath json表达式
     * @param expectResult 预期包含的内容
     */
    public void  assertJsonContains(String jsonPath,String expectResult){
        try {
            String actualResult = JSONPath.read(responseResult, jsonPath).toString();
            AutoLogger.log.info("解析的实际结果是："+actualResult);
            if(actualResult!=null){
                if(actualResult.contains(expectResult)){
                    AutoLogger.log.info("校验成功");
                    interResult.writeCell(writeLine,RES_COL,PASS);
                }
                else{
                    interResult.writeFailCell(writeLine,RES_COL,FAIL);
                }
            }
            else{
                AutoLogger.log.error("解析出的结果是空");
                interResult.writeFailCell(writeLine,RES_COL,FAIL);
            }
        } catch (Exception e) {
            interResult.writeFailCell(writeLine,RES_COL,FAIL);
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("解析Json并断言失败");

        }
    }
    public void assertTextContains(String expect){
        try {
            if (responseResult.contains(expect)){
                AutoLogger.log.info("校验成功");
                interResult.writeCell(writeLine,RES_COL,PASS);
            }
            else{
                interResult.writeFailCell(writeLine,RES_COL,FAIL);
            }
        } catch (Exception e) {
            interResult.writeFailCell(writeLine,RES_COL,FAIL);
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("返回为空，请检查调用逻辑");
        }
    }

    public void useCookie(){
        try {
            key.useCookie();
            interResult.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("执行useCookie失败");
            interResult.writeFailCell(writeLine,RES_COL,FAIL);
        }
    }

    public void notUseCookie(){
        try {
            key.notUseCookie();
            interResult.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("执行notUseCookie失败");
            interResult.writeFailCell(writeLine,RES_COL,FAIL);
        }
    }

    public void clearCookie(){
        try {
            key.clearCookie();
            interResult.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("执行cleraCookie失败");
            interResult.writeFailCell(writeLine,RES_COL,FAIL);
        }
    }


    /**
     * 输入json格式的头域键值对集合，将其解析并且逐个添加到httpclientkw存储的头域map中，方便后续调用。
     * @param headerJson
     */
    public void useHeader(String headerJson){
        //先替换参数的值，再去进行解析和头域添加。
        try {
            headerJson=toParam(headerJson);
            JSONObject headers= JSON.parseObject(headerJson);
            //针对json中的每个头域，调用一下添加头域的方法
            for (String headerKey : headers.keySet()) {
                //调用httpclientkw中的添加头域关键字。
                key.addHeader(headerKey,headers.get(headerKey).toString());
            }
            key.useHeader();
            interResult.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("使用useHeader失败");
            interResult.writeFailCell(writeLine,RES_COL,FAIL);
        }
    }

    /**
     * 不使用头域信息
     */
    public void notUseHeader(){
        try {
            key.notUseHeader();
            interResult.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("NotuseHeader执行失败");
            interResult.writeFailCell(writeLine,RES_COL,FAIL);
        }
    }

    /**
     * 清空保存的头域map。
     */
    public void clearHeader(){
        try {
            key.clearHeader();
            key.notUseHeader();
            interResult.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("clearHeader执行失败");
            interResult.writeFailCell(writeLine,RES_COL,FAIL);
        }
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
            interResult.writeCell(writeLine,RES_COL,PASS);
        } catch (Exception e) {
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("解析Json内容失败，请检查表达式");
            interResult.writeFailCell(writeLine,RES_COL,FAIL);
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
        try {
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
        } catch (Exception e) {
            AutoLogger.log.error(e.fillInStackTrace());
            AutoLogger.log.error("解析parseSOAP失败");
            interResult.writeFailCell(writeLine,RES_COL,FAIL);
        }

    }


}
