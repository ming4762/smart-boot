package com.smart.framework.crud.plus.metadata;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.commons.core.proxy.ExtendMethodInterceptor;
import com.smart.framework.crud.annotation.TableLogicField;
import com.smart.framework.crud.annotation.TableTenantField;
import com.smart.framework.crud.annotation.TableUseYnField;
import com.smart.framework.crud.utils.CrudUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.Configuration;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.io.Serial;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * table info
 * 在plus原有基础上进行功能增强
 * @author shizhongming
 * 2024/3/11 16:22
 * @since 3.0.0
 */
@EqualsAndHashCode(callSuper = true)
public class SmartTableInfo extends TableInfo {
    @Serial
    private static final long serialVersionUID = -66149012141872877L;

    @Getter
    private TableInfo tableInfo;

    @Getter
    private List<TableFieldInfo> smartTableFieldInfoList;

    /**
     * @param configuration 配置对象
     * @param entityType    实体类型
     * @since 3.4.4
     */
    public SmartTableInfo(Configuration configuration, Class<?> entityType) {
        super(configuration, entityType);
    }

    public static SmartTableInfo create(TableInfo tableInfo) {
        List<String> methodNameList = new ArrayList<>(Arrays.stream(tableInfo.getClass().getMethods()).map(Method::getName).toList());
        methodNameList.removeAll(Arrays.stream(SmartTableInfo.class.getDeclaredMethods()).map(Method::getName).toList());
        ExtendMethodInterceptor<TableInfo> interceptor = new ExtendMethodInterceptor<>(tableInfo, methodNameList);
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(interceptor);
        enhancer.setSuperclass(SmartTableInfo.class);
        SmartTableInfo smartTableInfo =  (SmartTableInfo) enhancer.create(new Class[]{Configuration.class, Class.class}, new Object[]{tableInfo.getConfiguration(), tableInfo.getEntityType()});

        initField(smartTableInfo, tableInfo);
        smartTableInfo.tableInfo = tableInfo;
        return smartTableInfo;
    }

    /**
     * 启用停用field
     */
    @Getter
    private TableFieldInfo useYnField;

    /**
     * 删除field
     */
    @Getter
    private TableLogicDeleteInfo logicDeleteInfo;

    /**
     * 默认租户字段
     */
    private TableTenantFieldInfo defaultTenantFieldInfo;

    /**
     * 租户字段
     */
    private final Map<String, TableTenantFieldInfo> tenantFieldInfoMap = new HashMap<>();

    /**
     * 获取默认的租户字段信息
     * @return 默认的租户字段信息
     */
    public TableTenantFieldInfo getTenantFieldInfo() {
        return this.defaultTenantFieldInfo;
    }

    /**
     * 获取租户字段信息
     * @param fieldName java属性名
     * @return 租户字段信息
     */
    public TableTenantFieldInfo getTenantFieldInfo(String fieldName) {
        return tenantFieldInfoMap.get(fieldName);
    }

    /**
     * 获取租户字段信息
     * @param column 实体类字段
     * @return 租户字段信息
     */
    public <T> TableTenantFieldInfo getTenantFieldInfo(@NonNull SFunction<T, ?> column) {
        return getTenantFieldInfo(CrudUtils.getJavaProperty(column));
    }

    /**
     * 是否有逻辑删除key
     * @return 是否有逻辑删除key
     */
    public boolean hasTableLogicKey() {
        return Optional.ofNullable(this.logicDeleteInfo)
                .map(TableLogicDeleteInfo::getDeleteKeyFieldInfo)
                .orElse(null) != null;
    }

    /**
     * 是否支持租户
     * @return 是否支持租户
     */
    public boolean supportTenant() {
        return this.defaultTenantFieldInfo != null;
    }

    /**
     * 表字段是否启用了更新填充
     *
     * @since 3.3.0
     */
    @Override
    public boolean isWithUpdateFill() {
        return this.tableInfo.isWithUpdateFill() || (this.logicDeleteInfo != null && !this.logicDeleteInfo.getFillFieldInfoList().isEmpty());
    }

    @Override
    public List<TableFieldInfo> getFieldList() {
        return Collections.unmodifiableList(smartTableFieldInfoList);
    }

    /**
     * 通过fieldName 获取 TableFieldInfo
     * @param fieldName java属性名
     * @return TableFieldInfo
     */
    public TableFieldInfo getTableFiled(String fieldName) {
        List<TableFieldInfo> tableFieldInfoList = this.getFieldList().stream()
                .filter(item -> StringUtils.equals(item.getProperty(), fieldName))
                .toList();
        if (CollectionUtils.isEmpty(tableFieldInfoList)) {
            return null;
        }
        return tableFieldInfoList.getFirst();
    }

    /**
     * 通过fieldName 获取 TableFieldInfo
     * @param column java属性名
     * @return TableFieldInfo
     */
    public<T> TableFieldInfo getTableFiled(@NonNull SFunction<T, ?> column) {
        return this.getTableFiled(CrudUtils.getJavaProperty(column));
    }


    private static void initField(SmartTableInfo smartTableInfo, TableInfo tableInfo) {
        AtomicInteger useYnNum = new AtomicInteger();
        AtomicInteger defaultTenantNum = new AtomicInteger();
        tableInfo.getFieldList().forEach(field -> {
            // 处理启用停用字段
            TableUseYnField tableUseYnField = AnnotationUtils.getAnnotation(field.getField(), TableUseYnField.class);
            if (tableUseYnField != null) {
                smartTableInfo.useYnField = field;
                useYnNum.getAndAdd(1);
            }
            // 处理租户字段
            TableTenantField tableTenantField = AnnotationUtils.getAnnotation(field.getField(), TableTenantField.class);
            if (tableTenantField != null) {
                TableTenantFieldInfo tenantFieldInfo = TableTenantFieldInfo.builder()
                        .tableFieldInfo(field)
                        .ignoreCommandList(Arrays.asList(tableTenantField.ignoreCommands()))
                        .platformTenantIgnoreCommandList(Arrays.asList(tableTenantField.platformTenantIgnoreCommands()))
                        .defaultField(tableTenantField.isDefault())
                        .build();
                if (tenantFieldInfo.isDefaultField()) {
                    smartTableInfo.defaultTenantFieldInfo = tenantFieldInfo;
                    defaultTenantNum.getAndAdd(1);
                }
                smartTableInfo.tenantFieldInfoMap.put(field.getProperty(), tenantFieldInfo);
            }
        });
        Assert.isTrue(useYnNum.get() <= 1, "@TableUseYnField not support more than one in Class: \"%s\"", tableInfo.getEntityType().getName());
        Assert.isTrue(defaultTenantNum.get() <= 1, "@TableTenantField can only be one default in Class: \"%s\"", tableInfo.getEntityType().getName());

        // 初始化逻辑删除功能
        initLogicDelete(smartTableInfo, tableInfo);
        // 设置主键信息，
        initPkTableFieldInfo(smartTableInfo, tableInfo);
    }

    /**
     * 初始化逻辑删除
     * @param smartTableInfo smartTableInfo
     * @param tableInfo 原始table info
     */
    protected static void initLogicDelete(SmartTableInfo smartTableInfo, TableInfo tableInfo) {
        if (!tableInfo.isWithLogicDelete()) {
            return;
        }
        AtomicInteger deleteKeyNum = new AtomicInteger();
        TableLogicDeleteInfo tableLogicDeleteInfo = new TableLogicDeleteInfo();
        List<TableFieldInfo> deleteFillFieldInfoList = new ArrayList<>();
        for (TableFieldInfo fieldInfo : tableInfo.getFieldList()) {
            TableLogicField smartTableLogic = AnnotationUtils.getAnnotation(fieldInfo.getField(), TableLogicField.class);
            if (smartTableLogic == null) {
                continue;
            }
            if (smartTableLogic.isDeleteKey()) {
                int andAdd = deleteKeyNum.getAndIncrement();
                if (andAdd >= 1) {
                    throw new IllegalArgumentException("isDeleteKey true not support more than one in Class: " + tableInfo.getEntityType().getName());
                }
                tableLogicDeleteInfo.setDeleteKeyFieldInfo(fieldInfo);
                tableLogicDeleteInfo.setLogicKeyStrategy(smartTableLogic.strategy());
            } else if (smartTableLogic.isFill()) {
                deleteFillFieldInfoList.add(fieldInfo);
            }
        }
        tableLogicDeleteInfo.setFillFieldInfoList(deleteFillFieldInfoList);

        smartTableInfo.logicDeleteInfo = tableLogicDeleteInfo;
    }

    /**
     * 初始化主键 field info
     * @param smartTableInfo smartTableInfo
     * @param tableInfo tableInfo
     */
    protected static void initPkTableFieldInfo(SmartTableInfo smartTableInfo, TableInfo tableInfo) {
        if (StringUtils.isBlank(tableInfo.getKeyColumn())) {
            // 表没有主键不做处理
            smartTableInfo.smartTableFieldInfoList = new ArrayList<>(tableInfo.getFieldList());
            return;
        }
        Field keyField = ReflectionUtils.findField(tableInfo.getEntityType(), tableInfo.getKeyProperty());
        if (keyField == null) {
            throw new SystemException("系统发生错误，获取主键字段失败，实体类：" + tableInfo.getEntityType().getName());
        }
        Configuration configuration = tableInfo.getConfiguration();
        GlobalConfig globalConfig = GlobalConfigUtils.getGlobalConfig(configuration);
        TableFieldInfo keyTableFieldInfo = new TableFieldInfo(globalConfig, tableInfo, keyField, tableInfo.getReflector(), tableInfo.isWithLogicDelete(), false);
        globalConfig.getPostInitTableInfoHandler().postFieldInfo(keyTableFieldInfo, configuration);

        smartTableInfo.smartTableFieldInfoList = new ArrayList<>(tableInfo.getFieldList().size() + 1);
        smartTableInfo.smartTableFieldInfoList.add(keyTableFieldInfo);
        smartTableInfo.smartTableFieldInfoList.addAll(tableInfo.getFieldList());
    }
}
