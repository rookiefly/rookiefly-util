package com.rookiefly.commons.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来在对象的get方法上加入的annotation，通过该annotation说明某个属性所对应的标题
 * @author wanggaoixang
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ExcelResources {
    /**
     * 属性的标题名称
     * @return
     */
    String title();

    /**
     * 在excel的顺序
     * Lower values have higher priority.
     * @return
     */
    int order() default 9999;
}
