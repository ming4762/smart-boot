package com.smart.crud.mapper;

import com.smart.crud.constants.SmartCrudConstants;
import com.smart.crud.model.BaseModel;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;

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
    @Override
    int deleteById(@Param(SmartCrudConstants.DELETE_ID) Serializable id);
}
