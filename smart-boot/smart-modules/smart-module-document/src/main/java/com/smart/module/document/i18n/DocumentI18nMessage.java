package com.smart.module.document.i18n;

import com.smart.commons.core.i18n.I18nMessage;

/**
 * @author ShiZhongMing
 * 2022/5/18
 * @since 2.0.0
 */
public enum DocumentI18nMessage implements I18nMessage {

    /**
     * 没有找到模板编码
     */
    TEMPLATE_CODE_NOT_FOUND("document_templateCode_not_found", "No template found, please check whether the template code is correct, code: {0}")
    ;

    private final String i18nCode;

    private final String defaultMessage;

    DocumentI18nMessage(String i18nCode, String defaultMessage) {
        this.i18nCode = i18nCode;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getI18nCode() {
        return this.i18nCode;
    }

    @Override
    public String defaultMessage() {
        return this.defaultMessage;
    }
}
