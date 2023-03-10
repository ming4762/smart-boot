package com.smart.cloud.common.feign.utils;

/**
 * @author zhongming4762
 * 2023/3/10
 */
public class TokenHolder {


    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    public static String get() {
        return THREAD_LOCAL.get();
    }

    public static void set(String token) {
        THREAD_LOCAL.remove();
        THREAD_LOCAL.set(token);
    }
}
