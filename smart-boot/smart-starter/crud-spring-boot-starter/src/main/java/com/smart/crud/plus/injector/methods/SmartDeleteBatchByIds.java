package com.smart.crud.plus.injector.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.smart.crud.plus.enums.SmartSqlMethod;
import com.smart.crud.plus.metadata.TableLogicDeleteFieldInfo;
import com.smart.crud.utils.CrudUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.io.Serial;

import static com.smart.crud.constants.SmartCrudConstants.DELETE_FIELDS_DOT;

/**
 * 根据集合删除
 * 在plus的基础上，加强逻辑删除支持，解决唯一索引冲突问题
 * @author shizhongming
 * 2023/10/31 13:47
 * @since 3.0.0
 */
public class SmartDeleteBatchByIds extends AbstractSmartMethod {


    @Serial
    private static final long serialVersionUID = -1282922352525893633L;

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
        if (tableInfo.isWithLogicDelete()) {
            sql = this.logicDeleteScriptWithDeleteKey(tableInfo);
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
            return addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
        } else {
            SqlMethod sqlMethod = SqlMethod.DELETE_BATCH_BY_IDS;
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), tableInfo.getKeyColumn(),
                    SqlScriptUtils.convertForeach(
                            SqlScriptUtils.convertChoose("@org.apache.ibatis.type.SimpleTypeRegistry@isSimpleType(item.getClass())",
                                    "#{item}", "#{item." + tableInfo.getKeyProperty() + "}"),
                            COLL, null, "item", COMMA));
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
            return this.addDeleteMappedStatement(mapperClass, methodName, sqlSource);
        }
    }

    protected String logicDeleteScriptWithDeleteKey(TableInfo tableInfo) {
        return String.format(SmartSqlMethod.LOGIC_DELETE_BATCH_BY_IDS_WITH_KEY.getSql(),
                tableInfo.getTableName(),
                SqlScriptUtils.convertSet(this.sqlLogicDeleteFieldSet(tableInfo, DELETE_FIELDS_DOT)),
                tableInfo.getKeyColumn(),
                SqlScriptUtils.convertForeach(
                        SqlScriptUtils.convertChoose("@org.apache.ibatis.type.SimpleTypeRegistry@isSimpleType(item.getClass())",
                                "#{item}", "#{item." + tableInfo.getKeyProperty() + "}"),
                        COLL, null, "item", COMMA),
                tableInfo.getLogicDeleteSql(true, true));
    }
}
