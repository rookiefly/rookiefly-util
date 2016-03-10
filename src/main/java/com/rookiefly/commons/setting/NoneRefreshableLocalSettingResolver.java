package com.rookiefly.commons.setting;

/**
 * 不支持刷新的配置解析器(某些特定的情况下,不希望每次都刷新配置,此时可以使用此接口)
 * 
 * @author rookiefly
 * 
 */
public interface NoneRefreshableLocalSettingResolver<T> extends LocalSettingResolver<T> {

}
