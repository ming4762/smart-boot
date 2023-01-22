package com.smart.system.mapper;

import com.smart.crud.mapper.CrudBaseMapper;
import com.smart.system.model.SysCategoryPO;
import org.apache.ibatis.annotations.Param;

/**
* sys_category - 分类字段 mapper层
* @author SmartCodeGenerator
* 2023-1-21 21:32:15
*/
public interface SysCategoryMapper extends CrudBaseMapper<SysCategoryPO> {

    boolean updateHasChild(@Param("id") Long id);
}