package com.smart.smc.inter.qingdaoport.support;

import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 持有客户编码、私钥等客户信息
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-05 15:50
 * @since 5.0.0
 */
public class QingdaoPortCustomHolder {
    /**
     * 私有化构造方法，防止实例化
     */
    private QingdaoPortCustomHolder() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static final String DEFAULT_CUSTOMER_CODE = "default";

    /**
     * 客户编码到客户信息的映射
     */
    private static final Map<String, QingdaoPortCustomerData> CUSTOMER_HOLDER_MAP = new ConcurrentHashMap<>(10);

    /**
     * 当前线程正在使用的客户编码
     */
    private static final ThreadLocal<String> CURRENT_CUSTOMER_CODE = ThreadLocal.withInitial(() -> DEFAULT_CUSTOMER_CODE);

    /**
     * 设置当前线程正在使用的客户编码
     * @param customerCode 客户编码
     */
    public static void set(String customerCode) {
        if (!CUSTOMER_HOLDER_MAP.containsKey(customerCode)) {
            throw new IllegalArgumentException("Customer code not found: " + customerCode);
        }
        CURRENT_CUSTOMER_CODE.set(customerCode);
    }

    /**
     * 清除当前线程正在使用的客户编码
     */
    public static void clear() {
        CURRENT_CUSTOMER_CODE.remove();
    }

    /**
     * 获取当前线程正在使用的客户信息
     * @return 客户信息
     */
    @Nullable
    public static QingdaoPortCustomerData get() {
        if (CUSTOMER_HOLDER_MAP.isEmpty()) {
            return null;
        }
        if (CUSTOMER_HOLDER_MAP.size() == 1) {
            return CUSTOMER_HOLDER_MAP.values().iterator().next();
        }
        if (DEFAULT_CUSTOMER_CODE.equals(CURRENT_CUSTOMER_CODE.get())) {
            return CUSTOMER_HOLDER_MAP.values().stream()
                    .filter(QingdaoPortCustomerData::defaultYn)
                    .findFirst()
                    .orElse(null);
        }
        return CUSTOMER_HOLDER_MAP.get(CURRENT_CUSTOMER_CODE.get());
    }

    /**
     * 添加客户信息
     * @param customerData 客户信息
     */
    public static void add(QingdaoPortCustomerData customerData) {
        CUSTOMER_HOLDER_MAP.put(customerData.customerCode(), customerData);
    }

}
