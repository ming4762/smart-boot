package com.smart.starter.exception.processor;

import com.smart.commons.core.i18n.I18nMessage;
import com.smart.commons.core.i18n.I18nUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * I18N异常信息处理其
 * @author ShiZhongMing
 * 2021/3/3 12:44
 * @since 1.0
 */
public abstract class AbstractI18nExceptionMessageProcessor<T extends Exception> extends AbstractTypeExceptionMessageProcessor<T> {

    /**
     * 获取I18N信息
     * @param code I18N编码
     * @return I18N信息
     */
    protected String i18nMessage(String code, String defaultMessage, Object ...args) {
        if (I18nUtils.supportI18n() && StringUtils.isBlank(code)) {
            return I18nUtils.get(code, args);
        }
        return defaultMessage;
    }

    /**
     * 获取I18N信息
     * @param message I18N编码
     * @return I18N信息
     */
    protected String i18nMessage(I18nMessage message, String defaultMessage, Object ...args) {
        if (I18nUtils.supportI18n() && Objects.nonNull(message)) {
            return I18nUtils.get(message, defaultMessage, args);
        }
        return defaultMessage;
    }
}
