package com.smart.framework.crud.plus.fill;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;

import java.util.List;

/**
 * @author shizhongming
 * 2024/4/19 21:05
 * @since 3.0.0
 */
@RequiredArgsConstructor
public class DelegateMetaObjectFill implements SmartMetaObjectFill {

    private final List<SmartMetaObjectFill> metaObjectHandlerList;


    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject      元对象
     * @param mappedStatement MappedStatement
     */
    @Override
    public void insertFill(MetaObject metaObject, MappedStatement mappedStatement) {
        this.metaObjectHandlerList.forEach(handler -> handler.insertFill(metaObject, mappedStatement));
    }

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject      元对象
     * @param mappedStatement MappedStatement
     */
    @Override
    public void updateFill(MetaObject metaObject, MappedStatement mappedStatement) {
        this.metaObjectHandlerList.forEach(handler -> handler.updateFill(metaObject, mappedStatement));
    }

    /**
     * 逻辑删除注入
     *
     * @param metaObject      元对象
     * @param mappedStatement MappedStatement
     */
    @Override
    public void logicDeleteFill(MetaObject metaObject, MappedStatement mappedStatement) {
        this.metaObjectHandlerList.forEach(handler -> handler.logicDeleteFill(metaObject, mappedStatement));
    }
}
