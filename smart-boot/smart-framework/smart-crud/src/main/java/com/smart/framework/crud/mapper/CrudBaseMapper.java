package com.smart.framework.crud.mapper;

import com.smart.framework.crud.model.BaseModel;

/**
 * 基础服务层
 * @author jackson
 * @param <T>
 */
public interface CrudBaseMapper<T extends BaseModel> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

}
