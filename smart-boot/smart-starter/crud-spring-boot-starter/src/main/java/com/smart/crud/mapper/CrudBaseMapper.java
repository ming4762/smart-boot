package com.smart.crud.mapper;

import com.smart.crud.model.BaseModel;

/**
 * 基础服务层
 * @author jackson
 * @param <T>
 */
public interface CrudBaseMapper<T extends BaseModel> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
//    @Override
//    int deleteById(@Param(SmartCrudConstants.DELETE_ID) Serializable id);
}
