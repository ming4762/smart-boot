package com.smart.system.mapper;

import com.smart.crud.mapper.CrudBaseMapper;
import com.smart.system.model.SysDeptPO;
import org.apache.ibatis.annotations.Param;

/**
* sys_dept - 部门表 mapper层
* @author GCCodeGenerator
* 2022年10月13日 上午10:24:21
*/
public interface SysDeptMapper extends CrudBaseMapper<SysDeptPO> {

    /**
     * 更新has_child
     * @param id ID
     * @return 是否更新成功
     */
    boolean updateHasChild(@Param("id") Long id);
}
