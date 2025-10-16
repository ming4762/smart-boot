package com.smart.framework.commons.core.json;

/**
 * 基础枚举接口
 * @author shizhongming
 * 2025/10/16 10:45
 * @since 5.0.0
 */
public interface BaseEnum<T> {

    /**
     * 获取枚举值
     * @return 枚举值
     */
    T getValue();
}
