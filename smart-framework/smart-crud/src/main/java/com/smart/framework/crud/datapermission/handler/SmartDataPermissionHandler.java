package com.smart.framework.crud.datapermission.handler;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.github.pagehelper.Page;
import com.smart.framework.commons.core.utils.BeanUtils;
import com.smart.framework.crud.datapermission.aspect.DataPermissionContextHolder;
import com.smart.framework.crud.datapermission.exception.SmartDataPermissionException;
import com.smart.framework.crud.plus.metadata.SmartTableInfo;
import com.smart.framework.crud.utils.CrudPageHelper;
import com.smart.framework.crud.utils.CrudUtils;
import com.smart.module.api.crud.SmartCrudDataPermissionApi;
import com.smart.module.api.crud.constants.DataPermissionScopeEnum;
import com.smart.module.api.crud.module.SmartDataContextUserModel;
import com.smart.module.api.crud.module.SmartDataPermissionModel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数据权限处理器
 * @author shizhongming
 * 2025/3/5 20:51
 * @since 5.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class SmartDataPermissionHandler implements MultiDataPermissionHandler {

    private static final String DOT = ".";
    private static final String COUNT_UPPERCASE = "_COUNT";
    private static final String COUNT_LOWERCASE = "_count";
    private static final String NOT_DATA_PARAMETER = "NO_DATA_&&&";
    private static final String CUSTOM_PLACEHOLDER_PREFIX = "${";
    private static final String CUSTOM_PLACEHOLDER_SUFFIX = "}";

    private static final String PLACEHOLDER_DEPT_KEY = "userDept";
    private static final String PLACEHOLDER_DEPT_CHILDREN_KEY = "userDeptWithChildren";

    private final SmartCrudDataPermissionApi smartCrudDataPermissionApi;


    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        // 判断是否函数配置忽略
        if (this.ignore(table, mappedStatementId)) {
            return null;
        }
        // 获取用户上下文
        SmartDataContextUserModel userContext = this.getUserContext();
        if (userContext == null || Boolean.TRUE.equals(userContext.getIsSuperAdmin())) {
            // 用户上下文为空或者是超级管理员，直接返回
            return null;
        }
        // 获取数据权限列表
        List<SmartDataPermissionModel> dataPermissionList = this.getDataPermissionList(userContext.getToken(), mappedStatementId);
        if (CollectionUtils.isEmpty(dataPermissionList)) {
            // 未配置数据权限
            return null;
        }
        // 排除权限类型为DataScopeEnum.DATA_ALL、表名不符合
        dataPermissionList = dataPermissionList.stream()
                .filter(item -> !DataPermissionScopeEnum.DATA_ALL.equals(item.getScope()))
                .peek(item -> {
                    // 根据字段填充表名
                    if (StringUtils.hasText(item.getTableName())) {
                        return;
                    }
                    SmartTableInfo smartTableInfo = CrudUtils.getTableInfo(table.getName());
                    if (smartTableInfo != null) {
                        TableFieldInfo tableFieldInfo = smartTableInfo.getTableFiledByColumn(Objects.requireNonNullElseGet(item.getColumn(), () -> item.getScope().getColumn()));
                        if (tableFieldInfo != null) {
                            item.setTableName(smartTableInfo.getTableName());
                        }
                    }
                })
                // 排除表名不一致的
                .filter(item -> table.getName().equals(item.getTableName()))
                .toList();
        if (CollectionUtils.isEmpty(dataPermissionList)) {
            return null;
        }
        // 构建数据权限表达式
        List<Expression> expressionList = this.buildExpressionList(table, mappedStatementId, dataPermissionList);
        if (CollectionUtils.isEmpty(expressionList)) {
            return null;
        }
        if (expressionList.size() == 1) {
            return expressionList.getFirst();
        }
        Expression result = expressionList.getFirst();
        for (int i = 1; i < expressionList.size(); i++) {
            result = new AndExpression().withLeftExpression(result).withRightExpression(expressionList.get(i));
        }
        return result;
    }

    /**
     * 判断是否函数配置忽略
     * @param table 表
     * @param mappedStatementId 方法名
     * @return 是否忽略
     */
    private boolean ignore(Table table, String mappedStatementId) {
        Set<SmartDataPermissionController.IgnoreData> ignoreDataList = SmartDataPermissionController.getIgnoreData();
        if (CollectionUtils.isEmpty(ignoreDataList)) {
            return false;
        }
        for (SmartDataPermissionController.IgnoreData ignoreData : ignoreDataList) {
            if (SmartDataPermissionController.IgnoreType.ALL.equals(ignoreData.getType())) {
                return true;
            }
            if (SmartDataPermissionController.IgnoreType.MAPPER.equals(ignoreData.getType()) && ignoreData.getValue().equals(mappedStatementId)) {
                return true;
            }
            if (SmartDataPermissionController.IgnoreType.TABLE.equals(ignoreData.getType()) && ignoreData.getValue().equals(table.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取mapper类
     * @param mappedStatementId 方法名
     * @return 类
     */
    private Class<?> getMapperClass(String mappedStatementId) {
        try {
            return Class.forName(mappedStatementId.substring(0, mappedStatementId.lastIndexOf(DOT)));
        } catch (ClassNotFoundException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    /**
     * 获取mapper方法
     * @param mappedStatementId 方法名
     * @return 方法
     */
    private Method getMapperMethod(String mappedStatementId) {
        Class<?> mapperClass = this.getMapperClass(mappedStatementId);
        if (mapperClass == null) {
            return null;
        }
        String mapperMethodName = mappedStatementId.substring(mappedStatementId.lastIndexOf(DOT) + 1);
        for (Method method : mapperClass.getDeclaredMethods()) {
            String methodName = method.getName();
            if (mapperMethodName.equals(methodName) || mapperMethodName.equals(methodName + COUNT_UPPERCASE) || mapperMethodName.equals(methodName + COUNT_LOWERCASE)) {
                return method;
            }
        }
        return null;
    }

    /**
     * 获取数据权限列表
     * 优先从手动指定获取
     * 从数据库配置的获取
     * 从注解获取
     * @return 数据权限列表
     */
    private List<SmartDataPermissionModel> getDataPermissionList(String token, String mappedStatementId) {
        // 优先从手动指定获取
        List<SmartDataPermissionModel> dataPermissionList = SmartDataPermissionController.getManualDataPermission();
        if (!CollectionUtils.isEmpty(dataPermissionList)) {
            return dataPermissionList;
        }
        // 从数据库配置的获取
        dataPermissionList = SmartDataPermissionMapperHolder.getCacheByMapperId(token, mappedStatementId, this.smartCrudDataPermissionApi::getCurrentUserDataPermission);
        if (!CollectionUtils.isEmpty(dataPermissionList)) {
            return dataPermissionList;
        }
        // 注解上下文获取
        dataPermissionList = DataPermissionContextHolder.get().stream()
                .map(item -> {
                    String tableName = item.tableName();
                    if (!StringUtils.hasText(tableName) && !Void.class.equals(item.tableClass())) {
                        SmartTableInfo smartTableInfo = CrudUtils.getTableInfo(item.tableClass());
                        if (smartTableInfo != null) {
                            tableName = smartTableInfo.getTableName();
                        }
                    }
                    return SmartDataPermissionModel.builder()
                            .permissionCode(item.configCode())
                            .scope(item.scope())
                            .column(item.column())
                            .tableName(tableName)
                            .permissionValue(item.permissionValue())
                            .build();
                })
                .toList();
        if (CollectionUtils.isEmpty(dataPermissionList)) {
            return Collections.emptyList();
        }
        // 根据code获取数据权限
        Map<Boolean, List<SmartDataPermissionModel>> codeMap = dataPermissionList.stream()
                .collect(
                        Collectors.groupingBy(item -> StringUtils.hasText(item.getPermissionCode()))
                );
        if (!codeMap.containsKey(Boolean.TRUE)) {
            return codeMap.get(Boolean.FALSE);
        }
        // 根据code获取数据权限
        List<SmartDataPermissionModel> dataPermissionByCode = SmartDataPermissionMapperHolder.getCacheByCode(
                codeMap.get(Boolean.TRUE).stream().map(SmartDataPermissionModel::getPermissionCode).toList(),
                this.smartCrudDataPermissionApi::getDataPermissionByCode
        );
        return Stream.concat(
                Objects.requireNonNullElseGet(codeMap.get(Boolean.FALSE), Collections::<SmartDataPermissionModel>emptyList).stream(),
                Objects.requireNonNullElseGet(dataPermissionByCode, Collections::<SmartDataPermissionModel>emptyList).stream()
        ).toList();
    }

    /**
     * 构建数据权限表达式
     * @param table 表
     * @param mappedStatementId 方法名
     * @param dataPermissionList 数据权限列表
     * @return 表达式
     */
    private List<Expression> buildExpressionList(Table table, String mappedStatementId, List<SmartDataPermissionModel> dataPermissionList) {
        // 获取表别名（可能为空）
        String tableAlias = Optional.ofNullable(table.getAlias()).map(Alias::getName).orElse(null);
        return dataPermissionList.stream()
                .map(permission -> {
                    DataPermissionScopeEnum dataScope = permission.getScope();
                    String permissionColumn = StringUtils.hasText(permission.getColumn()) ? permission.getColumn() : dataScope.getColumn();
                    return switch (dataScope) {
                        // TODO:策略模式
                        case DATA_PERSONAL -> this.buildPersonalExpression(tableAlias, permissionColumn);
                        case DATA_DEPT -> this.buildDeptExpression(tableAlias, permissionColumn, this::getUserDeptList);
                        case DATA_DEPT_AND_CHILD -> this.buildDeptExpression(tableAlias, permissionColumn, this::getUserDeptWithChildren);
                        case DATA_CUSTOM -> this.buildCustomExpression(mappedStatementId, permission.getPermissionValue());
                        default -> null;
                    };
                }).filter(Objects::nonNull)
                .toList();
    }

    /**
     * 构建个人数据权限表达式
     * @param tableAlias 表别名
     * @param permissionColumn 权限字段
     * @return 表达式
     */
    private Expression buildPersonalExpression(@Nullable String tableAlias, String permissionColumn) {
        SmartDataContextUserModel userContext = this.getUserContext();
        EqualsTo expression = new EqualsTo();
        expression.withLeftExpression(this.buildColumn(tableAlias, permissionColumn));
        expression.withRightExpression(new LongValue(userContext.getUserId()));
        return expression;
    }

    /**
     * 构建部门数据权限表达式
     * @param tableAlias 表别名
     * @param permissionColumn 权限字段
     * @param deptSupplier 部门
     * @return 表达式
     */
    private Expression buildDeptExpression(@Nullable String tableAlias, String permissionColumn, Supplier<List<Long>> deptSupplier) {
        List<Long> deptList = deptSupplier.get();
        if (CollectionUtils.isEmpty(deptList)) {
            // TODO: 用户没有对应部门，则不显示任何数据
            EqualsTo expression = new EqualsTo();
            expression.withLeftExpression(this.buildColumn(tableAlias, permissionColumn))
                    .withRightExpression(new LongValue(-1));
            return expression;
        }
        if (deptList.size() == 1) {
            // 部门数据权限
            EqualsTo expression = new EqualsTo();
            expression.withLeftExpression(this.buildColumn(tableAlias, permissionColumn))
                            .withRightExpression(new LongValue(deptList.getFirst()));
            return expression;
        }
        InExpression expression = new InExpression();
        ExpressionList<LongValue> deptExpressionList = new ExpressionList<>(
                deptList.stream()
                        .map(LongValue::new)
                        .toList()
        );
        expression.withLeftExpression(this.buildColumn(tableAlias, permissionColumn))
                .withRightExpression(ParenthesedExpressionList.from(deptExpressionList));
        return expression;
    }

    /**
     * 构建自定义的数据权限表达式
     * @param mappedStatementId mapper方法名
     * @param scopeValue 规则值
     * @return 表达式
     */
    @SneakyThrows(JSQLParserException.class)
    private Expression buildCustomExpression(String mappedStatementId, String scopeValue) {
        if (!StringUtils.hasText(scopeValue)) {
            throw new SmartDataPermissionException("数据权限配置错误，自定义权限，规则值不能为空，mappedStatementId：" + mappedStatementId);
        }
        Map<String, String> contextMap = this.convertContextToMap(scopeValue);
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(CUSTOM_PLACEHOLDER_PREFIX, CUSTOM_PLACEHOLDER_SUFFIX);
        String sql = helper.replacePlaceholders(scopeValue, name -> {
            String value = contextMap.get(name);
            return value == null ? NOT_DATA_PARAMETER : value;
        });
        return CCJSqlParserUtil.parseCondExpression(sql);
    }

    /**
     * 构建查询列
     * @param tableAlias 表别名
     * @param column 列名
     * @return 查询列
     */
    private Column buildColumn(String tableAlias, String column) {
        if (StringUtils.hasText(tableAlias)) {
            column = tableAlias + DOT + column;
        }
        return new Column(column);
    }

    /**
     * 获取用户上下文
     * @return 用户上下文
     */
    private SmartDataContextUserModel getUserContext() {
        return SmartDataContextHolder.getUserContext(this.smartCrudDataPermissionApi::getUserContext);
    }

    /**
     * 获取用户部门列表
     * @return 用户部门列表
     */
    private List<Long> getUserDeptList() {
        return SmartDataContextHolder.getUserDeptList(() -> {
            Page<Object> localPage = CrudPageHelper.getLocalPage();
            try {
                if (localPage != null) {
                    CrudPageHelper.clearPage();
                }
                return this.smartCrudDataPermissionApi.getUserDeptList();
            } finally {
                if (localPage != null) {
                    CrudPageHelper.setPage(localPage);
                }
            }
        });
    }

    /**
     * 获取用户部门及子部门列表
     * @return 用户部门及子部门列表
     */
    private List<Long> getUserDeptWithChildren() {
        return SmartDataContextHolder.getUserDeptWithChildren(this.smartCrudDataPermissionApi::getUserDeptChildren);
    }

    /**
     * 将上下文转为map，方便参数替换
     * @return map
     */
    protected Map<String, String> convertContextToMap(String scopeValue) {
        SmartDataContextUserModel userContext = this.getUserContext();
        Map<String, String> userMap = BeanUtils.beanToMap(userContext).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, item -> {
                    Object value = item.getValue();
                    if (value == null) {
                        // TODO:如果用户没有这属性，但是自定义参数配置了，返回一段字符串，这样查不出数据
                        return NOT_DATA_PARAMETER;
                    }
                    return value.toString();
                }));

        HashMap<String, String> result = HashMap.newHashMap(userMap.size() + 2);
        result.putAll(userMap);
        // 这里优化性能，只有在规则值中包含占位符时，才获取值
        if (scopeValue.contains(PLACEHOLDER_DEPT_KEY)) {
            result.put(PLACEHOLDER_DEPT_KEY, this.buildDeptListParam(Objects.requireNonNullElseGet(this.getUserDeptList(), Collections::emptyList)));
        }
        if (scopeValue.contains(PLACEHOLDER_DEPT_CHILDREN_KEY)) {
            result.put(PLACEHOLDER_DEPT_CHILDREN_KEY, this.buildDeptListParam(Objects.requireNonNullElseGet(this.getUserDeptWithChildren(), Collections::emptyList)));
        }
        return result;
    }

    protected String buildDeptListParam(List<Long> deptList) {
        return String.format("(%s)", deptList.stream().map(String::valueOf).collect(Collectors.joining(", ")));
    }
}
