package com.smart.crud.plus.metadata;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.smart.commons.core.proxy.ExtendMethodInterceptor;
import com.smart.crud.annotation.TableTenantField;
import com.smart.crud.annotation.TableUseYnField;
import com.smart.crud.constants.UserPropertyEnum;
import com.smart.crud.plus.logic.TableLogicKey;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.apache.ibatis.session.Configuration;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    /**
     * @param configuration 配置对象
     * @param entityType    实体类型
     * @since 3.4.4
     */
    public SmartTableInfo(Configuration configuration, Class<?> entityType) {
        super(configuration, entityType);
    }

    public static SmartTableInfo create(TableInfo tableInfo) {
        List<String> methodNameList = Arrays.stream(tableInfo.getClass().getMethods()).map(Method::getName).toList();
        ExtendMethodInterceptor<TableInfo> interceptor = new ExtendMethodInterceptor<>(tableInfo, methodNameList);
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(interceptor);
        enhancer.setSuperclass(SmartTableInfo.class);
        SmartTableInfo smartTableInfo =  (SmartTableInfo) enhancer.create(new Class[]{Configuration.class, Class.class}, new Object[]{tableInfo.getConfiguration(), tableInfo.getEntityType()});

        initField(smartTableInfo, tableInfo);
        return smartTableInfo;
    }

    /**
     * 启用停用field
     */
    private TableFieldInfo useYnField;

    /**
     * 删除field
     */
    private TableLogicDeleteFieldInfo deleteField;

    /**
     * 租户字段
     */
    private TableTenantFieldInfo tenantFieldInfo;

    /**
     * 是否有逻辑删除key
     * @return 是否有逻辑删除key
     */
    public boolean hasTableLogicKey() {
        return Optional.ofNullable(this.deleteField)
                .map(TableLogicDeleteFieldInfo::getTableLogicKey)
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
                smartTableInfo.tenantFieldInfo = new TableTenantFieldInfo(field, Arrays.asList(tableTenantField.excludeCommands()));
                tenantNum.getAndAdd(1);
            }
        });
        Assert.isTrue(useYnNum.get() <= 1, "@TableUseYnField not support more than one in Class: \"%s\"", tableInfo.getEntityType().getName());
        Assert.isTrue(tenantNum.get() <= 1, "@TableTenantField not support more than one in Class: \"%s\"", tableInfo.getEntityType().getName());
        if (tableInfo.isWithLogicDelete()) {
            List<TableFieldInfo> logicDeleteKeyFields = tableInfo.getFieldList().stream().filter(item -> {
                TableLogicKey tableLogicKey = AnnotationUtils.getAnnotation(item.getField(), TableLogicKey.class);
                return tableLogicKey != null;
            }).toList();
            TableLogicDeleteFieldInfo deleteFieldInfo = getTableLogicDeleteFieldInfo(tableInfo);
            if (!CollectionUtils.isEmpty(logicDeleteKeyFields)) {
                Assert.isTrue(logicDeleteKeyFields.size() <= 1, "@TableLogicKey not support more than one in Class: \"%s\"", tableInfo.getEntityType().getName());
                deleteFieldInfo.setDeleteKeyFieldInfo(logicDeleteKeyFields.get(0));
                deleteFieldInfo.setTableLogicKey(AnnotationUtils.getAnnotation(logicDeleteKeyFields.get(0).getField(), TableLogicKey.class));
            }
            smartTableInfo.deleteField = deleteFieldInfo;
        }
    }


    private static TableLogicDeleteFieldInfo getTableLogicDeleteFieldInfo(TableInfo tableInfo) {
        TableLogicDeleteFieldInfo deleteFieldInfo = new TableLogicDeleteFieldInfo();
        for (TableFieldInfo tableFieldInfo : tableInfo.getFieldList()) {
            if (UserPropertyEnum.DELETE_TIME.getName().equals(tableFieldInfo.getProperty())) {
                deleteFieldInfo.setDeleteTimeFieldInfo(tableFieldInfo);
            }
            if (UserPropertyEnum.DELETE_BY.getName().equals(tableFieldInfo.getProperty())) {
                deleteFieldInfo.setDeleteByFieldInfo(tableFieldInfo);
            }
            if (UserPropertyEnum.DELETE_USER_ID.getName().equals(tableFieldInfo.getProperty())) {
                deleteFieldInfo.setDeleteUserIdFieldInfo(tableFieldInfo);
            }
        }
        return deleteFieldInfo;
    }

}
