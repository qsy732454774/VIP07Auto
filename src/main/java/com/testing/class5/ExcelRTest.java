package com.testing.class5;

import com.testing.common.ExcelReader;
import com.testing.common.ExcelWriter;
import com.testing.web.WebKeyword;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExcelRTest {
    public static void main(String[] args) {
        WebKeyword web=new WebKeyword();
        ExcelReader cases=new ExcelReader("Cases/WebCases.xlsx");
        //获取当前时间
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String nowtime=sdf.format(date);
        //构造方法复制一份用例文件，生成结果文件。
        ExcelWriter results=new ExcelWriter("Cases/WebCases.xlsx","Cases/ResultofWeb"+nowtime+".xlsx");
        //先遍历sheet页
        for (int sheetNo = 0; sheetNo < cases.getTotalSheetNo(); sheetNo++) {
            //使用当前sheet页
            cases.useSheetByIndex(sheetNo);
            results.useSheetByIndex(sheetNo);
            //遍历sheet页每一行
            for(int rowNo=0;rowNo<cases.getRowNo();rowNo++){
                //读取当前行内容为list
                List<String> rowContent=cases.readLine(rowNo);
                System.out.println(rowContent);
                //基于一行内容执行用例
                //第一列和第二列都为空，是用例行，要执行
                if((rowContent.get(0).equals("")||rowContent.get(0).trim().length()<1)&&
                        (rowContent.get(1).equals("")||rowContent.get(1).trim().length()<1)){
                    switch(rowContent.get(3)){
                        case "openBrowser":
                        case "打开浏览器":
                            web.openBrowser(rowContent.get(4));
                            results.writeCell(rowNo,10,"Pass");
                            break;
                        case "visitWeb":
                            web.visitURL(rowContent.get(4));
                            results.writeCell(rowNo,10,"Pass");
                            break;
                        case "input":
                            web.input(rowContent.get(4),rowContent.get(5));
                            results.writeCell(rowNo,10,"Pass");
                            break;
                        case "click":
                            boolean result=web.click(rowContent.get(4));
                            //判断input执行的最终结果，如果是成功就往第11列写pass，否则写fail
                            if(result){
                                //写成功
                                results.writeCell(rowNo,10,"Pass");
                            }
                            else{
                                //写失败。
                                results.writeFailCell(rowNo,10,"Fail");
                            }
                            break;

                        case "intoIframe":
                        case "switchIframebyele":
                            web.switchIframe(rowContent.get(4));
                            results.writeCell(rowNo,10,"Pass");
                            break;
                        case "halt":
                            web.halt(rowContent.get(4));
                            results.writeCell(rowNo,10,"Pass");
                            break;
                        case "selectByValue":
                            web.selectByValue(rowContent.get(4),rowContent.get(5));
                            results.writeCell(rowNo,10,"Pass");
                            break;
                        case "closeBrowser":
                            web.closeBrowser();
                            results.writeCell(rowNo,10,"Pass");
                            break;
                        case "assertEleTextContains":
                            web.getEleText(rowContent.get(4),rowContent.get(5));
                            results.writeCell(rowNo,10,"Pass");
                            break;
                    }
                }
            }
        }
        cases.close();
        //调用save方法。
        results.save();
    }
}
