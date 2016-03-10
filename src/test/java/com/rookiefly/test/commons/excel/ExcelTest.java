package com.rookiefly.test.commons.excel;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rookiefly.commons.excel.ExcelUtil;
import com.rookiefly.commons.excel.ExportExcel;

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
        //ex.exportExcel(title, headers, dataset, out);
        ExcelUtil.getInstance().exportObj2Excel(out, dataset, Student.class, false);
    }

}
