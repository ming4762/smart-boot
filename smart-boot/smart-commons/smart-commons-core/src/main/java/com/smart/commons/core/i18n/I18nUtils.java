package com.smart.commons.core.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * I18N工具类
 * @author shizhongming
 * 2020/5/13 11:03 上午
 */
public class I18nUtils {

    private static MessageSource messageSource;

    private I18nUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 通过key获取I18N信息
     * @param key key
     * @return I18N信息
     */
    public static String get(@NonNull String key, String defaultMessage) {
        if (supportI18n()) {
            return get(key);
        }
        return defaultMessage;
    }
    public static String get(@NonNull String key, Locale locale, Object ...args) {
        validate();
        return messageSource.getMessage(key, args, locale);
    }

    public static String get(@NonNull String key, Object ...args) {
        validate();
        return get(key, LocaleContextHolder.getLocale(), args);
    }

    /**
     * 通过Key获取I18N信息
     * @param key key
     * @return I18N信息
     */
    public static String get(@NonNull String key) {
        validate();
        return get(key, LocaleContextHolder.getLocale());
    }

    /**
     * 获取I18N信息
     * @param i18nMessage I18nMessage
     * @return I18N
     */
    public static String get(@NonNull I18nMessage i18nMessage) {
        return doGet(i18nMessage, () -> messageSource.getMessage(i18nMessage.getI18nCode(), null, i18nMessage.defaultMessage(), LocaleContextHolder.getLocale()));
    }


    /**
     * 获取I18N信息
     * @param i18nMessage I18nMessage
     * @return I18N
     */
    public static String get(@NonNull I18nMessage i18nMessage, Object ...args) {
        return get(i18nMessage, args, LocaleContextHolder.getLocale());
    }

    /**
     * 获取I18N信息
     * @param i18nMessage I18nMessage
     * @return I18N
     */
    public static String get(@NonNull I18nMessage i18nMessage, Object[] args, Locale locale) {
        return doGet(i18nMessage, () -> messageSource.getMessage(i18nMessage.getI18nCode(), args, i18nMessage.defaultMessage(), locale));
    }

    private static String doGet(@NonNull I18nMessage i18nMessage, Supplier<String> handler) {
        if (messageSource == null) {
            if (i18nMessage.defaultMessage() != null) {
                return i18nMessage.defaultMessage();
            }
            throw new IllegalArgumentException("messageSource cannot be null");
        }
        return handler.get();
    }

    /**
     * 判断是否支持I18N
     * @return 是否支持I18N
     */
    public static boolean supportI18n() {
       return Objects.nonNull(messageSource) && !(messageSource instanceof DelegatingMessageSource  delegatingMessageSource && Objects.isNull(delegatingMessageSource.getParentMessageSource()));
    }

    /**
     * 获取I18N MessageSource
     * @return MessageSource
     */
    public static MessageSource getMessageSource() {
        return messageSource;
    }

    private static void validate() {
        Assert.notNull(messageSource, "messageSource cannot be null");
    }

    public static void setMessageSource(MessageSource messageSource) {
        I18nUtils.messageSource = messageSource;
    }
}
