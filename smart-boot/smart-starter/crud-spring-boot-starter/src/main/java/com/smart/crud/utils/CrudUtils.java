package com.smart.crud.utils;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.smart.crud.model.BaseModel;
import com.smart.crud.model.Sort;
import com.smart.crud.plus.metadata.SmartTableInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Crud工具类
 * @author shizhongming
 * 2020/1/10 10:00 下午
 *
 */
@Slf4j
public final class CrudUtils {

    private CrudUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final ImmutableMap<String, SymbolParameterType> WRAPPER_METHOD_PARAMETER_MAP= ImmutableMap.<String, SymbolParameterType>builder()
            .put("=", new SymbolParameterType("eq", new Class[]{Object.class, Object.class}))
            .put("like", new SymbolParameterType("like", new Class[]{Object.class, Object.class}))
            .put(">", new SymbolParameterType("gt", new Class[]{Object.class, Object.class}))
            .put(">=", new SymbolParameterType("ge", new Class[]{Object.class, Object.class}))
            .put("<>", new SymbolParameterType("ne", new Class[]{Object.class, Object.class}))
            .put("<", new SymbolParameterType("lt", new Class[]{Object.class, Object.class}))
            .put("<=", new SymbolParameterType("le", new Class[]{Object.class, Object.class}))
            .put("in", new SymbolParameterType("in", new Class[]{Object.class, Collection.class}))
            .put("notLike", new SymbolParameterType("notLike", new Class[]{Object.class, Object.class}))
            .put("likeLeft", new SymbolParameterType("likeLeft", new Class[]{Object.class, Object.class}))
            .put("likeRight", new SymbolParameterType("likeRight", new Class[]{Object.class, Object.class}))
            .put("notIn", new SymbolParameterType("notIn", new Class[]{Object.class, Collection.class}))
            .put("groupBy", new SymbolParameterType("groupBy", new Class[]{Object.class}))
            .build();

    private static final String SYMBOL_EQUAL = "=";

    private static final String SYMBOL_NOT_EQUAL = "<>";

    private static final String SEARCH_SYMBOL_SPLIT = "@";

    private static final Map<Class<?>, SmartTableInfo> SMART_TABLE_INFO_CACHE = Maps.newConcurrentMap();

    public static String getTableName(Class<?> clazz) {
        return Optional.ofNullable(getTableInfo(clazz))
                .map(TableInfo::getTableName)
                .orElse(null);
    }

    /**
     * 获取table info
     * @param clazz 实体类
     * @return TableInfo
     */
    public static SmartTableInfo getTableInfo(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        if (SMART_TABLE_INFO_CACHE.containsKey(clazz)) {
            return SMART_TABLE_INFO_CACHE.get(clazz);
        }
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        SmartTableInfo smartTableInfo = SmartTableInfo.create(tableInfo);
        SMART_TABLE_INFO_CACHE.put(clazz, smartTableInfo);
        return smartTableInfo;
    }

    /**
     * 根据表明获取table info
     * @param tableName 表名
     * @return SmartTableInfo
     */
    public static SmartTableInfo getTableInfo(String tableName) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(tableName);
        if (tableInfo == null) {
            return null;
        }
        return getTableInfo(tableInfo.getEntityType());
    }


    /**
     * 解析排序字段
     * @param sortName 以逗号分隔的实体类属性名称
     * @param sortOrder 以逗号分隔的排序方法
     * @param clazz 实体类类型
     * @return 排序信息
     */
    @NonNull
    public static List<Sort> analysisOrder(@NonNull String sortName, @Nullable String sortOrder, Class<?> clazz) {
        SmartTableInfo tableInfo = getTableInfo(clazz);
        final String[] sortNameList = sortName.split(",");
        final List<String> sortOrderList = sortOrder == null ? new ArrayList<>() : Arrays.asList(sortOrder.split(","));
        final List<Sort> sortList = Lists.newLinkedList();
        for (int i=0; i<sortNameList.length; i++) {
            final String name = sortNameList[i].trim();
            final String order = sortOrderList.size() > i ? sortOrderList.get(i).trim() : "asc";
            // 获取数据库字段
            final String dbName = tableInfo.getTableFiled(name).getColumn();
            if (StringUtils.isEmpty(dbName)) {
                log.warn("未找到排序字段对应的数据库字段：{}，该排序属性被忽略", name);
            } else {
                sortList.add(new Sort(name, order, dbName));
            }
        }
        return sortList;
    }

    /**
     * 设置查询的字段
     * @param fieldList 实体类字段列表
     * @param modelClass 类型
     * @param queryWrapper 查询参数
     * @param <T> 泛型
     */
    public static <T extends BaseModel> void setQueryField(@NonNull List<String> fieldList, @NonNull Class<?> modelClass, @NonNull QueryWrapper<T> queryWrapper) {
        if (CollectionUtils.isEmpty(fieldList)) {
            return;
        }
        SmartTableInfo tableInfo = getTableInfo(modelClass);
        queryWrapper.select(fieldList.stream()
                .map(item -> {
                    if (item.equals(tableInfo.getKeyProperty())) {
                        return tableInfo.getKeyColumn();
                    }
                    return Optional.ofNullable(tableInfo.getTableFiled(item))
                            .map(TableFieldInfo::getColumn)
                            .orElse(null);
                }).filter(StringUtils::isNotEmpty)
                .toArray(String[]::new));
    }

    /**
     * 从参数创建QueryWrapper
     * @param parameter 参数
     * @param clazz 实体类class
     * @param <T> 实体类泛型
     * @return 查询参数
     */
    @NonNull
    public static <T extends BaseModel> QueryWrapper<T> createQueryWrapperFromParameters(@NonNull Map<Serializable, Serializable> parameter, @NonNull Class<?> clazz) {
        final QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        createBaseQueryWrapperFromParameters(parameter, clazz, queryWrapper);
        return queryWrapper;
    }

    /**
     * 分割执行函数
     * @param partData 要分割数据
     * @param partSize 分割的大小
     * @param handler 执行函数
     * @return 分割执行结果
     * @param <T> 结果类型
     * @param <P> 参数类型
     */
    public static <T, P> List<T> partitionList(Collection<P> partData, int partSize, Function<Collection<P>, List<T>> handler) {
        if (CollectionUtils.isEmpty(partData)) {
            return Collections.emptyList();
        }
        if (partData.size() <= partSize) {
            return handler.apply(partData);
        }
        return Lists.partition(new ArrayList<>(partData), partSize).stream()
                .flatMap(list -> handler.apply(list).stream())
                .toList();
    }

    private static <T extends BaseModel> void createBaseQueryWrapperFromParameters(@NonNull Map<Serializable, Serializable> parameter, @NonNull Class<?> clazz, @NonNull Wrapper<T> queryWrapper) {
        SmartTableInfo tableInfo = getTableInfo(clazz);
        for (Map.Entry<Serializable, Serializable> entry : parameter.entrySet()) {
            Serializable keySer = entry.getKey();
            if (!(keySer instanceof String key)) {
                continue;
            }
            Serializable value = entry.getValue();
            if (!key.contains(SEARCH_SYMBOL_SPLIT)) {
                continue;
            }
            String[] keySplit = key.split(SEARCH_SYMBOL_SPLIT);
            // 获取符号
            final String symbol = keySplit.length > 1 ? keySplit[1] : null;
            if (StringUtils.isBlank(symbol)) {
                log.warn("参数无效，未找到符号，实体类：{}，key:{}", clazz.getName(), key);
                continue;
            }
            TableFieldInfo tableFiled = tableInfo.getTableFiled(keySplit[0]);
            if (tableFiled == null) {
                log.warn("参数无效，未找到实体类对应属性：{}", keySplit[0]);
                continue;
            }
            CrudUtils.dealValue(key, value, queryWrapper, symbol, tableFiled.getField(), tableFiled.getColumn());
        }
    }

    @SneakyThrows({IllegalAccessException.class, InvocationTargetException.class})
    private static <T> void dealValue(@NonNull String key, @Nullable Object value, @NonNull Wrapper<T> queryWrapper, @Nullable String symbol, @NonNull Field field, @Nullable String dbFieldName) {
        if (!Objects.isNull(value)) {
            // 处理value
            Object enumValue = dealEnumValue(value, field);
            if (enumValue != null) {
                value = enumValue;
            }
            // 值不为null处理
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(value.toString())) {
                final Method method = getWrapperMethod(queryWrapper.getClass(), symbol);
                if (method == null) {
                    log.warn("参数无效，未找到符号对应执行方法，参数名：{}，符号：{}", key, symbol);
                } else {
                    method.invoke(queryWrapper, dbFieldName, value);
                }
            } else {
                log.warn("参数无效，忽略参数值，参数名：{}，值：{}", key, value);
            }
        } else {
            // null 处理
            if (org.apache.commons.lang3.StringUtils.equals(SYMBOL_EQUAL, symbol)) {
                ((QueryWrapper<T>) queryWrapper).isNull(dbFieldName);
            } else if (org.apache.commons.lang3.StringUtils.equals(symbol, SYMBOL_NOT_EQUAL)) {
                ((QueryWrapper<T>) queryWrapper).isNotNull(dbFieldName);
            } else {
                log.warn("null值参数只能使用'='或'<>'");
            }
        }
    }

    private static Object dealEnumValue(@Nullable Object value, @NonNull Field field) {
        if (value == null) {
            return null;
        }
        if (IEnum.class.isAssignableFrom(field.getType()) && field.getType().isEnum()) {
            // 枚举类处理
            Map<String, IEnum<?>> enumMap = Arrays.stream(field.getType().getEnumConstants())
                    .collect(Collectors.toMap(item -> ((Enum<?>)item).name(), item -> (IEnum<?>) item));
            if (value instanceof String) {
                IEnum<?> iEnum = enumMap.get(value);
                return iEnum == null ? null : iEnum.getValue();
            } else if (Collection.class.isAssignableFrom(value.getClass())) {
                // 集合类
                List<? extends IEnum<?>> enumList = ((Collection<?>) value).stream().map(enumMap::get)
                        .filter(Objects::nonNull)
                        .toList();
                if (CollectionUtils.isEmpty(enumList)) {
                    return null;
                }
                return enumList;
            }
        }
        return null;
    }

    /**
     * 获取wrapper执行方法
     * @param clazz Wrapper类型
     * @param symbol 符号
     * @return 查询方法
     */
    @SuppressWarnings("rawtypes")
    private static Method getWrapperMethod(Class<? extends Wrapper> clazz, String symbol) {
        Method method = null;
        final SymbolParameterType symbolParameterType = WRAPPER_METHOD_PARAMETER_MAP.get(symbol);
        if (symbolParameterType != null) {
            try {
                method = clazz.getMethod(symbolParameterType.getSymbol(), symbolParameterType.getParameterTypes());
            } catch (NoSuchMethodException e) {
                log.error(e.getMessage(), e);
            }
        }
        return method;
    }


    @AllArgsConstructor
    @Getter
    @Setter
    static class SymbolParameterType {
        private String symbol;

        @SuppressWarnings("rawtypes")
        private Class[] parameterTypes;
    }

}
