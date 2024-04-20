package com.smart.crud.plus.handlers;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.smart.crud.plus.metadata.SmartTableInfo;
import com.smart.crud.utils.CrudUtils;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;

import java.util.List;

/**
 * @author shizhongming
 * 2024/4/19 21:05
 * @since 3.0.0
 */
@RequiredArgsConstructor
public class DelegateMetaObjectHandler implements MetaObjectHandler {

    private final List<MetaObjectHandler> metaObjectHandlerList;

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.metaObjectHandlerList.forEach(item -> item.insertFill(metaObject));
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.metaObjectHandlerList.forEach(item -> item.updateFill(metaObject));
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
    public SmartTableInfo findTableInfo(MetaObject metaObject) {
        return CrudUtils.getTableInfo(metaObject.getOriginalObject().getClass());
    }
}
