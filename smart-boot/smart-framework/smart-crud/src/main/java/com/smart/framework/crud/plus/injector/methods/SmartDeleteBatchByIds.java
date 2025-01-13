package com.smart.framework.crud.plus.injector.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.methods.DeleteByIds;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.io.Serial;

import static com.smart.framework.crud.constants.SmartCrudConstants.DELETE_FIELDS_DOT;

/**
 * 根据集合删除
 * 在plus的基础上，加强逻辑删除支持，解决唯一索引冲突问题
 * @author shizhongming
 * 2023/10/31 13:47
 * @since 3.0.0
 */
@Deprecated
public class SmartDeleteBatchByIds extends DeleteByIds implements AbstractSmartMethod {


    @Serial
    private static final long serialVersionUID = -1282922352525893633L;

    public SmartDeleteBatchByIds() {
        super(SqlMethod.DELETE_BATCH_BY_IDS.getMethod());
    }

    /**
     * 注入自定义 MappedStatement
     *
     * @param mapperClass com.smart.framework.tool.code.mapper 接口
     * @param modelClass  com.smart.framework.tool.code.mapper 泛型
     * @param tableInfo   数据库表反射信息
     * @return MappedStatement
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        // 如果启用了逻辑删除并且没有启用更新注入，则使用自定义逻辑，通过修改SQL注入
        if (tableInfo.isWithLogicDelete() && !tableInfo.isWithUpdateFill()) {
            String sql = this.logicDeleteScriptWithDeleteKey(tableInfo);
            SqlSource sqlSource = super.createSqlSource(configuration, sql, Object.class);
            return addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
        }
        // 使用plus原有逻辑注入
        return super.injectMappedStatement(mapperClass, modelClass, tableInfo);
    }

    protected String logicDeleteScriptWithDeleteKey(TableInfo tableInfo) {
        return String.format(SqlMethod.LOGIC_DELETE_BATCH_BY_IDS.getSql(),
                tableInfo.getTableName(),
                SqlScriptUtils.convertSet(this.sqlLogicDeleteFieldSet(tableInfo, DELETE_FIELDS_DOT, false)),
                tableInfo.getKeyColumn(),
                SqlScriptUtils.convertForeach(
                        SqlScriptUtils.convertChoose("@org.apache.ibatis.com.smart.framework.tool.code.type.SimpleTypeRegistry@isSimpleType(item.getClass())",
                                "#{item}", "#{item." + tableInfo.getKeyProperty() + "}"),
                        COLL, null, "item", COMMA),
                tableInfo.getLogicDeleteSql(true, true));
    }
}
