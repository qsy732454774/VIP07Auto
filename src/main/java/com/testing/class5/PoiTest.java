package com.testing.class5;

import com.testing.web.WebKeyword;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PoiTest {
    public static void main(String[] args) throws IOException, InvalidFormatException {
       //创建关键字类进行调用。
        WebKeyword web=new WebKeyword();
        //打开excel文件
        Workbook wb=new XSSFWorkbook(new File("E:\\VIPclassnew\\2自动化课程教案\\Java自动化第5课-PO模式与Web自动化数据驱动框架实现\\课程案例\\WebCases.xlsx"));
        //使用指定的sheet页，使用第一个
        Sheet sheetOne= wb.getSheetAt(0);
        //这个list用于存储sheet页中所有行的信息
        List<List<String>>  sheetcontent=new ArrayList<>();
        //遍历sheet页中的所有行
//        System.out.println("总行数"+sheetOne.getPhysicalNumberOfRows());
        for(int rowNo=0;rowNo<sheetOne.getPhysicalNumberOfRows();rowNo++) {
            //获取指定行
            Row row = sheetOne.getRow(rowNo);
            //这个list用于存储每行的信息
            List<String> rowcontent=new ArrayList<>();
//            System.out.println("第"+rowNo+"行的长度是"+row.getPhysicalNumberOfCells());
            for(int colNo=0;colNo<row.getPhysicalNumberOfCells();colNo++) {

                //获取指定行中的指定单元格
                Cell cell = row.getCell(colNo);
                //从单元格获取内容
                String cellValue="";
                //如果单元格内容是数字
                if(cell.getCellType().equals(CellType.NUMERIC)){
                    cellValue=cell.getNumericCellValue()+"";
                }
                else {
                  cellValue=cell.getStringCellValue();
                }

                //把每一个单元格的内容加到行内容中
                rowcontent.add(cellValue);
//                System.out.println(cell2Value);
            }
            System.out.println("第"+rowNo+"行的内容是"+rowcontent);
            switch(rowcontent.get(3)){
                case "打开浏览器":
                    web.openBrowser(rowcontent.get(4));
                    break;
                case "visitWeb":
                    web.visitURL(rowcontent.get(4));
                    break;
                case "input":
                    web.input(rowcontent.get(4),rowcontent.get(5));
                    break;
                case "click":
                    web.click(rowcontent.get(4));
                    break;
            }

            //把每一行的内容加到sheet页里面
            sheetcontent.add(rowcontent);
        }

        System.out.println("sheet页中所有内容是"+sheetcontent);

    }
}
