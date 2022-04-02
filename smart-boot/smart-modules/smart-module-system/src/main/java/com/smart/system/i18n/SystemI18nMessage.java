package com.smart.system.i18n;

import com.smart.commons.core.i18n.I18nMessage;

/**
 * TODO:国际化
 * @author ShiZhongMing
 * 2021/11/19
 * @since 1.0.7
 */
public enum SystemI18nMessage implements I18nMessage {
    /**
     * 系统国际化页面国际化信息
     */
    I18N_ITEM_DUPLICATE("system.i18n.item.duplicate", "I18N Item Locale Duplicate, key: {0}"),
    I18N_DUPLICATE("system.i18n.code.duplicate", "I18N Code Duplicate, key: {0}"),
    SYSTEM_ACCOUNT_EXIST_ERROR("system.user.account.exist.error", "Account cannot be created repeatedly, user: {0}"),
    SYSTEM_ACCOUNT_HAS_DELETE_ERROR("system.user.account.delete", "Deleted user cannot create account, deleted user: {0}"),
    SYSTEM_ACCOUNT_HAS_NO_USE_ERROR("system.user.account.noUse", "Disabled user cannot create account, disabled user: {0}")
    ;

    private final String code;

    private final String defaultMessage;

    SystemI18nMessage(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getI18nCode() {
        return this.code;
    }

    @Override
    public String defaultMessage() {
        return this.defaultMessage;
    }
}
