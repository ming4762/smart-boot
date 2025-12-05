package com.smart.module.api.system;

import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.commons.core.utils.ReflectUtils;
import com.smart.module.api.system.constants.SmartChangeLogEnum;
import com.smart.module.api.system.dto.SerialCodeCreateDTO;
import com.smart.module.api.system.dto.SmartChangeLogListDTO;
import com.smart.module.api.system.parameter.CommonChangeLogSaveParameter;
import com.smart.module.api.system.parameter.RemoteChangeLogListParameter;
import com.smart.module.api.system.parameter.RemoteChangeLogSaveParameter;
import com.smart.module.api.system.parameter.SerialCodeCreateParameter;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 系统工具API
 * @author zhongming4762
 * 2023/6/2
 */
public interface SysToolApi {

    Set<String> DEFAULT_EXCLUDE_LIST = Set.of(
            "createUserId",
            "createBy",
            "createTime",
            "updateUserId",
            "updateBy",
            "updateTime"
    );

    /**
     * 生成业务编码
     * @param parameter 参数
     * @return 业务编码
     */
    SerialCodeCreateDTO createSerial(SerialCodeCreateParameter parameter);

    /**
     * 批量生成业务编码
     * @param parameterList 参数列表
     * @return 业务编码列表
     */
    List<SerialCodeCreateDTO> createSerial(List<SerialCodeCreateParameter> parameterList);

    /**
     * 保存修改记录
     * TODO: TODO_结合mybatis plus数据变动记录插件
     * @param parameter 参数
     * @return 是否保存成功
     */
    boolean saveChangeLog(RemoteChangeLogSaveParameter parameter);

    /**
     * 保存修改记录
     * @param parameter 参数
     * @param beforeData 原数据
     * @param afterData 修改后的数据
     * @return 是否修改成功
     * @param <T> 泛型
     */
    default <T> boolean saveChangeLog(CommonChangeLogSaveParameter parameter, @Nullable T beforeData, @Nullable T afterData) {
        return this.saveChangeLog(parameter, beforeData, afterData ,null, DEFAULT_EXCLUDE_LIST);
    }

    /**
     * 保存修改记录
     * @param parameter 参数
     * @param beforeData 原数据
     * @param afterData 修改后的数据
     * @param fieldList 保存的字段列表，null则保存所有
     * @param excludeList 排除的字段列表
     * @return 是否修改成功
     * @param <T> 泛型
     */
    default <T> boolean saveChangeLog(CommonChangeLogSaveParameter parameter, @Nullable T beforeData, @Nullable T afterData, Set<String> fieldList, Set<String> excludeList) {
        if (excludeList == null) {
            excludeList = DEFAULT_EXCLUDE_LIST;
        }
        if (beforeData == null && afterData == null) {
            throw new SystemException("记录日志错误，原数据和修改后数据不能同时为空");
        }
        // 设置修改记录类型
        parameter.setOperateType(determineOperateType(beforeData, afterData, parameter.getOperateType()));

        Class<?> clazz = Optional.ofNullable(beforeData).orElse(afterData).getClass();
        RemoteChangeLogSaveParameter remoteChangeLogSaveParameter = new RemoteChangeLogSaveParameter();
        BeanUtils.copyProperties(parameter, remoteChangeLogSaveParameter);
        // 删除和新增操作不保存详细修改记录
        if (SmartChangeLogEnum.UPDATE.equals(parameter.getOperateType()) || this.isSaveCreateDetail(parameter)) {
            List<RemoteChangeLogSaveParameter.Detail> detailList =
                    buildDetailList(beforeData, afterData, clazz, fieldList, excludeList, parameter.getIgnoreAfterNull());

            remoteChangeLogSaveParameter.setDetailList(detailList);

            if (CollectionUtils.isEmpty(detailList)) {
                return true;
            }
        }
        return this.saveChangeLog(remoteChangeLogSaveParameter);
    }

    /**
     * 确定操作类型
     * @param beforeData 原数据
     * @param afterData 修改后的数据
     * @param operateType 操作类型
     * @return 操作类型
     */
    private SmartChangeLogEnum determineOperateType(Object beforeData, Object afterData, SmartChangeLogEnum operateType) {
        if (operateType != null) {
            return operateType;
        }
        if (beforeData == null) {
            return SmartChangeLogEnum.CREATE;
        }
        if (afterData == null) {
            return SmartChangeLogEnum.DELETE;
        }
        return SmartChangeLogEnum.UPDATE;
    }

    /**
     * 构建修改记录详情列表
     * @param beforeData 原数据
     * @param afterData 修改后的数据
     * @param clazz 类型
     * @param fieldList 保存的字段列表，null则保存所有
     * @param excludeList 排除的字段列表
     * @param ignoreAfterNull 是否忽略修改为null
     * @return 修改记录详情列表
     */
    private List<RemoteChangeLogSaveParameter.Detail> buildDetailList(
            Object beforeData, Object afterData, Class<?> clazz,
            Set<String> fieldList, Set<String> excludeList, boolean ignoreAfterNull) {

        Set<Field> fields = HashSet.newHashSet(16);
        ReflectUtils.getAllFields(clazz, fields);

        return fields.stream()
                .filter(f -> CollectionUtils.isEmpty(fieldList) || fieldList.contains(f.getName()))
                .filter(f -> CollectionUtils.isEmpty(excludeList) || !excludeList.contains(f.getName()))
                .map(f -> this.getDetail(ignoreAfterNull, clazz, f, beforeData, afterData))
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 是否保存添加详情
     * @param parameter 参数
     * @return 是否保存添加详情
     */
    private boolean isSaveCreateDetail(CommonChangeLogSaveParameter parameter) {
        return SmartChangeLogEnum.CREATE.equals(parameter.getOperateType()) && Boolean.TRUE.equals(parameter.getSaveCreateDetail());
    }


    /**
     * 获取详情
     * 如果值相同则返回null
     * @param aClass 类型
     * @param field Field
     * @param beforeData beforeData
     * @param afterData afterData
     * @param ignoreAfterNull 是否忽略修改为null
     * @return 如果值相同则返回null
     */
    @SneakyThrows(Exception.class)
    private <T> RemoteChangeLogSaveParameter.Detail getDetail(Boolean ignoreAfterNull, Class<?> aClass, Field field, @Nullable T beforeData, @Nullable T afterData) {
        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(aClass, field.getName());
        if (propertyDescriptor == null) {
            throw new SystemException(String.format("保存记录失败，实体类无法获取getter函数，class：%s，字典：%s", aClass.getName(), field.getName()));
        }
        Method readMethod = propertyDescriptor.getReadMethod();
        Object beforeValue = beforeData == null ? null : readMethod.invoke(beforeData);
        Object afterValue = afterData == null ? null : readMethod.invoke(afterData);
        if (Objects.equals(beforeValue, afterValue)) {
            return null;
        }
        if (Boolean.TRUE.equals(ignoreAfterNull) && afterValue == null) {
            return null;
        }
        return new RemoteChangeLogSaveParameter.Detail(
                field.getName(),
                Optional.ofNullable(beforeValue).map(Object::toString).orElse(null),
                Optional.ofNullable(afterValue).map(Object::toString).orElse(null)
        );
    }

    /**
     * 查询修改记录
     * @param parameter 参数
     * @return 修改记录列表
     */
    List<SmartChangeLogListDTO> listChangeLog(RemoteChangeLogListParameter parameter);

}
