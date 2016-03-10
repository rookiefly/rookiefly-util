package com.rookiefly.commons.setting;

import java.util.Properties;

/**
 * 本地配置解析器,负责实现将字符串解析成一个配置对象
 *
 * @param <T>
 * @author rookiefly
 */
public interface LocalSettingResolver<T> {
    /**
     * 将属性文件中的属性解析成配置对象
     *
     * @param prop 属性文件中的属性
     * @return 解析后的配置对象
     */
    T resolveSettings(Properties prop);
}
