package com.smart.db.analysis.utils;

import com.google.common.collect.Maps;
import com.smart.db.analysis.constants.ExtMappingEnum;
import com.smart.db.analysis.constants.TypeMappingEnum;
import com.smart.db.analysis.converter.Converter;
import com.smart.db.analysis.converter.StringConverter;
import com.smart.db.analysis.pojo.dbo.AbstractDatabaseBaseDO;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存工具类
 * @author shizhongming
 * 2020/1/18 9:05 下午
 */
public class CacheUtils {

    private CacheUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 数据库字段与Field映射缓存
     */
    private static final ConcurrentMap<Class<? extends AbstractDatabaseBaseDO>, Map<String, Field>> DATABASE_FIELD_MAPPING = Maps.newConcurrentMap();

    /**
     * 类型映射
     */
    private static final ConcurrentMap<Integer, TypeMappingEnum> TYPE_MAPPING_CACHE = Maps.newConcurrentMap();

    /**
     * converter缓存
     */
    private static final ConcurrentMap<Class<? extends Converter<?, ?>>, Converter<?, ?>> CONVERTER_CACHE = Maps.newConcurrentMap();

    /**
     * 自动转换器存储
     */
    private static final ConcurrentMap<String, Converter<?, ?>> AUTO_CONVERTER_CACHE = Maps.newConcurrentMap();

    /**
     * extjs与java映射
     */
    private static final ConcurrentMap<String, ExtMappingEnum> EXT_JAVA_MAPPING_CACHE = Maps.newConcurrentMap();

    private static final StringConverter STRING_CONVERTER = new StringConverter();


    /**
     * 添加converter
     * @param key key
     * @param converter converter
     */
    public static void addConverter(@NonNull String key, @NonNull Converter<?, ?> converter) {
        AUTO_CONVERTER_CACHE.put(key, converter);
    }

    /**
     * 获取自动转换器
     * @param key 转换器的key
     * @return 自动转换器
     */
    @Nullable
    public static Converter<?, ?> getAutoConverter(String key) {
        return AUTO_CONVERTER_CACHE.get(key);
    }


    public static void setFieldMapping(@NonNull Class<? extends AbstractDatabaseBaseDO> key, @NonNull Map<String, Field> mapping) {
        DATABASE_FIELD_MAPPING.put(key, mapping);
    }

    @Nullable
    public static Map<String, Field> getFieldMapping(@NonNull Class<? extends AbstractDatabaseBaseDO> key) {
        return DATABASE_FIELD_MAPPING.get(key);
    }

    /**
     * 获取转换器
     * @param clazz 转换器类型
     * @return 转换器
     * @throws NoSuchMethodException 未找到构造方法
     * @throws IllegalAccessException IllegalAccessException
     * @throws InvocationTargetException InvocationTargetException
     * @throws InstantiationException InstantiationException
     */
    public static Converter getConverter(Class<? extends Converter<?, ?>> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!CONVERTER_CACHE.containsKey(clazz)) {
            CONVERTER_CACHE.put(clazz, clazz.getDeclaredConstructor().newInstance());
        }
        return CONVERTER_CACHE.get(clazz);
    }

    public static Converter getStringConverter() {
        return STRING_CONVERTER;
    }

    /**
     * 数据库field映射是否为null
     * @return 数据库field映射是否为null
     */
    public static boolean isFieldMappingEmpty() {
        return DATABASE_FIELD_MAPPING.isEmpty();
    }

    public static void setTypeMapping(@NonNull Integer key, @NonNull TypeMappingEnum mapping) {
        TYPE_MAPPING_CACHE.put(key, mapping);
    }

    public static void setTypeMapping(@NonNull Map<Integer, TypeMappingEnum> values) {
        TYPE_MAPPING_CACHE.putAll(values);
    }

    @Nullable
    public static TypeMappingEnum getFieldMapping(@NonNull Integer key) {
        return TYPE_MAPPING_CACHE.get(key);
    }

    /**
     * 设置ext映射
     * @param mapping ext映射
     */
    public static void setExtTypeMapping(@NonNull Map<String, ExtMappingEnum> mapping) {
        EXT_JAVA_MAPPING_CACHE.putAll(mapping);
    }

    /**
     * 获取java类映射的ext类型
     * @param key java类型
     * @return ext类型
     */
    public static ExtMappingEnum getExtTypeMapping(@NonNull String key) {
        return EXT_JAVA_MAPPING_CACHE.get(key);
    }
}
