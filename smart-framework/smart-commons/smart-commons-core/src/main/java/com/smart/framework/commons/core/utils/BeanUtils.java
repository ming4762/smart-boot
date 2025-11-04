package com.smart.framework.commons.core.utils;

import cn.hutool.core.bean.BeanUtil;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

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

    private static final List<Class<?>> DEEP_MAP_IGNORE_CLASS_LIST = Arrays.asList(
            CharSequence.class,
            Number.class,
            Date.class,
            Temporal.class,
            Enum.class,
            UUID.class,
            URI.class,
            URL.class,
            Locale.class,
            MultipartFile.class
    );

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
            return Collections.emptyMap();
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

    public static Map<String, Object> flattenBean(Object bean) {
        if (bean == null) {
            return Collections.emptyMap();
        }
        // 深度转换为嵌套 map
        Map<String, Object> nestedMap = deepBeanToMap(bean);
        // 展开嵌套 map
        Map<String, Object> flatMap = LinkedHashMap.newLinkedHashMap(10);
        buildFlatMap("", nestedMap, flatMap);
        return flatMap;
    }

    private static void buildFlatMap(String prefix, Object current, Map<String, Object> flatMap) {
        if (current == null) {
            return;
        }
        if (current instanceof Map<?, ?> map) {
            flattenMap(prefix, map, flatMap);
            return;
        }
        if (current instanceof Collection<?> coll) {
            flattenIterable(prefix, coll, flatMap);
            return;
        }
        if (current.getClass().isArray()) {
            flattenArray(prefix, current, flatMap);
            return;
        }
        // 基本类型或简单对象
        flatMap.put(prefix, current);
    }

    private static void flattenMap(String prefix, Map<?, ?> map, Map<String, Object> flatMap) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = entry.getKey() == null ? "null" : entry.getKey().toString();
            String newKey = prefix.isEmpty() ? key : prefix + "." + key;
            buildFlatMap(newKey, entry.getValue(), flatMap);
        }
    }

    private static void flattenIterable(String prefix, Collection<?> coll, Map<String, Object> flatMap) {
        int index = 0;
        for (Object item : coll) {
            String newKey = prefix + "[" + index++ + "]";
            buildFlatMap(newKey, item, flatMap);
        }
    }

    private static void flattenArray(String prefix, Object array, Map<String, Object> flatMap) {
        int len = Array.getLength(array);
        for (int i = 0; i < len; i++) {
            String newKey = prefix + "." + i;
            buildFlatMap(newKey, Array.get(array, i), flatMap);
        }
    }

    private static Object convertValue(Object value,
                                       IdentityHashMap<Object, Object> visited,
                                       int depth,
                                       int maxDepth,
                                       String[] ignoreProperties) {
        if (value == null || isSimpleValueType(value.getClass())) {
            return value;
        }
        // 循环引用检测
        if (visited.containsKey(value)) {
            return "[CIRCULAR_REF]";
        }
        // 深度限制
        if (depth >= maxDepth) {
            return value.toString();
        }

        if (value instanceof Map<?, ?> map) {
            return handleMap(map, visited, depth, maxDepth, ignoreProperties);
        }
        if (value instanceof Collection<?> coll) {
            return handleCollection(coll, visited, depth, maxDepth, ignoreProperties);
        }
        if (value.getClass().isArray()) {
            return handleArray(value, visited, depth, maxDepth, ignoreProperties);
        }
        return handlePojo(value, visited, depth, maxDepth, ignoreProperties);
    }

    private static Object handleMap(Map<?, ?> original,
                                    IdentityHashMap<Object, Object> visited,
                                    int depth,
                                    int maxDepth,
                                    String[] ignoreProperties) {
        Map<Object, Object> result = new LinkedHashMap<>();
        visited.put(original, Boolean.TRUE);
        for (Map.Entry<?, ?> e : original.entrySet()) {
            Object key = (e.getKey() == null) ? null : e.getKey().toString();
            Object value = convertValue(e.getValue(), visited, depth + 1, maxDepth, ignoreProperties);
            result.put(key, value);
        }
        visited.remove(original);
        return result;
    }

    private static Object handleCollection(Collection<?> coll,
                                           IdentityHashMap<Object, Object> visited,
                                           int depth,
                                           int maxDepth,
                                           String[] ignoreProperties) {
        List<Object> list = new ArrayList<>(coll.size());
        visited.put(coll, Boolean.TRUE);
        for (Object elem : coll) {
            list.add(convertValue(elem, visited, depth + 1, maxDepth, ignoreProperties));
        }
        visited.remove(coll);
        return list;
    }

    private static Object handleArray(Object array,
                                      IdentityHashMap<Object, Object> visited,
                                      int depth,
                                      int maxDepth,
                                      String[] ignoreProperties) {
        int len = Array.getLength(array);
        List<Object> list = new ArrayList<>(len);
        visited.put(array, Boolean.TRUE);
        for (int i = 0; i < len; i++) {
            list.add(convertValue(Array.get(array, i), visited, depth + 1, maxDepth, ignoreProperties));
        }
        visited.remove(array);
        return list;
    }

    private static Object handlePojo(Object pojo,
                                     IdentityHashMap<Object, Object> visited,
                                     int depth,
                                     int maxDepth,
                                     String[] ignoreProperties) {
        visited.put(pojo, Boolean.TRUE);
        Map<String, Object> map = BeanUtil.beanToMap(pojo, ignoreProperties);
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, Object> e : map.entrySet()) {
            result.put(e.getKey(), convertValue(e.getValue(), visited, depth + 1, maxDepth, ignoreProperties));
        }
        visited.remove(pojo);
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
        boolean matched = DEEP_MAP_IGNORE_CLASS_LIST.stream().anyMatch(item -> item.isAssignableFrom(clazz));
        if (matched) {
            return true;
        }
        return clazz.equals(Class.class);
    }
}
