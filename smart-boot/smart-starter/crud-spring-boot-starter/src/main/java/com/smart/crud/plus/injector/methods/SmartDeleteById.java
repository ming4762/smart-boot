package com.smart.crud.plus.injector.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.methods.DeleteById;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.smart.crud.plus.enums.SmartSqlMethod;
import com.smart.crud.plus.metadata.SmartTableInfo;
import com.smart.crud.utils.CrudUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.io.Serial;

import static com.smart.crud.constants.SmartCrudConstants.DELETE_FIELDS_DOT;

/**
 * 根据ID删除
 * 在plus的基础上，加强逻辑删除支持，解决唯一索引冲突问题
 * @author shizhongming
 * 2023/10/31 15:06
 * @since 3.0.0
 */
public class SmartDeleteById extends DeleteById implements AbstractSmartMethod {

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
        SmartSqlMethod smartSqlMethod = SmartSqlMethod.LOGIC_DELETE_BY_ID;
        SmartTableInfo smartTableInfo = CrudUtils.getTableInfo(modelClass);
        if (smartTableInfo.isWithLogicDelete()) {
            boolean withUpdateFill = smartTableInfo.isWithUpdateFill();
//            String deleteId = withUpdateFill ? tableInfo.getKeyProperty() : SmartCrudConstants.DELETE_ID;
            String prefix = withUpdateFill ? EMPTY : DELETE_FIELDS_DOT;
            String sql = String.format(smartSqlMethod.getSql(), tableInfo.getTableName(),
                    SqlScriptUtils.convertSet(this.sqlLogicDeleteFieldSet(tableInfo, prefix, true)),
                    tableInfo.getKeyColumn(), tableInfo.getKeyProperty(),
                    tableInfo.getLogicDeleteSql(true, true));
            SqlSource sqlSource = super.createSqlSource(configuration, sql, Object.class);
            return addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
        }
        return super.injectMappedStatement(mapperClass, modelClass, tableInfo);
    }
}
