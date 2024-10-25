package com.smart.crud.plus.configuration;

import com.baomidou.mybatisplus.core.MybatisParameterHandler;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.smart.crud.constants.CrudConstants;
import com.smart.crud.plus.fill.SmartMetaObjectFill;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;

/**
 * 自定义的MybatisParameterHandler
 * @author shizhongming
 * 2024/4/20 19:41
 * @since 3.0.0
 */
public class SmartMybatisParameterHandler extends MybatisParameterHandler {

    private final MappedStatement mappedStatement;

    /**
     * 是否是逻辑删除
     */
    private final boolean logicDeleteMethod;


    public SmartMybatisParameterHandler(MappedStatement mappedStatement, Object parameter, BoundSql boundSql) {
        super(mappedStatement, parameter, boundSql);
        this.mappedStatement = mappedStatement;
        this.logicDeleteMethod = CrudConstants.LOGIC_DELETE_METHODS.stream().anyMatch(item -> mappedStatement.getId().endsWith(item));
        super.processParameter(parameter);
    }

    /**
     * plus 直接在构造函数中处理参数，此时mappedStatement是null，导致重写的insertFill、updateFill无法获取mappedStatement
     * 重写此函数，然后设置完mappedStatement，在执行super.processParameter
     * @param parameter 原始参数
     */
    @Override
    public void processParameter(Object parameter) {
        // do nothing
    }


    @Override
    protected void insertFill(MetaObject metaObject, TableInfo tableInfo) {
        GlobalConfigUtils.getMetaObjectHandler(mappedStatement.getConfiguration()).ifPresent(metaObjectHandler -> {
            if (metaObjectHandler.openInsertFill(mappedStatement)) {
                if (metaObjectHandler instanceof SmartMetaObjectFill smartMetaObjectFill) {
                    smartMetaObjectFill.insertFill(metaObject, this.mappedStatement);
                } else {
                    metaObjectHandler.insertFill(metaObject);
                }
            }
        });
    }

    @Override
    protected void updateFill(MetaObject metaObject, TableInfo tableInfo) {
        GlobalConfigUtils.getMetaObjectHandler(this.mappedStatement.getConfiguration()).ifPresent(metaObjectHandler -> {
            if (metaObjectHandler.openUpdateFill(mappedStatement)) {
                if (metaObjectHandler instanceof SmartMetaObjectFill smartMetaObjectFill) {
                    // 判断是否是逻辑删除
                    if (this.logicDeleteMethod) {
                        smartMetaObjectFill.logicDeleteFill(metaObject, this.mappedStatement);
                    } else {
                        smartMetaObjectFill.updateFill(metaObject, this.mappedStatement);
                    }
                } else {
                    metaObjectHandler.updateFill(metaObject);
                }
            }
        });
    }
}
