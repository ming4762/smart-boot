package com.smart.message.manager.constants;

import com.smart.commons.core.i18n.I18nMessage;

/**
 * @author zhongming4762
 * 2023/5/26
 */
public enum SmartSmsI18nMessageEnum implements I18nMessage {

    /**
     * 短信模块国际化信息
     */
    ERROR_SAVE_MULTI_DEFAULT("smart.sms.validate.multiDefault", "只能有一个默认项")
    ;

    private final String i18nCode;

    private final String defaultMessage;

    SmartSmsI18nMessageEnum(String i18nCode, String defaultMessage) {
        this.i18nCode = i18nCode;
        this.defaultMessage = defaultMessage;
    }

    /**
     * 获取I18N key
     *
     * @return key
     */
    @Override
    public String getI18nCode() {
        return this.i18nCode;
    }

    /**
     * 获取默认的信息
     *
     * @return 默认
     */
    @Override
    public String defaultMessage() {
        return this.defaultMessage;
    }
}
