package com.smart.commons.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 反射工具类
 * @author shizhongming
 * 2020/1/12 5:10 下午
 */
@Slf4j
public final class ReflectUtils {

    private ReflectUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 判断field是否是静态变量
     * @param field field
     * @return 是否是静态变量
     */
    public static boolean isStatic(@NonNull Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    /**
     * 判断field是否是静态变量
     * @param field field
     * @return 是否是静态变量
     */
    public static boolean isNotStatic(@NonNull Field field) {
        return !Modifier.isStatic(field.getModifiers());
    }

    /**
     * 获取所有Filed 包括上级的
     * @param clazz Class
     * @param fields field集合
     */
    public static void getAllFields(@NonNull Class<?> clazz, Set<Field> fields) {
        final Field[] fieldArray = clazz.getDeclaredFields();
        fields.addAll(Arrays.stream(fieldArray)
                .filter(ReflectUtils:: isNotStatic)
                .collect(Collectors.toSet()));
        // 获取上级
        Class<?> superClass = clazz.getSuperclass();
        if (Objects.nonNull(superClass)) {
            getAllFields(superClass, fields);
        }
    }

    /**
     * 获取指定类的属性
     * @param clazz 类
     * @return Field列表
     */
    public static List<Field> getField(@NonNull Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(ReflectUtils:: isNotStatic)
                .collect(Collectors.toList());
    }
}
