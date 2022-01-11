package com.smart.commons.core.i18n;

import java.io.Serializable;

/**
 * I18N message接口
 * @author shizhongming
 * 2021/1/24 10:42 上午
 */
public interface I18nMessage extends Serializable {

    /**
     * 获取I18N key
     * @return key
     */
    String getI18nCode();

    /**
     * 获取默认的信息
     * @return 默认
     */
    default String defaultMessage() {
        return null;
    }
}
