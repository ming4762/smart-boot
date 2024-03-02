package com.smart.crud.plus.injector.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.smart.crud.plus.enums.SmartSqlMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.io.Serial;

import static com.smart.crud.constants.SmartCrudConstants.DELETE_FIELDS_DOT;

/**
 * @author shizhongming
 * 2023/10/31 15:12
 * @since 3.0.0
 */
public class SmartDelete extends AbstractSmartMethod{

    @Serial
    private static final long serialVersionUID = 8701454358884931568L;

    /**
     * @since 3.5.0
     */
    public SmartDelete() {
        this(SqlMethod.DELETE.getMethod());
    }

    /**
     * @param methodName 方法名
     * @since 3.5.0
     */
    public SmartDelete(String methodName) {
        super(methodName);
    }

    /**
     * 注入自定义 MappedStatement
     *
     * @param mapperClass mapper 接口
     * @param modelClass  mapper 泛型
     * @param tableInfo   数据库表反射信息
     * @return MappedStatement
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql;
        SmartSqlMethod smartSqlMethod = SmartSqlMethod.LOGIC_DELETE;
        if (tableInfo.isWithLogicDelete()) {
            sql = String.format(smartSqlMethod.getSql(), tableInfo.getTableName(),
                    SqlScriptUtils.convertSet(this.sqlLogicDeleteFieldSet(tableInfo, DELETE_FIELDS_DOT)),
                    sqlWhereEntityWrapper(true, tableInfo),
                    sqlComment());
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
            return addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
        } else {
            SqlMethod sqlMethod = SqlMethod.DELETE;
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(),
                    sqlWhereEntityWrapper(true, tableInfo),
                    sqlComment());
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
            return this.addDeleteMappedStatement(mapperClass, methodName, sqlSource);
        }
    }
}
