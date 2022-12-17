package com.smart.db.analysis.utils;

import com.google.common.collect.Lists;
import com.smart.commons.core.utils.PropertyUtils;
import com.smart.db.analysis.annotation.DatabaseField;
import com.smart.db.analysis.constants.ExceptionConstant;
import com.smart.db.analysis.converter.AutoConverter;
import com.smart.db.analysis.converter.Converter;
import com.smart.db.analysis.exception.SmartDatabaseException;
import lombok.SneakyThrows;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

/**
 * 数据库工具类
 * @author shizhongming
 * 2020/1/19 8:41 下午
 */
public class DatabaseUtils {

    private DatabaseUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * resultSet转为实体类
     * @param resultSet ResultSet
     * @param clazz 实体类类型
     * @param mapping 映射关系
     * @param <T> 实体类类型
     * @return 实体类实例
     */
    @SneakyThrows
    @NonNull
    public static <T> List<T> resultSetToModel(@NonNull ResultSet resultSet, @NonNull Class<T> clazz, @NonNull Map<String, Field> mapping) {
        final ResultSetMetaData metaData = resultSet.getMetaData();
        final int columnCount = metaData.getColumnCount();
        final List<T> modelList = Lists.newLinkedList();
        while (resultSet.next()) {
            final T model = clazz.getDeclaredConstructor().newInstance();
            for (int i=1; i<=columnCount; i++) {
                final String name = metaData.getColumnName(i);
                final Field field = mapping.get(name);
                if (field != null) {
                    Object value;
                    final Class<?> aClass = field.getType();
                    if (aClass == Date.class) {
                        value = resultSet.getTimestamp(i);
                    } else if (aClass == Short.class) {
                        value = resultSet.getShort(i);
                    } else {
                        value = resultSet.getObject(i);
                    }
                    if (value instanceof Short) {
                        value = Integer.valueOf(value.toString());
                    }
                    if (Objects.nonNull(value)) {
                        // 判断类型是否一致，如果不一致 使用转换器进行转换
                        if (!Objects.equals(field.getType(), value.getClass())) {
                            // 执行转换
                            value = convertValue(field, value);
                        }
                        PropertyUtils.setProperty(model, field.getName(), value);
                    }

                }
            }
            modelList.add(model);
        }
        return modelList;
    }

    /**
     * 转换至
     * @param field java field
     * @param value 需要转换的值
     * @return 转换后的值
     */
    private static Object convertValue(@NonNull Field field, @NonNull Object value) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Converter converter = null;
        // 1、判断field是否有自定义的转换器，如果有使用自定义转换器
        Class<? extends Converter<?, ?>> converterClass = Optional.ofNullable(AnnotationUtils.findAnnotation(field, DatabaseField.class))
                .map(DatabaseField::converter)
                .orElse(null);
        if (Objects.nonNull(converterClass) && !Objects.equals(converterClass, AutoConverter.class)) {
            converter = CacheUtils.getConverter(converterClass);
        }
        // 2、如果没有自定义转换器，使用自动转换器
        if (Objects.isNull(converter)) {
            // 获取key
            final String key = value.getClass().getName() + field.getType().getName();
            converter = CacheUtils.getAutoConverter(key);
        }
        if (field.getType().equals(String.class)) {
            converter = CacheUtils.getStringConverter();
        }
        // 3、如果都没有转换器，抛出异常
        if (Objects.isNull(converter)) {
            throw new SmartDatabaseException(ExceptionConstant.DATABASE_FIELD_TO_JAVA_CONVERT_ERROR, value.getClass().getName(), field.getType().getName(), field.getName());
        }
        // 4、执行转换
        value = converter.convert(value);
        return value;
    }
}
