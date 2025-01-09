package com.smart.framework.crud.plus.injector.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.methods.DeleteByIds;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author shizhongming
 * 2025/1/8 15:33
 * @since 5.0.0
 */
public class SmartDeleteByIds extends DeleteByIds implements AbstractSmartMethod {

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
        if (tableInfo.isWithLogicDelete()) {
            String sql = this.logicDeleteScriptWithDeleteKey(tableInfo);
            SqlSource sqlSource = super.createSqlSource(configuration, sql, Object.class);
            return addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
        }
        // 使用plus原有逻辑注入
        return super.injectMappedStatement(mapperClass, modelClass, tableInfo);
    }

    protected String logicDeleteScriptWithDeleteKey(TableInfo tableInfo) {
        // 查询固定的字段
        String commonField = this.sqlLogicDeleteFieldCommonSet(tableInfo, Constants.MP_FILL_ET + Constants.DOT, false, true, false);
        String fillField = this.sqlLogicDeleteFieldCommonSet(tableInfo, Constants.MP_FILL_ET + Constants.DOT, true, false, true);

        if (StringUtils.isNotBlank(fillField)) {
            fillField = SqlScriptUtils.convertIf(fillField, String.format("%s != null", Constants.MP_FILL_ET), true);
        }
        String sqlSet = "SET " + String.join(StringPool.EMPTY, fillField, commonField);
        return String.format(
                SqlMethod.LOGIC_DELETE_BY_IDS.getSql(),
                tableInfo.getTableName(),
                sqlSet,
                tableInfo.getKeyColumn(),
                SqlScriptUtils.convertForeach(
                        SqlScriptUtils.convertChoose("@org.apache.ibatis.type.SimpleTypeRegistry@isSimpleType(item.getClass())",
                                "#{item}", "#{item." + tableInfo.getKeyProperty() + "}"),
                        COLL, null, "item", COMMA),
                tableInfo.getLogicDeleteSql(true, true)
        );
    }
}
