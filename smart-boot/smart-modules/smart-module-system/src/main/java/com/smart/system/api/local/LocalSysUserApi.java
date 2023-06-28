package com.smart.system.api.local;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.constants.UserAccountStatusEnum;
import com.smart.module.api.system.dto.AccountLoginFailTimeUpdateDTO;
import com.smart.module.api.system.dto.SysUserDTO;
import com.smart.module.api.system.dto.UserAccountLockDTO;
import com.smart.system.model.SysUserAccountPO;
import com.smart.system.model.SysUserPO;
import com.smart.system.service.SysUserAccountService;
import com.smart.system.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author zhongming4762
 * 2023/3/11
 */
@Component
@Primary
public class LocalSysUserApi implements SysUserApi {

    private final SysUserService sysUserService;

    private final SysUserAccountService sysUserAccountService;

    public LocalSysUserApi(SysUserService sysUserService, SysUserAccountService sysUserAccountService) {
        this.sysUserService = sysUserService;
        this.sysUserAccountService = sysUserAccountService;
    }

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
                .set(SysUserAccountPO::getLockTime, LocalDateTime.now())
                .set(SysUserAccountPO::getAccountStatus, parameter.getAccountStatus().getValue())
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
        SysUserAccountPO userAccount = this.sysUserAccountService.getById(userId);
        if (userAccount == null) {
            return false;
        }
        LambdaUpdateWrapper<SysUserAccountPO> updateWrapper = new UpdateWrapper<SysUserAccountPO>().lambda()
                .eq(SysUserAccountPO::getUserId, userId);
        // 重置登录失败次数
        if (parameter.getLoginFailTime() == 0L) {
            updateWrapper.set(SysUserAccountPO::getLastLoginTime, LocalDateTime.now());
            if (userAccount.getLoginFailTime() > 0L) {
                updateWrapper.set(SysUserAccountPO::getLoginFailTime, 0L);
            }
            return this.sysUserAccountService.update(updateWrapper);
        } else {
            Long time = userAccount.getLoginFailTime() + 1;
            updateWrapper.set(SysUserAccountPO::getLoginFailTime, time);
            // 多次登录失败，账户锁定
            if (userAccount.getLoginFailTimeLimit() > 0 && time >= userAccount.getLoginFailTimeLimit()) {
                // 锁定用户
                updateWrapper.set(SysUserAccountPO :: getAccountStatus, UserAccountStatusEnum.LOGIN_FAIL_LOCKED.getValue())
                        // 设置锁定时间
                        .set(SysUserAccountPO :: getLockTime, LocalDateTime.now());
            }
            return this.sysUserAccountService.update(updateWrapper);
        }
    }
}
