package com.rookiefly.commons.json;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import java.util.ArrayList;
import java.util.List;

public class SerializeFilterBuilder {

    List<SerializeFilter> filters = new ArrayList<>();

    SerializeFilterBuilder() {
    }

    public static SerializeFilterBuilder create() {
        return new SerializeFilterBuilder();
    }

    /**
     * 添加class中序列化的字段，fastjson中支持的SerializeFilter
     *
     * @param clazz
     * @param fields
     * @return
     */
    public SerializeFilterBuilder addSerializeFilter(Class<?> clazz, String... fields) {

        filters.add(new SimplePropertyPreFilter(clazz, fields));
        return this;
    }

    public SerializeFilterBuilder addExcludeFilter(Class<?> clazz, String... excludesfields) {
        filters.add(new SimpleFieldFilter(clazz, excludesfields));
        return this;
    }

    public SerializeFilterBuilder addIncludeFilter(Class<?> clazz, String... includesfields) {
        filters.add(new SimpleFieldFilter(includesfields, clazz));
        return this;
    }

    public SerializeFilter[] build() {

        SerializeFilter[] result = new SerializeFilter[filters.size()];
        filters.toArray(result);

        return result;
    }

    public static SerializeFilter[] build(Class<?> clazz, String... fields) {

        SerializeFilterBuilder builder = SerializeFilterBuilder.create();
        builder.addSerializeFilter(clazz, fields);
        return builder.build();
    }

    public static SerializeFilter newFilter(Class<?> clazz, String... fields) {
        return new SimplePropertyPreFilter(clazz, fields);
    }

    public static SerializeFilter newExcludeFilter(Class<?> clazz, String... excludesfields) {
        return new SimpleFieldFilter(clazz, excludesfields);
    }

    public static SerializeFilter newIncludeFilter(Class<?> clazz, String... includesfields) {
        return new SimpleFieldFilter(includesfields, clazz);
    }

}