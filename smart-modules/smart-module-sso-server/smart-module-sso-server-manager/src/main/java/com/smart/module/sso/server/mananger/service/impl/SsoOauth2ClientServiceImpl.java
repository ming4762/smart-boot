package com.smart.module.sso.server.mananger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.smart.framework.commons.core.utils.BeanUtils;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.framework.crud.utils.CrudPageHelper;
import com.smart.module.api.system.dto.SysUserDTO;
import com.smart.module.sso.server.common.manager.mapper.SsoOauth2ClientMapper;
import com.smart.module.sso.server.common.manager.model.SsoOauth2ClientPO;
import com.smart.module.sso.server.common.manager.model.SsoOauth2ClientUserPO;
import com.smart.module.sso.server.mananger.pojo.parameter.SsoClientBindUserParameter;
import com.smart.module.sso.server.mananger.pojo.parameter.SsoClientUnBindUserParameter;
import com.smart.module.sso.server.mananger.pojo.parameter.SsoClientUserUseYnParameter;
import com.smart.module.sso.server.mananger.pojo.parameter.SsoListClientUserParameter;
import com.smart.module.sso.server.mananger.pojo.vo.SsoClientUserVO;
import com.smart.module.sso.server.mananger.service.SsoOauth2ClientService;
import com.smart.module.sso.server.mananger.service.SsoOauth2ClientUserService;
import com.smart.module.system.model.SysUserPO;
import com.smart.module.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * oauth2_client - oauth2客户端 Service实现类
 *
 * @author SmartCodeGenerator
 * 2025年11月14日 16:54:04
 */
@Service
@RequiredArgsConstructor
public class SsoOauth2ClientServiceImpl extends BaseServiceImpl<SsoOauth2ClientMapper, SsoOauth2ClientPO> implements SsoOauth2ClientService {

    private final SsoOauth2ClientUserService ssoOauth2ClientUserService;
    private final SysUserService sysUserService;

    /**
     * 根据clientId查询oauth2客户端
     *
     * @param clientCode 客户端编码
     * @return oauth2客户端
     */
    @Override
    public SsoOauth2ClientPO getByClientCode(String clientCode) {
        return this.lambdaQuery().eq(SsoOauth2ClientPO::getClientCode, clientCode)
                .eq(SsoOauth2ClientPO::getUseYn, Boolean.TRUE)
                .one();
    }

    /**
     * 根据id和useYn查询oauth2客户端
     *
     * @param id    客户端ID
     * @return oauth2客户端
     */
    @Override
    public SsoOauth2ClientPO getByIdInUse(Long id) {
        return this.lambdaQuery().eq(SsoOauth2ClientPO::getId, id)
                .eq(SsoOauth2ClientPO::getUseYn, Boolean.TRUE)
                .one();
    }

    /**
     * 根据clientId查询客户端用户
     *
     * @param parameter 查询参数
     * @param userParameter 用户查询参数
     * @return 客户端用户列表
     */
    @Override
    public List<SsoClientUserVO> listClientUser(@NonNull SsoListClientUserParameter parameter, @Nullable QueryWrapper<SysUserPO> userParameter) {
        // 查询客户端对应用户ID
        LambdaQueryChainWrapper<SsoOauth2ClientUserPO> queryChainWrapper = this.ssoOauth2ClientUserService.lambdaQuery()
                .eq(SsoOauth2ClientUserPO::getClientId, parameter.getClientId());
        if (parameter.getClientUserUseYn() != null) {
            queryChainWrapper.eq(SsoOauth2ClientUserPO::getUseYn, parameter.getClientUserUseYn());
        }
        if (parameter.getAccessStrategy() != null) {
            queryChainWrapper.eq(SsoOauth2ClientUserPO::getAccessStrategy, parameter.getAccessStrategy());
        }
        Map<Long, SsoOauth2ClientUserPO> clientUserMap = queryChainWrapper.list().stream()
                .collect(Collectors.toMap(SsoOauth2ClientUserPO::getUserId, item -> item));
        if (CollectionUtils.isEmpty(clientUserMap)) {
            return List.of();
        }
        // 查询用户信息
        List<SysUserPO> userList = this.sysUserService.list(
                CrudPageHelper.get(),
                Objects.requireNonNullElseGet(userParameter, () -> new QueryWrapper<SysUserPO>()).lambda()
                        .in(SysUserPO::getUserId, clientUserMap.keySet())
        );
        if (CollectionUtils.isEmpty(userList)) {
            return List.of();
        }
        // 转换为VO
        return userList.stream()
                .map(user -> {
                    SysUserDTO dto = BeanUtils.copyProperties(user, SysUserDTO.class);
                    return SsoClientUserVO.builder()
                            .user(dto)
                            .clientUser(clientUserMap.get(user.getUserId()))
                            .build();
                }).toList();
    }

    /**
     * 查询未绑定用户列表
     *
     * @param clientId     客户端ID
     * @param queryWrapper 用户查询条件
     * @return 未绑定用户列表
     */
    @Override
    public List<SysUserDTO> listUnBindUser(@NonNull Long clientId, @Nullable QueryWrapper<SysUserPO> queryWrapper) {
        LambdaQueryWrapper<SysUserPO> lambdaQueryWrapper = Objects.requireNonNullElseGet(queryWrapper, () -> new QueryWrapper<SysUserPO>()).lambda()
                .apply("""
                        NOT EXISTS (
                            SELECT 1
                            FROM sso_oauth2_client_user B
                            WHERE B.user_id = sys_user.user_id
                            and B.client_id = {0}
                        )
                        """, clientId);
        List<SysUserPO> userList = this.sysUserService.list(CrudPageHelper.get(), lambdaQueryWrapper);
        return userList.stream()
                .map(user -> BeanUtils.copyProperties(user, SysUserDTO.class))
                .toList();
    }

    /**
     * 绑定用户到客户端
     *
     * @param parameter 绑定用户参数
     * @return 是否绑定成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bindUser(SsoClientBindUserParameter parameter) {
        List<SsoOauth2ClientUserPO> clientUserList = parameter.getUserIdList().stream().map(userId -> {
            SsoOauth2ClientUserPO clientUser = new SsoOauth2ClientUserPO();
            clientUser.setClientId(parameter.getClientId());
            clientUser.setUserId(userId);
            clientUser.setAccessStrategy(parameter.getAccessStrategy());
            clientUser.setUseYn(parameter.getUseYn());
            return clientUser;
        }).toList();
        return this.ssoOauth2ClientUserService.saveOrUpdateBatch(clientUserList);
    }

    /**
     * 设置绑定用户启用状态
     *
     * @param parameter 设置启用状态参数
     * @return 是否设置成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setBindUserUseYn(SsoClientUserUseYnParameter parameter) {
        return this.ssoOauth2ClientUserService.update(
                new UpdateWrapper<SsoOauth2ClientUserPO>().lambda()
                        .set(SsoOauth2ClientUserPO::getUseYn, parameter.getUseYn())
                        .eq(SsoOauth2ClientUserPO::getClientId, parameter.getClientId())
                        .in(SsoOauth2ClientUserPO::getUserId, parameter.getUserIdList())
        );
    }

    /**
     * 解绑用户与oauth2客户端
     *
     * @param parameter 解绑用户参数
     * @return 是否解绑成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unBindUser(SsoClientUnBindUserParameter parameter) {
        return this.ssoOauth2ClientUserService.remove(
                new QueryWrapper<SsoOauth2ClientUserPO>().lambda()
                        .eq(SsoOauth2ClientUserPO::getClientId, parameter.getClientId())
                        .in(SsoOauth2ClientUserPO::getUserId, parameter.getUserIdList())
        );
    }
}