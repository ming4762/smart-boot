package com.smart.crud.plus.injector.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.methods.DeleteBatchByIds;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.smart.crud.plus.enums.SmartSqlMethod;
import com.smart.crud.plus.metadata.TableLogicKeyFieldInfo;
import com.smart.crud.utils.CrudUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 根据集合删除
 * 在plus的基础上，加强逻辑删除支持，解决唯一索引冲突问题
 * @author shizhongming
 * 2023/10/31 13:47
 * @since 3.0.0
 */
public class SmartDeleteBatchByIds extends AbstractSmartMethod {


    public SmartDeleteBatchByIds() {
        super(SqlMethod.DELETE_BATCH_BY_IDS.getMethod());
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
        SqlMethod sqlMethod = SqlMethod.LOGIC_DELETE_BATCH_BY_IDS;
        if (tableInfo.isWithLogicDelete()) {
            TableLogicKeyFieldInfo tableLogicKeyField = CrudUtils.getTableLogicKeyField(tableInfo);
            if (tableLogicKeyField == null) {
                sql = new DeleteBatchByIds().logicDeleteScript(tableInfo, sqlMethod);
            } else {
                // 设置了逻辑删除key
               sql = this.logicDeleteScriptWithDeleteKey(tableInfo, tableLogicKeyField);
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
            return addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
        } else {
            sqlMethod = SqlMethod.DELETE_BATCH_BY_IDS;
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), tableInfo.getKeyColumn(),
                    SqlScriptUtils.convertForeach(
                            SqlScriptUtils.convertChoose("@org.apache.ibatis.type.SimpleTypeRegistry@isSimpleType(item.getClass())",
                                    "#{item}", "#{item." + tableInfo.getKeyProperty() + "}"),
                            COLL, null, "item", COMMA));
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
            return this.addDeleteMappedStatement(mapperClass, methodName, sqlSource);
        }
    }

    protected String logicDeleteScriptWithDeleteKey(TableInfo tableInfo, TableLogicKeyFieldInfo tableLogicKeyField) {
        return String.format(SmartSqlMethod.LOGIC_DELETE_BATCH_BY_IDS_WITH_KEY.getSql(),
                tableInfo.getTableName(),
                sqlLogicSet(tableInfo),
                this.sqlLogicKeySet(tableInfo),
                tableInfo.getKeyColumn(),
                SqlScriptUtils.convertForeach(
                        SqlScriptUtils.convertChoose("@org.apache.ibatis.type.SimpleTypeRegistry@isSimpleType(item.getClass())",
                                "#{item}", "#{item." + tableInfo.getKeyProperty() + "}"),
                        COLL, null, "item", COMMA),
                tableInfo.getLogicDeleteSql(true, true));
    }
}
