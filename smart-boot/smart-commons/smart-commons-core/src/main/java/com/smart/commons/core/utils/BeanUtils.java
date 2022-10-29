package com.smart.commons.core.utils;

import lombok.SneakyThrows;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2022/1/21
 * @since 2.0.0
 */
public class BeanUtils {

    private BeanUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 拷贝列表
     * @param sourceList 源列表
     * @param targetClass 目标类型
     * @param <T> 目标类型
     * @return 拷贝后的列表
     */
    @SneakyThrows({NoSuchMethodException.class, InstantiationException.class, IllegalAccessException.class,
            IllegalArgumentException.class, InvocationTargetException.class})
    public static <T> List<T> copyProperties(Collection<?> sourceList, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>(0);
        }
        List<T> targetList = new ArrayList<>(sourceList.size());
        for (Object item : sourceList) {
            T target = targetClass.getConstructor().newInstance();
            org.springframework.beans.BeanUtils.copyProperties(item, target);
            targetList.add(target);
        }
        return targetList;
    }
}
