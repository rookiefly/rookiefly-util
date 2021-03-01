package com.rookiefly.test.commons.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelTest {

    public static void main(String[] args) throws Exception {

        //ExportExcel<Student> ex = new ExportExcel<Student>();
        //String title = "学生信息导出表";
        //String[] headers = { "学号", "姓名", "年龄", "性别", "出生日期" };
        List<Student> dataset = new ArrayList<Student>();
        dataset.add(new Student(10000001, "张三", 20, true, new Date()));
        dataset.add(new Student(20000002, "李四", 24, false, new Date()));
        dataset.add(new Student(30000003, "王五", 22, true, new Date()));

        OutputStream out = new FileOutputStream("student.xls");

        ExportParams exportParams = new ExportParams();
        exportParams.setSheetName("学生信息");
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, Student.class, dataset);

        workbook.write(out);
    }

}
