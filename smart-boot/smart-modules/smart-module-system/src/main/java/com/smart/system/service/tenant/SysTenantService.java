package com.smart.system.service.tenant;

import com.smart.crud.query.IdParameter;
import com.smart.crud.service.BaseService;
import com.smart.system.model.SysUserPO;
import com.smart.system.model.tenant.SysTenantPO;
import com.smart.system.model.tenant.SysTenantPackagePO;
import com.smart.system.pojo.dbo.tenant.SysTenantUserListDO;
import com.smart.system.pojo.dto.tenant.SysTenantBindUserDTO;
import com.smart.system.pojo.dto.tenant.SysTenantListNoBindUserDTO;
import com.smart.system.pojo.dto.tenant.SysTenantRemoveBindUserDTO;
import com.smart.system.pojo.dto.tenant.SysTenantUserListDTO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
* sys_tenant - 租户表 Service
* @author SmartCodeGenerator
* 2024年3月29日 上午10:42:40
*/
public interface SysTenantService extends BaseService<SysTenantPO> {
    /**
     * 查询租户对应用户
     * @param parameter 参数
     * @return 用户列表
     */
    List<SysTenantUserListDO> listTenantUser(SysTenantUserListDTO parameter);

    /**
     * 查询未绑定租户的用户
     * @param parameter 参数
     * @return 用户列表
     */
    List<SysUserPO> listNoBindUser(SysTenantListNoBindUserDTO parameter);

    /**
     * 绑定用户
     * @param parameter 参数
     * @return 是否绑定成功
     */
    boolean bindTenantUser(SysTenantBindUserDTO parameter);

    /**
     * 解绑用户
     * @param parameter 参数
     * @return 是否解绑成功
     */
    boolean removeBindUser(SysTenantRemoveBindUserDTO parameter);

    /**
     * 根据租户ID查询没有绑定的套餐
     * @param parameter 参数
     * @return 套餐包列表
     */
    List<SysTenantPackagePO> listNoBindPackageByTenantId(IdParameter parameter);

    /**
     * 根据用户查询租户
     * @param userId 用户ID
     * @return 租户列表
     */
    @NonNull
    List<SysTenantPO> listTenantByUserId(@NonNull Long userId);
}