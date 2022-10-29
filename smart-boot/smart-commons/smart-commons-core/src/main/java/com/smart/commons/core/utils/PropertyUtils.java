package com.smart.commons.core.utils;

import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;

import java.lang.reflect.InvocationTargetException;

/**
 * @author shizhongming
 * 2022/10/29 16:12
 */
public class PropertyUtils {

    private PropertyUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 设置属性
     * @param bean bean
     * @param name field name
     * @param value value
     */
    @SneakyThrows({IllegalAccessException.class, IllegalArgumentException.class, InvocationTargetException.class})
    public static void setProperty(final Object bean, final String name, final Object value) {
        var descriptor = BeanUtils.getPropertyDescriptor(bean.getClass(), name);
        if (descriptor != null) {
            descriptor.getWriteMethod().invoke(bean, value);
        }
    }

    /**
     * 通过对象属性名获取属性值
     * @param bean 对象
     * @param fieldName 属性名
     * @return 属性值
     */
    @SneakyThrows({IllegalAccessException.class, IllegalArgumentException.class, InvocationTargetException.class})
    public static Object getProperty(@NonNull Object bean, @NonNull String fieldName) {
        var descriptor = BeanUtils.getPropertyDescriptor(bean.getClass(), fieldName);
        if (descriptor != null) {
            return descriptor.getReadMethod().invoke(bean);
        }
        return null;
    }
}

