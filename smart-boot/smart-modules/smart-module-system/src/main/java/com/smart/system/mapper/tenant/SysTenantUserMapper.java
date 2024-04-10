package com.smart.system.mapper.tenant;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.smart.crud.mapper.CrudBaseMapper;
import com.smart.system.model.tenant.SysTenantUserPO;
import com.smart.system.pojo.dbo.tenant.SysTenantListByUserDO;
import com.smart.system.pojo.dbo.tenant.SysTenantUserListDO;
import com.smart.system.pojo.dto.tenant.SysListTenantFunctionDTO;
import com.smart.system.pojo.dto.tenant.SysListTenantRoleFunctionDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* sys_tenant_user - 租户用户关联关系表 mapper层
* @author SmartCodeGenerator
* 2024年4月6日 下午8:25:53
*/
public interface SysTenantUserMapper extends CrudBaseMapper<SysTenantUserPO> {

    /**
     * 查询租户对应用户
     * @param parameter 参数
     * @return 用户列表
     */
    List<SysTenantUserListDO> listTenantUser(@Param(Constants.WRAPPER)Wrapper<SysTenantUserPO> parameter);

    /**
     * 根据用户ID查询满足条件的一个租户
     * @param userId 用户ID
     * @return 租户信息
     */
    SysTenantListByUserDO selectOneTenantByUser(@Param("userId") Long userId);

    /**
     * 根据租户角色查询功能ID
     * @param parameter 参数
     * @return functionIdList
     */
    List<Long> listTenantRoleFunctionIds(SysListTenantRoleFunctionDTO parameter);

    /**
     * 查询租户所有有效订阅菜单ID
     * @param parameter 参数
     * @return 菜单ID
     */
    List<Long> listTenantFunctionIds(SysListTenantFunctionDTO parameter);
}