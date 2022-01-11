package com.smart.auth.security.exception;

import com.smart.commons.core.i18n.I18nException;
import com.smart.commons.core.i18n.I18nMessage;

/**
 * @author ShiZhongMing
 * 2021/6/30 13:09
 * @since 1.0
 */
public class PasswordChangeException extends I18nException {
    private static final long serialVersionUID = -8106965686005578575L;

    public PasswordChangeException(Throwable e) {
        super(e);
    }

    public PasswordChangeException(I18nMessage i18nMessage, Throwable e) {
        super(i18nMessage, e);
    }

    public PasswordChangeException(I18nMessage i18nMessage) {
        super(i18nMessage);
    }
}
