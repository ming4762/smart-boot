package com.smart.crud.mybatis.plugin;

import com.smart.crud.constants.UserPropertyEnum;
import com.smart.crud.service.UserProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 使用mybatis拦截器自动设置创建人、创建时间、更新人、更新时间
 * @author shizhongming
 * 2022/12/16 20:57
 */
@Slf4j
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class CreateUpdateUserTimeMybatisInterceptor implements Interceptor {

    private final UserProvider userProvider;

    public CreateUpdateUserTimeMybatisInterceptor(UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        if (parameter == null) {
            // 没有参数不做任何操作
            return invocation.proceed();
        }
        if (parameter.getClass().equals(MapperMethod.ParamMap.class)) {
            parameter = ((MapperMethod.ParamMap<?>) parameter).values().iterator().next();
        }
        if (sqlCommandType == SqlCommandType.INSERT) {
            // 插入操作
            Long userId = this.userProvider.getCurrentUserId();
            String fullName = this.userProvider.getCurrentUserFullName();
            // 创建人
            PropertyDescriptor createUserIdDescriptor = BeanUtils.getPropertyDescriptor(parameter.getClass(), UserPropertyEnum.CREATE_USER_ID.getName());
            if (createUserIdDescriptor != null) {
                createUserIdDescriptor.getWriteMethod().invoke(parameter, userId);
            }
            // 创建人名字
            PropertyDescriptor createUserDescriptor =  BeanUtils.getPropertyDescriptor(parameter.getClass(), UserPropertyEnum.CREATE_USER.getName());
            if (createUserDescriptor != null) {
                createUserDescriptor.getWriteMethod().invoke(parameter, fullName);
            }
            // 创建时间
            PropertyDescriptor createTimeDescriptor =  BeanUtils.getPropertyDescriptor(parameter.getClass(), UserPropertyEnum.CREATE_TIME.getName());
            this.setTime(createTimeDescriptor, parameter);
        }
        if (sqlCommandType == SqlCommandType.UPDATE) {
            // 更新操作
            Long userId = this.userProvider.getCurrentUserId();
            String fullName = this.userProvider.getCurrentUserFullName();
            // 更新人
            PropertyDescriptor updateUserIdDescriptor = BeanUtils.getPropertyDescriptor(parameter.getClass(), UserPropertyEnum.UPDATE_USER_ID.getName());
            if (updateUserIdDescriptor != null) {
                updateUserIdDescriptor.getWriteMethod().invoke(parameter, userId);
            }
            // 创建人名字
            PropertyDescriptor updateUserDescriptor =  BeanUtils.getPropertyDescriptor(parameter.getClass(), UserPropertyEnum.UPDATE_USER.getName());
            if (updateUserDescriptor != null) {
                updateUserDescriptor.getWriteMethod().invoke(parameter, fullName);
            }
            // 创建时间
            PropertyDescriptor updateTimeDescriptor =  BeanUtils.getPropertyDescriptor(parameter.getClass(), UserPropertyEnum.UPDATE_TIME.getName());
            this.setTime(updateTimeDescriptor, parameter);
        }
        return invocation.proceed();
    }

    @SneakyThrows
    private void setTime(PropertyDescriptor propertyDescriptor, Object data) {
        if (propertyDescriptor != null) {
            if (propertyDescriptor.getPropertyType().equals(LocalDateTime.class)) {
                propertyDescriptor.getWriteMethod().invoke(data, LocalDateTime.now());
            } else if (propertyDescriptor.getPropertyType().equals(Date.class)) {
                propertyDescriptor.getWriteMethod().invoke(data, new Date());
            } else {
                log.warn("createTime类型不匹配，无法自动设置，类型：{}", propertyDescriptor.getPropertyType().getName());
            }
        }
    }
}
