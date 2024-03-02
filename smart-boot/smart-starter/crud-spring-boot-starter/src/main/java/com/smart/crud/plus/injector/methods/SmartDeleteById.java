package com.smart.crud.plus.injector.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.smart.crud.constants.SmartCrudConstants;
import com.smart.crud.plus.enums.SmartSqlMethod;
import com.smart.crud.utils.CrudUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.io.Serial;
import java.util.List;

import static com.smart.crud.constants.SmartCrudConstants.DELETE_FIELDS_DOT;
import static java.util.stream.Collectors.joining;

/**
 * 根据ID删除
 * 在plus的基础上，加强逻辑删除支持，解决唯一索引冲突问题
 * @author shizhongming
 * 2023/10/31 15:06
 * @since 3.0.0
 */
public class SmartDeleteById extends AbstractSmartMethod {

    @Serial
    private static final long serialVersionUID = 5112502573877941762L;

    /**
     * @since 3.5.0
     */
    public SmartDeleteById() {
        this(SqlMethod.DELETE_BY_ID.getMethod());
    }

    /**
     * @param methodName 方法名
     * @since 3.5.0
     */
    public SmartDeleteById(String methodName) {
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
        SqlMethod sqlMethod = SqlMethod.LOGIC_DELETE_BY_ID;
        SmartSqlMethod smartSqlMethod = SmartSqlMethod.LOGIC_DELETE_BY_ID;
        if (tableInfo.isWithLogicDelete()) {
            List<TableFieldInfo> fieldInfos = tableInfo.getFieldList().stream()
                    .filter(TableFieldInfo::isWithUpdateFill)
                    .filter(f -> !f.isLogicDelete())
                    .toList();
            boolean hasTableLogicKey = CrudUtils.hasTableLogicKey(tableInfo);
            String deleteId = SmartCrudConstants.DELETE_ID;
            if (CollectionUtils.isNotEmpty(fieldInfos)) {
                String sqlSet = SqlScriptUtils.convertIf(fieldInfos.stream()
                        .map(i -> i.getSqlSet(EMPTY)).collect(joining(EMPTY)), "!@org.apache.ibatis.type.SimpleTypeRegistry@isSimpleType(_parameter.getClass())", true)
                        + tableInfo.getLogicDeleteSql(false, false);
                if (hasTableLogicKey) {
                    sql = String.format(smartSqlMethod.getSql(), tableInfo.getTableName(),
                            SqlScriptUtils.convertSet(sqlSet + ", \n" + this.sqlLogicDeleteFieldSet(tableInfo, DELETE_FIELDS_DOT)),
                            tableInfo.getKeyColumn(),
                            deleteId, tableInfo.getLogicDeleteSql(true, true));
                } else {
                    sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), sqlSet, tableInfo.getKeyColumn(),
                            deleteId, tableInfo.getLogicDeleteSql(true, true));
                }
            } else {
                sql = String.format(smartSqlMethod.getSql(), tableInfo.getTableName(),
                        SqlScriptUtils.convertSet(this.sqlLogicDeleteFieldSet(tableInfo, DELETE_FIELDS_DOT)),
                        tableInfo.getKeyColumn(), deleteId,
                        tableInfo.getLogicDeleteSql(true, true));
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
            return addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
        } else {
            sqlMethod = SqlMethod.DELETE_BY_ID;
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), tableInfo.getKeyColumn(),
                    tableInfo.getKeyProperty());
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
            return this.addDeleteMappedStatement(mapperClass, methodName, sqlSource);
        }
    }
}
