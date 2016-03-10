package com.rookiefly.commons.setting;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 本地配置工具类,可以实现在不用重启服务的情况下更改本地配置(通过配置文件)
 *
 * @author rookiefly
 *         <p/>
 */
public class LocalSettings {
    private static Logger logger = Logger
            .getLogger(LocalSettings.class);
    private static volatile Properties settings;
    @SuppressWarnings("rawtypes")
    private static final ConcurrentHashMap<String, LocalSettingResolver> RESOLVERS = new ConcurrentHashMap<String, LocalSettingResolver>();
    private static final ConcurrentHashMap<String, Object> resolvedObjs = new ConcurrentHashMap<String, Object>();
    private static final ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(
                    1,
                    new ThreadFactoryBuilder(
                            "Scheduled-LocalSettings-scheduler").build());

    static {
        refreshSettings();
        int refreshInterval = getInt("LocalSettings.refresh_interval", 5);
        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                refreshSettings();
            }
        }, refreshInterval, refreshInterval, TimeUnit.SECONDS);
        if (logger.isDebugEnabled()) {
            logger.debug("LocalSettings will be refreshed at interval " + refreshInterval);
        }
    }

    /**
     * 获取配置
     *
     * @param key          键
     * @param defaultValue 若键值不存在时需要返回的值
     * @return 键相关联的值, 若不存在, 则返回defaultValue
     */
    public static String getString(String key, String... defaultValue) {
        String val = settings == null ? null : (String) settings.get(key);
        if (val == null && defaultValue != null && defaultValue.length > 0) {
            val = defaultValue[0];
        }
        return val;
    }

    /**
     * 获取配置
     *
     * @param key          键
     * @param defaultValue 若键值不存在时需要返回的值
     * @return 键相关联的值, 若不存在, 则返回defaultValue
     */
    public static boolean getBoolean(String key, boolean... defaultValue) {
        String val = getString(key);
        if (val == null) {
            if (defaultValue != null && defaultValue.length > 0) {
                return defaultValue[0];
            }
            return false;
        }
        return Boolean.valueOf(val);
    }

    /**
     * 获取配置
     *
     * @param key          键
     * @param defaultValue 若键值不存在时需要返回的值
     * @return 键相关联的值, 若不存在, 则返回defaultValue
     */
    public static int getInt(String key, int... defaultValue) {
        String val = getString(key);
        if (val == null) {
            if (defaultValue != null && defaultValue.length > 0) {
                return defaultValue[0];
            }
            return 0;
        }
        return Integer.valueOf(val);
    }

    /**
     * 获取配置
     *
     * @param key          键
     * @param defaultValue 若键值不存在时需要返回的值
     * @return 键相关联的值, 若不存在, 则返回defaultValue
     */
    public static long getLong(String key, long... defaultValue) {
        String val = getString(key);
        if (val == null) {
            if (defaultValue != null && defaultValue.length > 0) {
                return defaultValue[0];
            }
            return 0;
        }
        return Long.valueOf(val);
    }

    public static <T> T getConfig(String key, LocalSettingResolver<T> resolver) {
        if (resolver instanceof NoneRefreshableLocalSettingResolver) {
            // 并发情况下可能会重复解析,但可以接受这种情况
            @SuppressWarnings("unchecked")
            T config = (T) resolvedObjs.get(key);
            if (config == null) {
                config = resolver.resolveSettings(settings);
                resolvedObjs.put(key, config);
            }
            return config;
        } else {// 下次数刷新时生效
            RESOLVERS.putIfAbsent(key, resolver);
            @SuppressWarnings("unchecked")
            T config = (T) resolvedObjs.get(key);
            return config;
        }
    }

    /**
     * 刷新配置
     */
    @SuppressWarnings("rawtypes")
    private static void refreshSettings() {
        String confFile = "/localsettings.conf";
        InputStream is = ClassUtil.getResourceAsStream(LocalSettings.class, confFile);
        if (is == null) {
            confFile = "/config/localsettings.conf";
            is = ClassUtil.getResourceAsStream(LocalSettings.class, confFile);
            if (is == null) {
                logger.warn("no config file,no LocalSettings will be avaiable!!");
                return;
            }
        }
        Properties tmpProps = new Properties();
        try {
            tmpProps.load(new InputStreamReader(is, "UTF-8"));
        } catch (IOException e) {
            logger.error("error load settings from " + confFile, e);
        } finally {
            try {
                is.close();
            } catch (IOException e2) {
                logger.error("error close file " + confFile);
            }
        }
        settings = tmpProps;
        // resolve configs
        for (Entry<String, LocalSettingResolver> e : RESOLVERS.entrySet()) {
            String key = e.getKey();
            LocalSettingResolver resolver = e.getValue();
            if (resolver instanceof NoneRefreshableLocalSettingResolver) {
                continue;
            }
            try {
                Object config = resolver.resolveSettings(tmpProps);
                if (config != null) {
                    resolvedObjs.put(key, config);
                }
            } catch (Throwable ex) {
                logger.warn("fail to resolve config for key:" + key, ex);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("LocalSettings has been refreshed,new settings is:" + settings);
        }
    }
}
