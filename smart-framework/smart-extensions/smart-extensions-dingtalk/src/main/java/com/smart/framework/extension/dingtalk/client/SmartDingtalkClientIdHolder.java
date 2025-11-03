package com.smart.framework.extension.dingtalk.client;

/**
 * 基于ThreadLocal的钉钉应用ID持有者
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/1 22:11
 * @since 5.0.0
 */
public class SmartDingtalkClientIdHolder {

    private static final ThreadLocal<String> CLIENT_ID_HOLDER = ThreadLocal.withInitial(() -> "default");

    public static String get() {
        return CLIENT_ID_HOLDER.get();
    }

    /**
     * 设置当前线程的钉钉应用ID
     * @param clientId 钉钉应用ID
     */
    public static void set(String clientId) {
        CLIENT_ID_HOLDER.set(clientId);
    }

     /**
      * 清除当前线程的钉钉应用ID
      */
    public static void clear() {
        CLIENT_ID_HOLDER.remove();
    }
}
