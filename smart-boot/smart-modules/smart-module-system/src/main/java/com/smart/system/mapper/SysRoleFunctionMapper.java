package com.smart.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.smart.crud.mapper.CrudBaseMapper;
import com.smart.system.model.SysRoleFunctionPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shizhongming
 * 2020/9/22 8:46 下午
 */
public interface SysRoleFunctionMapper extends CrudBaseMapper<SysRoleFunctionPO> {

    /**
     * 查询角色功能
     *
     * @param queryWrapper 查询参数
     * @return List<SysRoleFunctionPO>
     */
    List<SysRoleFunctionPO> listRoleFunction(@Param(Constants.WRAPPER) Wrapper<SysRoleFunctionPO> queryWrapper);
}
