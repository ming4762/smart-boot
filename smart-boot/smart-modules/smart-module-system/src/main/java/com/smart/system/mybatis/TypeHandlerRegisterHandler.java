package com.smart.system.mybatis;

import com.smart.system.mybatis.type.*;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ShiZhongMing
 * 2022/8/18
 * @since 3.0.0
 */
@Component
public class TypeHandlerRegisterHandler implements InitializingBean {

    private static final List<Class<? extends TypeHandler<?>>> TYPE_HANDLER_CLASS = List.of(
            FunctionTypeTypeHandler.class,
            LogSourceTypeHandler.class,
            MaxConnectionsPolicyTypeHandler.class,
            UserAccountStatusTypeHandler.class
    );

    private final SqlSessionFactory sqlSessionFactory;

    public TypeHandlerRegisterHandler(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void afterPropertiesSet() {
        TypeHandlerRegistry registry = this.sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();
        TYPE_HANDLER_CLASS.forEach(registry::register);
    }
}
