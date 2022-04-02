package com.smart.commons.core.i18n;

import com.smart.commons.core.exception.BusinessException;
import lombok.Getter;

/**
 * 支持I18n 的异常信息
 * @author shizhongming
 * 2021/1/24 10:39 上午
 */
@Getter
public class I18nException extends BusinessException {
    private static final long serialVersionUID = 7336029237493468850L;

    private final I18nMessage i18nMessage;

    private Object[] args;

    public I18nException(Throwable e) {
        super(e);
        this.i18nMessage = null;
    }

    public I18nException(I18nMessage i18nMessage, Throwable e) {
        super(e);
        this.i18nMessage = i18nMessage;
    }

    public I18nException(I18nMessage i18nMessage, Throwable e, Object ...args) {
        this(i18nMessage, e);
        this.args = args;
    }

    public I18nException(I18nMessage i18nMessage, Object ...args) {
        this.i18nMessage = i18nMessage;
        this.args = args;
    }

    /**
     * 抛出异常
     * @param message 异常信息
     */
    public static void of(I18nMessage message) {
        throw new I18nException(message);
    }
    public static void of(I18nMessage message, Throwable e) {
        throw new I18nException(message, e);
    }
}
