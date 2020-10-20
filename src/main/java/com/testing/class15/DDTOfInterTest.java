package com.testing.class15;

import com.testing.common.ExcelReader;
import com.testing.common.ExcelWriter;
import com.testing.inter.DDTOfInter;
import com.testing.inter.InterKeyWord;
import org.apache.xmlbeans.GDate;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DDTOfInterTest {
    public static void main(String[] args) {

        ExcelReader interCase = new ExcelReader("Cases/interCases.xlsx");
        //添加时间，方便区分结果文件
        Date date=new Date();
        SimpleDateFormat dft = new SimpleDateFormat("yyy-MM-dd HH-mm-ss");
        String nowtime = dft.format(date);


        ExcelWriter interResult = new ExcelWriter("Cases/interCases.xlsx",
                "Cases/resultOf"+nowtime+"Inter.xlsx");

        DDTOfInter inter = new DDTOfInter(interResult);

        //遍历用例文件，对每一行读取其中的关键字调用对应的关键字方法
        for (int sheetNo=0;sheetNo<interCase.getTotalSheetNo();sheetNo++){
            //excelreader和excelwriter使用当前sheet页
            interCase.useSheetByIndex(sheetNo);
            interResult.useSheetByIndex(sheetNo);
            System.out.println("当前sheet页为:"+interResult.getSheetName(sheetNo));
            //遍历每一行
            for (int rowNo=0;rowNo<interCase.getRowNo();rowNo++){
                //读取一行数据，作为一个List
                List<String> rowContent = interCase.readLine(rowNo);
                System.out.println("当前是第"+(rowNo + 1)+"行，内容是："+rowContent);
                inter.setLine(rowNo);
                invokeInter(inter,rowContent);
                invokeAssert(inter,rowContent);

                //判断关键字，执行相关操作
//                switch (rowContent.get(3)){
//                    case "get":
//                        inter.doGet(rowContent.get(4),rowContent.get(5));
//                        break;
//                    case "post":
//                        inter.doPostUrl(rowContent.get(4),rowContent.get(5));
//                        break;
//                    case "addHeader":
//                        inter.useHeader(rowContent.get(4));
//                        break;
//                    case "clearHeader":
//                        inter.clearHeader();
//                        break;
//                    case "saveParam":
//                        inter.saveParam(rowContent.get(4),rowContent.get(5));
//                        break;
//                    case "useCookie":
//                        inter.useCookie();
//                        break;
//                    case "notUseCookie":
//                        inter.notUseCookie();
//                        break;
//                }
                //判断断言的关键字，执行相关的操作
                switch (rowContent.get(7)){
                    case"assertJsonSame":
                        inter.assertJsonSame(rowContent.get(8),rowContent.get(9));
                        break;


                }
            }

        }
        interCase.close();
        interResult.save();


    }


    public static void invokeInter(Object api,List<String> rowContent){
        //查找有没有符合关键字名的方法，从0个开始查找
        try {
            Method target = api.getClass().getDeclaredMethod(rowContent.get(3));
            target.invoke(api);
            return;
        } catch (Exception e) {
        }
        //尝试查找方法名对应的，有1个参数的方法
        try {
            Method target = api.getClass().getDeclaredMethod(rowContent.get(3),String.class);
            target.invoke(api,rowContent.get(4));
            return;
        } catch (Exception e) {
        }
        //尝试查找方法名对应的，有2个参数的方法
        try {
            Method target = api.getClass().getDeclaredMethod(rowContent.get(3),String.class,String.class);
            target.invoke(api,rowContent.get(4),rowContent.get(5));
            return;
        } catch (Exception e) {
        }
        //尝试查找方法名对应的，有3个参数的方法
        try {
            Method target = api.getClass().getDeclaredMethod(rowContent.get(3),String.class,String.class);
            target.invoke(api,rowContent.get(4),rowContent.get(5),rowContent.get(6));
            return;
        } catch (Exception e) {
        }



    }
    //反射调用断言方法
    public static void invokeAssert(Object api,List<String> rowContent){
        //查找有没有符合关键字名的方法，从0个开始查找
        try {
            Method target = api.getClass().getDeclaredMethod(rowContent.get(7));
            target.invoke(api);
            return;
        } catch (Exception e) {
        }
        //尝试查找方法名对应的，有1个参数的方法
        try {
            Method target = api.getClass().getDeclaredMethod(rowContent.get(7),String.class);
            target.invoke(api,rowContent.get(8));
            return;
        } catch (Exception e) {
        }
        //尝试查找方法名对应的，有2个参数的方法
        try {
            Method target = api.getClass().getDeclaredMethod(rowContent.get(7),String.class,String.class);
            target.invoke(api,rowContent.get(8),rowContent.get(9));
            return;
        } catch (Exception e) {
        }
    }
}
