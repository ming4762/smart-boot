package com.smart.framework.tool.database.utils;

import com.smart.framework.commons.core.utils.PropertyUtils;
import com.smart.framework.tool.database.annotation.DatabaseField;
import com.smart.framework.tool.database.constants.ExceptionConstant;
import com.smart.framework.tool.database.converter.AutoConverter;
import com.smart.framework.tool.database.converter.Converter;
import com.smart.framework.tool.database.exception.SmartDatabaseException;
import com.smart.framework.tool.database.pojo.dto.SmartSelectColumn;
import com.smart.framework.tool.database.pojo.dto.SmartSelectSqlInfo;
import com.smart.framework.tool.database.pojo.dto.SmartSelectWhere;
import com.smart.framework.tool.database.pojo.dto.SmartSqlInfo;
import lombok.SneakyThrows;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.springframework.core.annotation.AnnotationUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据库工具类
 * @author shizhongming
 * 2020/1/19 8:41 下午
 */
public class DatabaseUtils {

    private static final String STAR = "*";
    /**
     * sql 占位符 正则
     */
    private static final Pattern SQL_PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

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
    @NonNull
    @SneakyThrows(Exception.class)
    public static <T> List<T> resultSetToModel(@NonNull ResultSet resultSet,
                                               @NonNull Class<T> clazz,
                                               @NonNull Map<String, Field> mapping) {
        final List<T> modelList = new LinkedList<>();
        final ResultSetMetaData metaData = resultSet.getMetaData();
        final int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            modelList.add(convertRowToModel(resultSet, clazz, mapping, metaData, columnCount));
        }

        return modelList;
    }

    @SneakyThrows(Exception.class)
    private static <T> T convertRowToModel(ResultSet resultSet,
                                           Class<T> clazz,
                                           Map<String, Field> mapping,
                                           ResultSetMetaData metaData,
                                           int columnCount) {
        final T model = clazz.getDeclaredConstructor().newInstance();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            Field field = mapping.get(columnName);
            if (field != null) {
                Object value = getColumnValue(resultSet, i, field);
                if (value != null) {
                    PropertyUtils.setProperty(model, field.getName(), value);
                }
            }
        }
        return model;
    }
    @SneakyThrows(Exception.class)
    private static Object getColumnValue(ResultSet resultSet, int index, Field field) {
        Class<?> fieldType = field.getType();
        Object value;

        if (fieldType == Date.class) {
            value = resultSet.getTimestamp(index);
        } else if (fieldType == Short.class) {
            value = resultSet.getShort(index);
        } else {
            value = resultSet.getObject(index);
        }

        // Short 类型特殊处理
        if (value instanceof Short) {
            value = Integer.valueOf(value.toString());
        }

        // 类型不一致，执行转换
        if (value != null && !Objects.equals(fieldType, value.getClass())) {
            value = convertValue(field, value);
        }

        return value;
    }

    /**
     * 解析SQL
     * @param sql SQL语句
     * @return SQL解析结果
     */
    @SneakyThrows(JSQLParserException.class)
    public static SmartSqlInfo parseSql(@NonNull String sql) {
        if (!StringUtils.hasText(sql)) {
            return null;
        }
        Statement parse = CCJSqlParserUtil.parse(sql);
        if (parse instanceof PlainSelect select) {
            return parseSelectSql(select, sql);
        }
        throw new UnsupportedOperationException("不支持的SQL语句类型，sql: " + sql);
    }

    /**
     * 解析查询语句
     * @param select 查询语句
     * @param sql 原始SQL语句
     * @return 查询语句解析结果
     */
    private static SmartSelectSqlInfo parseSelectSql(PlainSelect select, String sql) {
        List<SelectItem<?>> selectItems = select.getSelectItems();

        // 解析查询列信息
        // 是否包含 * 列
        boolean hasStarColumn = false;
        // 解析查询列
        List<SmartSelectColumn> columnList = new ArrayList<>(selectItems.size());
        for (SelectItem<?> item : selectItems) {
            SimpleNode node = item.getASTNode();
            String firstToken = node.jjtGetFirstToken().image;
            String lastToken = node.jjtGetLastToken().image;
            if (STAR.equals(lastToken)) {
                hasStarColumn = true;
            }
            columnList.add(
                    SmartSelectColumn.builder()
                            .column(item.toString())
                            .left(firstToken)
                            .right(lastToken)
                            .build()
            );
        }
        // 解析where 条件
        List<SmartSelectWhere> whereList = new ArrayList<>();
        parseWhere(select.getWhere(), whereList);

        // 解析SQL中的占位符
        List<String> placeholderList = parsePlaceholder(sql);

        return SmartSelectSqlInfo.builder()
                .statement(select)
                .sql(sql)
                .placeholderList(placeholderList)
                .hasStarColumn(hasStarColumn)
                .columnList(columnList)
                .whereList(whereList)
                .build();
    }

    /**
     * 解析where 条件
     * @param expression where 条件
     * @param whereList where 条件列表
     */
    private static void parseWhere(Expression expression, List<SmartSelectWhere> whereList) {
        if (expression == null) {
            return;
        }
        if (expression instanceof AndExpression andExpression) {
            parseWhere(andExpression.getLeftExpression(), whereList);
            parseWhere(andExpression.getRightExpression(), whereList);
        } else if (expression instanceof OrExpression orExpression) {
            parseWhere(orExpression.getLeftExpression(), whereList);
            parseWhere(orExpression.getRightExpression(), whereList);
        } else if (
                expression instanceof EqualsTo
                || expression instanceof GreaterThan
                || expression instanceof GreaterThanEquals
                || expression instanceof MinorThan
                || expression instanceof MinorThanEquals
                || expression instanceof NotEqualsTo
        ) {

            BinaryExpression binaryExpr = (BinaryExpression) expression;
            String operator = binaryExpr.getStringExpression();
            whereList.add(buildWhere(binaryExpr.getLeftExpression(), binaryExpr.getRightExpression(), operator));
        } else if (expression instanceof InExpression inExpression) {
            whereList.add(buildWhere(inExpression.getLeftExpression(), inExpression.getRightExpression(), "in"));
        } else {
            throw new UnsupportedOperationException("不支持的SQL where条件，where: " + expression);
        }
    }

    private static SmartSelectWhere buildWhere(Expression leftExpression, Expression rightExpression, String operator) {
        String columnFull = null;
        String columnName = null;
        if (leftExpression instanceof Column column) {
            columnFull = column.getFullyQualifiedName();
            columnName = column.getColumnName();
        }
        String value = rightExpression.toString();
        // 判断是否是占位符
        boolean placeholder = SQL_PLACEHOLDER_PATTERN.matcher(value).find();
        return SmartSelectWhere.builder()
                .columnFull(columnFull)
                .columnName(columnName)
                .operator(operator)
                .value(value)
                .placeholder(placeholder)
                .build();
    }


    /**
     * 解析SQL中的占位符
     * @param sql SQL语句
     * @return 占位符列表
     */
    private static List<String> parsePlaceholder(String sql) {
        Matcher matcher = SQL_PLACEHOLDER_PATTERN.matcher(sql);
        List<String> placeholders = new ArrayList<>(10);
        while (matcher.find()) {
            placeholders.add(matcher.group(1));
        }
        return placeholders;
    }


    /**
     * 转换至
     * @param field java field
     * @param value 需要转换的值
     * @return 转换后的值
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
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
