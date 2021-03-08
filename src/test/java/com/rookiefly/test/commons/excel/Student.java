package com.rookiefly.test.commons.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@ExcelTarget("studentEntity")
@Data
@AllArgsConstructor
public class Student {

    @Excel(name = "id", orderNum = "1")
    private long id;

    @Excel(name = "名称", orderNum = "2")
    private String name;

    @Excel(name = "年龄", orderNum = "3")
    private int age;

    @Excel(name = "性别", orderNum = "4")
    private boolean sex;

    @Excel(name = "生日", orderNum = "5", exportFormat = "yyyy-MM-dd")
    private Date birthday;
}