package com.smart.framework.crud.plus.fill;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.smart.framework.crud.plus.metadata.SmartTableInfo;
import com.smart.framework.crud.utils.CrudUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;

/**
 * @author shizhongming
 * 2024/4/20 19:49
 * @since 3.0.0
 */
public interface SmartMetaObjectFill extends MetaObjectHandler {

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     * @param metaObject 元对象
     * @param mappedStatement MappedStatement
     */
    default void insertFill(MetaObject metaObject, MappedStatement mappedStatement) {
        // do nothing
    }


    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     * @param metaObject 元对象
     * @param mappedStatement MappedStatement
     */
    default void updateFill(MetaObject metaObject, MappedStatement mappedStatement) {
        // do nothing
    }

    /**
     * 逻辑删除注入
     * @param metaObject 元对象
     * @param mappedStatement MappedStatement
     */
    default void logicDeleteFill(MetaObject metaObject, MappedStatement mappedStatement) {
        // do nothing
    }

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    default void insertFill(MetaObject metaObject) {
        // do nothing
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    default void updateFill(MetaObject metaObject) {
        // dong noting
    }

    /**
     * find the tableInfo cache by metaObject </p>
     * 获取 TableInfo 缓存
     *
     * @param metaObject meta object parameter
     * @return TableInfo
     * @since 3.3.0
     */
    @Override
    default SmartTableInfo findTableInfo(MetaObject metaObject) {
        return CrudUtils.getTableInfo(metaObject.getOriginalObject().getClass());
    }
}
