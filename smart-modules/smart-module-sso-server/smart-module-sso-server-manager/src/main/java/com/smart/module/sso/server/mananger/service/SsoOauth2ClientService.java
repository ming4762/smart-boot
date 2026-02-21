package com.smart.module.sso.server.mananger.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.crud.service.BaseService;
import com.smart.module.api.system.dto.SysUserDTO;
import com.smart.module.sso.server.common.manager.model.SsoOauth2ClientPO;
import com.smart.module.sso.server.mananger.pojo.parameter.SsoClientBindUserParameter;
import com.smart.module.sso.server.mananger.pojo.parameter.SsoClientUserUseYnParameter;
import com.smart.module.sso.server.mananger.pojo.parameter.SsoListClientUserParameter;
import com.smart.module.sso.server.mananger.pojo.vo.SsoClientUserVO;
import com.smart.module.system.model.SysUserPO;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
* oauth2_client - oauth2客户端 Service
* @author SmartCodeGenerator
* 2025年11月14日 16:54:04
*/
public interface SsoOauth2ClientService extends BaseService<SsoOauth2ClientPO> {

    /**
     * 根据clientId查询oauth2客户端
     * @param clientId 客户端ID
     * @return oauth2客户端
     */
    SsoOauth2ClientPO getByClientId(String clientId);

    /**
     * 根据id和useYn查询oauth2客户端
     * @param id 客户端ID
     * @return oauth2客户端
     */
    SsoOauth2ClientPO getByIdInUse(Long id);

    /**
     * 根据clientId查询客户端用户
     * @param parameter 查询参数
     * @param userParameter 用户查询条件
     * @return 客户端用户列表
     */
    List<SsoClientUserVO> listClientUser(@NonNull SsoListClientUserParameter parameter, @Nullable QueryWrapper<SysUserPO> userParameter);

    /**
     * 查询未绑定用户列表
     * @param clientId 客户端ID
     * @param queryWrapper 用户查询条件
     * @return 未绑定用户列表
     */
    List<SysUserDTO> listUnBindUser(@NonNull Long clientId, @Nullable QueryWrapper<SysUserPO> queryWrapper);

    /**
     * 绑定用户到客户端
     * @param parameter 绑定用户参数
     * @return 是否绑定成功
     */
    boolean bindUser(SsoClientBindUserParameter parameter);

     /**
     * 设置绑定用户启用状态
     * @param parameter 设置启用状态参数
     * @return 是否设置成功
     */
    boolean setBindUserUseYn(SsoClientUserUseYnParameter parameter);
}