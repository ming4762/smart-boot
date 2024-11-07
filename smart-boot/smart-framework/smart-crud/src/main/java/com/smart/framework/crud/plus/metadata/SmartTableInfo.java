package com.smart.framework.crud.plus.metadata;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.commons.core.proxy.ExtendMethodInterceptor;
import com.smart.framework.crud.annotation.TableLogicField;
import com.smart.framework.crud.annotation.TableTenantField;
import com.smart.framework.crud.annotation.TableUseYnField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;
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
@Getter
@EqualsAndHashCode(callSuper = true)
public class SmartTableInfo extends TableInfo {
    @Serial
    private static final long serialVersionUID = -66149012141872877L;

    private TableInfo tableInfo;

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
    private TableFieldInfo useYnField;

    /**
     * 删除field
     */
    private TableLogicDeleteInfo logicDeleteInfo;

    /**
     * 租户字段
     */
    private TableTenantFieldInfo tenantFieldInfo;

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
        return this.tenantFieldInfo != null;
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
        return tableFieldInfoList.get(0);
    }

    /**
     * 通过fieldName 获取 TableFieldInfo
     * @param column java属性名
     * @return TableFieldInfo
     */
    public<T> TableFieldInfo getTableFiled(@NonNull SFunction<T, ?> column) {
        LambdaMeta meta = LambdaUtils.extract(column);
        String property = PropertyNamer.methodToProperty(meta.getImplMethodName());
        return this.getTableFiled(property);
    }


    private static void initField(SmartTableInfo smartTableInfo, TableInfo tableInfo) {
        AtomicInteger useYnNum = new AtomicInteger();
        AtomicInteger tenantNum = new AtomicInteger();
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
                smartTableInfo.tenantFieldInfo = TableTenantFieldInfo.builder()
                        .tableFieldInfo(field)
                        .ignoreCommandList(Arrays.asList(tableTenantField.ignoreCommands()))
                        .platformTenantIgnoreCommandList(Arrays.asList(tableTenantField.platformTenantIgnoreCommands()))
                        .build();
                tenantNum.getAndAdd(1);
            }
        });
        Assert.isTrue(useYnNum.get() <= 1, "@TableUseYnField not support more than one in Class: \"%s\"", tableInfo.getEntityType().getName());
        Assert.isTrue(tenantNum.get() <= 1, "@TableTenantField not support more than one in Class: \"%s\"", tableInfo.getEntityType().getName());

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
