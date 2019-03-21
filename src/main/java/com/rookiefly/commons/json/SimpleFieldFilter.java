package com.rookiefly.commons.json;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;

import java.util.HashSet;
import java.util.Set;

public class SimpleFieldFilter implements PropertyPreFilter {

    private Class<?> clazz;

    private final Set<String> includes = new HashSet<String>();

    private final Set<String> excludes = new HashSet<String>();

    public SimpleFieldFilter() {
    }

    public SimpleFieldFilter(Class<?> clazz, String... excludesfields) {
        this.clazz = clazz;
        for (String item : excludesfields) {
            if (item != null) {
                this.excludes.add(item);
            }
        }
    }

    public SimpleFieldFilter(String[] includesfields, Class<?> clazz) {
        this.clazz = clazz;
        for (String item : includesfields) {
            if (item != null) {
                this.includes.add(item);
            }
        }
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Set<String> getIncludes() {
        return includes;
    }

    public Set<String> getExcludes() {
        return excludes;
    }

    /**
     * @see com.alibaba.fastjson.serializer.PropertyFilter#apply(Object, String, Object)
     */
    @Override
    public boolean apply(JSONSerializer serializer, Object source, String name) {
        if (source == null) {
            return true;
        }

        if (clazz != null && !clazz.isInstance(source)) {
            return true;
        }

        if (this.excludes.contains(name)) {
            return false;
        }

        if (includes.size() == 0 || includes.contains(name)) {
            return true;
        }

        return false;
    }

}