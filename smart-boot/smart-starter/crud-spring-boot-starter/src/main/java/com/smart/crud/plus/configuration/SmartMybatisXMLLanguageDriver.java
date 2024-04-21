package com.smart.crud.plus.configuration;

import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * @author shizhongming
 * 2024/4/20 19:40
 * @since 3.0.0
 */
public class SmartMybatisXMLLanguageDriver extends MybatisXMLLanguageDriver {

    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new SmartMybatisParameterHandler(mappedStatement, parameterObject, boundSql);
    }
}
