package com.rookiefly.commons.setting;

import java.io.InputStream;
import java.net.URL;

/**
 * 资源工具类
 *
 * @author rookiefly
 */
public class ClassUtil {
    /**
     * 获取classpath下的资源
     *
     * @param cls  class类
     * @param path 资源文件路径
     * @return 资源路径的输入流, 若不存在, 则返回null
     */
    @SuppressWarnings("rawtypes")
    public static InputStream getResourceAsStream(Class cls, String path) {
        if (path == null) {
            return null;
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream is = loader.getResourceAsStream(path);
        if (is == null && cls != null) {
            is = cls.getResourceAsStream(path);
        }
        return is;
    }

    public static URL getResource(String path) {
        if (path == null) {
            return null;
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL resource = null;
        resource = loader.getResource(path);
        return resource;
    }

    @SuppressWarnings("rawtypes")
    public static Class getClass(String loadClassName) {
        if (loadClassName == null) {
            return null;
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            return loader.loadClass(loadClassName);
        } catch (ClassNotFoundException e) {
        }
        return null;
    }

}
