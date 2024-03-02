package com.smart.crud.plus.injector.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.smart.crud.plus.enums.SmartSqlMethod;
import com.smart.crud.utils.CrudUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.io.Serial;
import java.util.Map;

/**
 * @author shizhongming
 * 2023/10/31 15:18
 * @since 3.0.0
 */
public class SmartDeleteByMap extends AbstractSmartMethod {

    @Serial
    private static final long serialVersionUID = -7443604353347817047L;

    /**
     * @since 3.5.0
     */
    public SmartDeleteByMap() {
        this(SqlMethod.DELETE_BY_MAP.getMethod());
    }

    /**
     * @param methodName 方法名
     * @since 3.5.0
     */
    public SmartDeleteByMap(String methodName) {
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
        SqlMethod sqlMethod = SqlMethod.LOGIC_DELETE_BY_MAP;
        if (tableInfo.isWithLogicDelete()) {
            if (CrudUtils.hasTableLogicKey(tableInfo)) {
                sql = String.format(SmartSqlMethod.LOGIC_DELETE_BY_MAP.getSql(), tableInfo.getTableName(), sqlLogicSet(tableInfo), this.sqlLogicDeleteFieldSet(tableInfo, ENTITY_DOT), sqlWhereByMap(tableInfo));
            } else {
                sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), sqlLogicSet(tableInfo), sqlWhereByMap(tableInfo));
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Map.class);
            return addUpdateMappedStatement(mapperClass, Map.class, methodName, sqlSource);
        } else {
            sqlMethod = SqlMethod.DELETE_BY_MAP;
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), this.sqlWhereByMap(tableInfo));
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Map.class);
            return this.addDeleteMappedStatement(mapperClass, methodName, sqlSource);
        }
    }
}
