package com.smart.module.system.api.local;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.dto.auth.UserAccountStatusEnum;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.dto.*;
import com.smart.module.api.system.parameter.RemoteSysUserListParameter;
import com.smart.module.api.system.parameter.SysUserDeptParameter;
import com.smart.module.api.system.parameter.SysUserThirdAccountParameter;
import com.smart.module.system.model.SysUserAccountPO;
import com.smart.module.system.model.SysUserPO;
import com.smart.module.system.model.SysUserThirdAccountPO;
import com.smart.module.system.service.SysDeptService;
import com.smart.module.system.service.SysUserAccountService;
import com.smart.module.system.service.SysUserService;
import com.smart.module.system.service.SysUserThirdAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author zhongming4762
 * 2023/3/11
 */
@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class LocalSysUserApi implements SysUserApi {

    private final SysUserService sysUserService;
    private final SysUserAccountService sysUserAccountService;
    private final SysDeptService sysDeptService;
    private final SysUserThirdAccountService sysUserThirdAccountService;


    /**
     * 通过用户名查询用户
     *
     * @param usernameList 用户名列表
     * @return List
     */
    @Override
    public List<SysUserDTO> listUserByUsername(List<String> usernameList) {
        if (CollectionUtils.isEmpty(usernameList)) {
            return Collections.emptyList();
        }
        return this.sysUserService.list(
                new QueryWrapper<SysUserPO>().lambda()
                        .in(SysUserPO::getUsername, usernameList)
        ).stream().map(item -> {
            SysUserDTO dto = new SysUserDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).toList();
    }

    /**
     * 通过ID查询用户
     *
     * @param userIdList 用户ID列表
     * @return List
     */
    @Override
    public List<SysUserDTO> listUserById(List<Long> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return Collections.emptyList();
        }
        return this.sysUserService.listByIds(userIdList).stream()
                .map(item -> {
                    SysUserDTO dto = new SysUserDTO();
                    BeanUtils.copyProperties(item, dto);
                    return dto;
                }).toList();
    }

    /**
     * 锁定账户
     *
     * @param parameter 参数
     * @return 账户锁定参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean lockAccount(UserAccountLockDTO parameter) {
        SysUserPO user = this.sysUserService.getByUsername(parameter.getUsername());
        if (user == null) {
            return false;
        }
        Long userId = user.getUserId();
        LambdaUpdateWrapper<SysUserAccountPO> updateWrapper = new UpdateWrapper<SysUserAccountPO>().lambda()
                .set(SysUserAccountPO::getLockTime, ZonedDateTime.now())
                .set(SysUserAccountPO::getAccountStatus, parameter.getAccountStatus().getValue())
                .eq(SysUserAccountPO::getTenantId, parameter.getTenantId())
                .eq(SysUserAccountPO::getUserId, userId);
        return this.sysUserAccountService.update(updateWrapper);
    }

    /**
     * 重置登录失败次数
     *
     * @param parameter 参数
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLoginFailTime(AccountLoginFailTimeUpdateDTO parameter) {
        SysUserPO user = this.sysUserService.getByUsername(parameter.getUsername());
        if (user == null) {
            user = this.sysUserService.getByMobile(parameter.getUsername());
        }
        if (user == null) {
            return false;
        }
        Long userId = user.getUserId();
        SysUserAccountPO userAccount = this.sysUserAccountService.getByUserId(userId, parameter.getTenantId());
        if (userAccount == null) {
            return false;
        }
        LambdaUpdateWrapper<SysUserAccountPO> updateWrapper = new UpdateWrapper<SysUserAccountPO>().lambda()
                .eq(SysUserAccountPO::getUserId, userId)
                .eq(SysUserAccountPO::getTenantId, parameter.getTenantId());
        // 重置登录失败次数
        if (parameter.getLoginFailTime() == 0L) {
            updateWrapper.set(SysUserAccountPO::getLastLoginTime, ZonedDateTime.now());
            if (userAccount.getLoginFailTime() > 0L) {
                updateWrapper.set(SysUserAccountPO::getLoginFailTime, 0L);
            }
        } else {
            Long time = userAccount.getLoginFailTime() + 1;
            updateWrapper.set(SysUserAccountPO::getLoginFailTime, time);
            // 多次登录失败，账户锁定
            if (userAccount.getLoginFailTimeLimit() > 0 && time >= userAccount.getLoginFailTimeLimit()) {
                // 锁定用户
                updateWrapper.set(SysUserAccountPO :: getAccountStatus, UserAccountStatusEnum.LOGIN_FAIL_LOCKED.getValue())
                        // 设置锁定时间
                        .set(SysUserAccountPO :: getLockTime, ZonedDateTime.now());
            }
        }
        return this.sysUserAccountService.update(updateWrapper);
    }

    /**
     * 查询用户列表
     *
     * @param parameter 参数
     * @return 用户列表
     */
    @Override
    public List<SysUserDTO> listUser(RemoteSysUserListParameter parameter) {
        LambdaQueryWrapper<SysUserPO> queryWrapper = new QueryWrapper<SysUserPO>().lambda();
        if (parameter.getUseYn() != null) {
            queryWrapper.eq(SysUserPO::getUseYn, parameter.getUseYn());
        }
        if (StringUtils.hasText(parameter.getFullName())) {
            queryWrapper.like(SysUserPO::getFullName, parameter.getFullName());
        }
        if (StringUtils.hasText(parameter.getUsername())) {
            queryWrapper.like(SysUserPO::getUsername, parameter.getUsername());
        }
        if (!CollectionUtils.isEmpty(parameter.getUserIdList())) {
            queryWrapper.in(SysUserPO::getUserId, parameter.getUserIdList());
        }
        return this.sysUserService.list(queryWrapper).stream()
                .map(item -> {
                    SysUserDTO dto = new SysUserDTO();
                    BeanUtils.copyProperties(item, dto);
                    return dto;
                }).toList();
    }

    /**
     * 查询用户部门列表
     *
     * @param parameter 参数
     * @return 用户部门列表
     */
    @Override
    public List<SysDeptDTO> listUserDept(SysUserDeptParameter parameter) {
        Long userId = parameter == null ? AuthUtils.getCurrentUserId() : Objects.requireNonNullElseGet(parameter.getUserId(), AuthUtils::getCurrentUserId);
        return this.sysDeptService.listUserDept(userId).stream()
                .map(item -> {
                    SysDeptDTO dto = new SysDeptDTO();
                    BeanUtils.copyProperties(item, dto);
                    return dto;
                }).toList();
    }

    /**
     * 查询用户部门及子部门列表
     *
     * @param parameter 参数
     * @return 用户部门及子部门列表
     */
    @Override
    public List<SysDeptDTO> listUserDeptWithChildren(SysUserDeptParameter parameter) {
        List<SysDeptDTO> deptList = this.listUserDept(parameter);
        if (CollectionUtils.isEmpty(deptList)) {
            return deptList;
        }
        List<Long> deptIdList = deptList.stream().map(SysDeptDTO::getDeptId).toList();
        Set<Long> childrenIds = this.sysDeptService.queryAllChildIds(new HashSet<>(deptIdList));
        if (CollectionUtils.isEmpty(childrenIds)) {
            return deptList;
        }
        childrenIds.addAll(deptIdList);
        return this.sysDeptService.listByIds(childrenIds).stream()
               .map(item -> {
                    SysDeptDTO dto = new SysDeptDTO();
                    BeanUtils.copyProperties(item, dto);
                    return dto;
                }).toList();
    }

    /**
     * 查询用户第三方账号列表
     *
     * @param parameter 参数
     * @return 用户第三方账号列表
     */
    @Override
    public List<SysUserThirdAccountDTO> listUserThirdAccount(SysUserThirdAccountParameter parameter) {
        LambdaQueryChainWrapper<SysUserThirdAccountPO> lambdaQueryChainWrapper = this.sysUserThirdAccountService.lambdaQuery()
                .in(SysUserThirdAccountPO::getUserId, parameter.getUserIdList());
        if (parameter.getPlatformType() != null) {
            lambdaQueryChainWrapper.eq(SysUserThirdAccountPO::getPlatformType, parameter.getPlatformType());
        }
        if (parameter.getPlatformSubType() != null) {
            lambdaQueryChainWrapper.eq(SysUserThirdAccountPO::getPlatformSubType, parameter.getPlatformSubType());
        }
        if (StringUtils.hasText(parameter.getAppid())) {
            lambdaQueryChainWrapper.eq(SysUserThirdAccountPO::getAppid, parameter.getAppid());
        }
        return lambdaQueryChainWrapper.list().stream()
                .map(item -> com.smart.framework.commons.core.utils.BeanUtils.copyProperties(item, SysUserThirdAccountDTO.class))
                .toList();
    }
}
