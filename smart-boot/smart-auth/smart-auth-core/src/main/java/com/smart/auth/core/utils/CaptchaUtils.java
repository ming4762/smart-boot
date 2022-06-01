package com.smart.auth.core.utils;

import com.smart.auth.core.exception.CaptchaException;
import com.smart.auth.core.i18n.AuthI18nMessage;
import com.smart.auth.core.service.AuthCache;
import com.smart.commons.core.i18n.I18nUtils;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.awt.*;
import java.io.OutputStream;
import java.time.Duration;

/**
 * 验证码工具类
 * @author ShiZhongMing
 * 2021/6/2 14:16
 * @since 1.0
 */
public class CaptchaUtils {

    private static final String PREFIX = "captcha";

    private static final int DEFAULT_WIDTH = 130;

    private static final int DEFAULT_HEIGHT = 48;

    private static final int DEFAULT_LENGTH = 5;

    private static final int DEFAULT_TYPE = Captcha.TYPE_DEFAULT;

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5L * 60L);

    private static final Font DEFAULT_FONT = new Font("Verdana", Font.PLAIN, 32);

    private CaptchaUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 生成验证码
     * @param outputStream 输出流
     * @param key key
     * @param authCache 缓存
     */
    public static void out(@NonNull OutputStream outputStream, @NonNull String key, @NonNull AuthCache<String, Object> authCache) {
        out(outputStream, key, authCache, DEFAULT_FONT, DEFAULT_TYPE, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_LENGTH, DEFAULT_TIMEOUT);
    }

    /**
     * 生成验证码
     * @param outputStream 输出流
     * @param key key
     * @param authCache 缓存
     * @param font 字体
     * @param type 类型
     * @param width 宽
     * @param height 高
     * @param length 长度
     * @param timeout 超时时间
     */
    public static void out(@NonNull OutputStream outputStream, @NonNull String key, @NonNull AuthCache<String, Object> authCache, @Nullable Font font, int type, int width, int height, int length, Duration timeout) {
        final SpecCaptcha specCaptcha = new SpecCaptcha(width, height, length);
        specCaptcha.setFont(font);
        specCaptcha.setCharType(type);
        // 放入缓存
        authCache.put(getKey(key), specCaptcha.text().toLowerCase(), timeout);
        // 写入输出流
        specCaptcha.out(outputStream);
    }

    /**
     * 验证验证码
     * @param key key
     * @param code 验证码
     * @param authCache 缓存
     * @return 是否验证成功
     */
    public static boolean validate(@NonNull String key, @NonNull String code, @NonNull AuthCache<String, Object> authCache) {
        // 判断是否过期
        final String cacheCode = (String) authCache.get(getKey(key));
        if (cacheCode == null) {
            throw new CaptchaException(I18nUtils.get(AuthI18nMessage.CAPTCHA_EXPIRE_ERROR));
        }
        return StringUtils.equals(code.toLowerCase(), cacheCode);
    }

    /**
     * 使验证码失效
     * @param key 验证码的key
     * @param authCache 缓存类
     */
    public static void invalid(@NonNull String key, @NonNull AuthCache<String, Object> authCache) {
        authCache.remove(getKey(key));
    }

    /**
     * 获取key
     * @param ident 标识位
     * @return 带前缀的key
     */
    private static String getKey(String ident) {
        return PREFIX + "&$" + ident;
    }
}
