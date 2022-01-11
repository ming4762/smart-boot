package com.smart.commons.core.utils;


import org.springframework.lang.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64 编解码工具
 * @author shizhongming
 * 2020/1/8 8:40 下午
 */
public final class Base64Utils {

    /**
     * 解码工具
     */
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    /**
     * 转码工具
     */
    private static final Base64.Encoder ENCODER = Base64.getEncoder();

    private Base64Utils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * base64编码
     * @param str 要编码的字符串
     * @return 编码后的字符串
     */
    @NonNull
    public static String encoder(@NonNull String str) {
        final byte[] textByte = str.getBytes(StandardCharsets.UTF_8);
        return encoder(textByte);
    }

    /**
     * base64编码
     * @param bytes 要编码的字节数组
     * @return 编码后的字符串
     */
    @NonNull
    public static String encoder(@NonNull byte[] bytes) {
        return ENCODER.encodeToString(bytes);
    }

    /**
     * base64解码
     * @param str 要解码的字符串
     * @return 解码后的字符串
     */
    @NonNull
    public static String decoder(@NonNull String str) {
        return new String(DECODER.decode(str), StandardCharsets.UTF_8);
    }
}
