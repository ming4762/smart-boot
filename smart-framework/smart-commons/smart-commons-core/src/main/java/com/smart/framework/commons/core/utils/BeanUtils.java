package com.smart.framework.commons.core.utils;

import cn.hutool.core.bean.BeanUtil;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.time.temporal.Temporal;
import java.util.*;

/**
 * @author ShiZhongMing
 * 2022/1/21
 * @since 2.0.0
 */
public class BeanUtils {

    private static final String CLASS_NAME = "class";

    private static final int DEFAULT_MAX_DEPTH = 100;

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

    /**
     * 浅层次bean转为map
     * @param object 需要转换的bean
     * @return 转换的map
     */
    @SneakyThrows({IllegalAccessException.class, InvocationTargetException.class})
    public static Map<String, Object> beanToMap(@NonNull Object object) {
        String[] propertyNames = Arrays.stream(org.springframework.beans.BeanUtils.getPropertyDescriptors(object.getClass()))
                .map(FeatureDescriptor::getName)
                .toArray(String[]::new);
        Map<String, Object> map = HashMap.newHashMap(propertyNames.length);

        for (String property : propertyNames) {
            if (!CLASS_NAME.equals(property)) {
                Object value = Objects.requireNonNull(org.springframework.beans.BeanUtils.getPropertyDescriptor(object.getClass(), property))
                        .getReadMethod().invoke(object);
                map.put(property, value);
            }
        }
        return map;
    }

    /**
     * 深度转换（默认不限制深度）
     */
    public static Map<String, Object> deepBeanToMap(Object bean, String... ignoreProperties) {
        return deepBeanToMap(bean, DEFAULT_MAX_DEPTH, ignoreProperties);
    }

    /**
     * 深度转换，支持设置最大深度
     *
     * @param bean bean 对象
     * @param maxDepth 最大递归深度（根对象为 depth=0）；若 <=0 则视为 0（只返回顶层字段但不递归其 bean 字段）
     * @param ignoreProperties 要忽略的属性名（传递给 BeanUtil.beanToMap）
     */
    public static Map<String, Object> deepBeanToMap(Object bean, int maxDepth, String... ignoreProperties) {
        if (bean == null) {
            return null;
        }
        Assert.notNull(maxDepth, "maxDepth must not be null"); // Hutool Assert，可换成 Objects.requireNonNull
        IdentityHashMap<Object, Object> visited = new IdentityHashMap<>();
        Object converted = convertValue(bean, visited, 0, Math.max(0, maxDepth), ignoreProperties);
        // 根对象应当是 Map（如果是简单类型则直接放入一个字段? 这里我们期望传入的是 POJO，转换应为 Map）
        if (converted instanceof Map) {
            //noinspection unchecked
            return (Map<String, Object>) converted;
        } else {
            // 如果根对象是简单类型，返回一个 map 包装 value
            Map<String, Object> wrapper = new LinkedHashMap<>();
            wrapper.put("value", converted);
            return wrapper;
        }
    }

    private static Object convertValue(Object value,
                                       IdentityHashMap<Object, Object> visited,
                                       int depth,
                                       int maxDepth,
                                       String[] ignoreProperties) {
        if (value == null) {
            return null;
        }
        // 基本类型 / 常见不可再拆分的类型 -> 直接返回
        if (isSimpleValueType(value.getClass())) {
            return value;
        }
        // 循环引用检测
        if (visited.containsKey(value)) {
            return "[CIRCULAR_REF]";
        }
        // 深度限制
        if (depth >= maxDepth) {
            // 达到深度限制：把对象转换为字符串表示（或直接返回原对象，视需求）
            return value.toString();
        }
        // Map -> 递归处理 value
        if (value instanceof Map<?, ?> original) {
            Map<Object, Object> result = new LinkedHashMap<>();
            visited.put(value, Boolean.TRUE);
            for (Map.Entry<?, ?> e : original.entrySet()) {
                Object k = e.getKey();
                Object v = e.getValue();
                Object ck = (k == null) ? null : k.toString();
                Object cv = convertValue(v, visited, depth + 1, maxDepth, ignoreProperties);
                result.put(ck, cv);
            }
            visited.remove(value);
            return result;
        }
        // Collection -> 递归处理元素
        if (value instanceof Collection<?> coll) {
            List<Object> list = new ArrayList<>(coll.size());
            visited.put(value, Boolean.TRUE);
            for (Object elem : coll) {
                list.add(convertValue(elem, visited, depth + 1, maxDepth, ignoreProperties));
            }
            visited.remove(value);
            return list;
        }
        // Array -> 转为 List 并递归
        if (value.getClass().isArray()) {
            int len = Array.getLength(value);
            List<Object> list = new ArrayList<>(len);
            visited.put(value, Boolean.TRUE);
            for (int i = 0; i < len; i++) {
                list.add(convertValue(Array.get(value, i), visited, depth + 1, maxDepth, ignoreProperties));
            }
            visited.remove(value);
            return list;
        }
        // 其他为 POJO（bean） -> 先用 Hutool beanToMap 转为 Map，然后对 Map 的值递归
        visited.put(value, Boolean.TRUE);
        Map<String, Object> map = BeanUtil.beanToMap(value, ignoreProperties);
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, Object> e : map.entrySet()) {
            result.put(e.getKey(), convertValue(e.getValue(), visited, depth + 1, maxDepth, ignoreProperties));
        }
        visited.remove(value);
        return result;
    }

    /**
     * 是否是简单类型
     * @param clazz 类型
     * @return 是否是简单类型
     */
    public static boolean isSimpleValueType(Class<?> clazz) {
        if (clazz == null) {
            return true;
        }
        if (clazz.isPrimitive()) {
            return true;
        }
        if (ClassUtils.isPrimitiveOrWrapper(clazz)) {
            return true;
        }
        if (CharSequence.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (Number.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (Date.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (Temporal.class.isAssignableFrom(clazz)) {
            return true;
        } // java.time.*
        if (Enum.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (UUID.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (URI.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (URL.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (Locale.class.isAssignableFrom(clazz)) {
            return true;
        }
        return clazz.equals(Class.class);
    }
}
