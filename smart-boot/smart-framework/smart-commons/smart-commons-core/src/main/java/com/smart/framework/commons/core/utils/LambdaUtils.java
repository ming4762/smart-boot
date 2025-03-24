package com.smart.framework.commons.core.utils;

import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.commons.core.utils.lambda.SFunction;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * lambda工具类
 * @author shizhongming
 * 2025/3/24 9:37
 * @since 5.0.0
 */
public class LambdaUtils {

    private LambdaUtils() {
        throw new UnsupportedOperationException();
    }

    private static final String GETTER_PREFIX = "get";
    private static final String IS_PREFIX = "is";

    private static final Map<String, SerializedLambda> LAMBDA_CACHE = new ConcurrentHashMap<>(10);

    /**
     * 获取属性名称
     * @param func 函数式接口
     * @param <T> 类型
     * @return 属性名称
     */
    public static <T> String getPropertyName(SFunction<T, ?> func) {
        SerializedLambda serializedLambda = LAMBDA_CACHE.computeIfAbsent(func.getClass().getName(), key -> {
            try {
                Method writeReplace = func.getClass().getDeclaredMethod("writeReplace");
                writeReplace.setAccessible(true);
                return (SerializedLambda) writeReplace.invoke(func);
            } catch (Exception e) {
                throw new SystemException(e);
            }
        });
        String implMethodName = serializedLambda.getImplMethodName();
        // 如果方法名以 "get" 开头，进行处理，去除 "get" 并将首字母小写
        if (implMethodName.startsWith(GETTER_PREFIX)) {
            String property = implMethodName.substring(GETTER_PREFIX.length());
            return Character.toLowerCase(property.charAt(0)) + property.substring(1);
        }
        // 如果是布尔类型的方法，比如 isXxx
        if (implMethodName.startsWith(IS_PREFIX)) {
            String property = implMethodName.substring(IS_PREFIX.length());
            return Character.toLowerCase(property.charAt(0)) + property.substring(1);
        }
        // 其它情况，直接返回方法名
        return implMethodName;
    }
}
