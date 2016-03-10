package com.rookiefly.test.commons.excel;

import com.rookiefly.commons.excel.ExcelResources;

import java.util.Date;

public class Student {
 
   private long id;
   private String name;
   private int age;
   private boolean sex;
   private Date birthday;
 
   public Student() {
      super();
   }
 
   public Student(long id, String name, int age, boolean sex, Date birthday) {
      super();
      this.id = id;
      this.name = name;
      this.age = age;
      this.sex = sex;
      this.birthday = birthday;
   }

   @ExcelResources(title="学生ID", order = 10)
   public long getId() {
      return id;
   }
 
   public void setId(long id) {
      this.id = id;
   }

   @ExcelResources(title="学生姓名", order = 9)
   public String getName() {
      return name;
   }
 
   public void setName(String name) {
      this.name = name;
   }

   @ExcelResources(title="学生年龄", order = 8)
   public int getAge() {
      return age;
   }
 
   public void setAge(int age) {
      this.age = age;
   }
 
   public boolean getSex() {
      return sex;
   }
 
   public void setSex(boolean sex) {
      this.sex = sex;
   }
 
   public Date getBirthday() {
      return birthday;
   }
 
   public void setBirthday(Date birthday) {
      this.birthday = birthday;
   }
 
}