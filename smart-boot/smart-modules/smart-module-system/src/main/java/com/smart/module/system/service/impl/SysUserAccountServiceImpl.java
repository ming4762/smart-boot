package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.smart.framework.commons.core.dto.auth.UserAccountStatusEnum;
import com.smart.framework.commons.core.i18n.I18nException;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.api.system.constants.SysParameterCodeEnum;
import com.smart.module.system.i18n.SystemI18nMessage;
import com.smart.module.system.mapper.SysUserAccountMapper;
import com.smart.module.system.mapper.SysUserMapper;
import com.smart.module.system.model.SysUserAccountPO;
import com.smart.module.system.model.SysUserPO;
import com.smart.module.system.service.SysParameterService;
import com.smart.module.system.service.SysUserAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/12/31
 * @since 1.0.7
 */
@Service
public class SysUserAccountServiceImpl extends BaseServiceImpl<SysUserAccountMapper, SysUserAccountPO> implements SysUserAccountService {

    private final SysUserMapper sysUserMapper;
    private final SysParameterService sysParameterService;

    public SysUserAccountServiceImpl(SysUserMapper sysUserMapper, SysParameterService sysParameterService) {
        this.sysUserMapper = sysUserMapper;
        this.sysParameterService = sysParameterService;
    }

    /**
     * 更改密码
     * @param password 密码
     * @param userId 用户ID
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changePassword(@NonNull Long userId, @NonNull String password) {
        // 获取账户信息
        LambdaUpdateWrapper<SysUserAccountPO> updateWrapper = new UpdateWrapper<SysUserAccountPO>().lambda()
                .set(SysUserAccountPO :: getPasswordModifyTime, LocalDateTime.now())
                .set(SysUserAccountPO :: getInitialPasswordYn, Boolean.FALSE)
                .eq(SysUserAccountPO :: getUserId, userId);
        this.update(updateWrapper);
        // 更新密码
        return SqlHelper.retBool(this.sysUserMapper.update(null,
                new UpdateWrapper<SysUserPO>().lambda()
                        .set(SysUserPO :: getPassword, password)
                        .eq(SysUserPO :: getUserId, userId)
        ));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createAccount(@NonNull Long tenantId, @NonNull List<Long> userIdList) {
        // 查询用户信息
        List<SysUserPO> userList = this.sysUserMapper.selectByIds(userIdList);
        if (CollectionUtils.isEmpty(userList)) {
            return false;
        }
        // 验证用户是否已经删除
        List<SysUserPO> deleteUser = userList.stream()
                .filter(item -> Boolean.TRUE.equals(item.getDeleteYn()))
                .toList();
        if (!CollectionUtils.isEmpty(deleteUser)) {
            throw new I18nException(SystemI18nMessage.SYSTEM_ACCOUNT_HAS_DELETE_ERROR, deleteUser.stream().map(SysUserPO::getUsername).collect(Collectors.joining(",")));
        }
        // 验证用户是否未启用
        List<SysUserPO> noUserList = userList.stream()
                .filter(item -> Boolean.FALSE.equals(item.getUseYn()))
                .toList();
        if (!CollectionUtils.isEmpty(noUserList)) {
            throw new I18nException(SystemI18nMessage.SYSTEM_ACCOUNT_HAS_NO_USE_ERROR, noUserList.stream().map(SysUserPO::getUsername).collect(Collectors.joining(",")));
        }
        Map<Long, SysUserPO> userMap = userList.stream()
                .collect(Collectors.toMap(SysUserPO :: getUserId, item -> item));
        // 判断账户是否已经存在
        List<SysUserAccountPO> existAccountList = this.list(
                new QueryWrapper<SysUserAccountPO>().lambda()
                .select(SysUserAccountPO :: getUserId)
                        .eq(SysUserAccountPO::getTenantId, tenantId)
                        .in(SysUserAccountPO :: getUserId, userMap.keySet())
        );
        if (!CollectionUtils.isEmpty(existAccountList)) {
            // 账户已经存在抛出异常
            throw new I18nException(
                    SystemI18nMessage.SYSTEM_ACCOUNT_EXIST_ERROR,
                    existAccountList.stream()
                        .map(item -> Optional.ofNullable(userMap.get(item.getUserId())).map(SysUserPO::getUsername).orElse(null))
                        .filter(StringUtils::isNotBlank)
                        .collect(Collectors.joining(","))
            );
        }
        LocalDateTime currentTime = LocalDateTime.now();
        // 获取参数值
        Map<String, String> sysParameter = this.sysParameterService.getParameter(List.of(
                SysParameterCodeEnum.AUTH_MAX_CONNECTIONS.getCode(),
                SysParameterCodeEnum.AUTH_MAX_DAYS_SINCE_LOGIN.getCode(),
                SysParameterCodeEnum.AUTH_PASSWORD_LIFE_DAYS.getCode(),
                SysParameterCodeEnum.AUTH_PASSWORD_ERROR_UNLOCK_SECOND.getCode()
                ));

        List<SysUserAccountPO> userAccountList = userList.stream()
                .map(item -> SysUserAccountPO.builder()
                        .userId(item.getUserId())
                        .tenantId(tenantId)
                        .lastLoginTime(currentTime)
                        .passwordModifyTime(currentTime)
                        .maxConnections(Long.valueOf(sysParameter.get(SysParameterCodeEnum.AUTH_MAX_CONNECTIONS.getCode())))
                        .maxDaysSinceLogin(Long.valueOf(sysParameter.get(SysParameterCodeEnum.AUTH_MAX_DAYS_SINCE_LOGIN.getCode())))
                        .passwordLifeDays(Long.valueOf(sysParameter.get(SysParameterCodeEnum.AUTH_PASSWORD_LIFE_DAYS.getCode())))
                        .passwordErrorUnlockSecond(this.getLongValue(sysParameter.get(SysParameterCodeEnum.AUTH_PASSWORD_ERROR_UNLOCK_SECOND.getCode()), 0L))
                        .build())
                .toList();
        return this.saveBatch(userAccountList);
    }

    private Long getLongValue(String value, Long defaultValue) {
        if (org.springframework.util.StringUtils.hasText(value)) {
            return Long.valueOf(value);
        }
        return defaultValue;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlock(@NonNull SysUserAccountPO userAccount, UserAccountStatusEnum lockStatus) {
        if (userAccount.getAccountStatus().equals(UserAccountStatusEnum.NORMAL)) {
            return false;
        }
        if (lockStatus != null && !lockStatus.equals(userAccount.getAccountStatus())) {
            return false;
        }
        LambdaUpdateWrapper<SysUserAccountPO> updateWrapper = new UpdateWrapper<SysUserAccountPO>().lambda()
                .eq(SysUserAccountPO :: getUserId, userAccount.getUserId())
                .set(SysUserAccountPO::getLockTime, null)
                .set(SysUserAccountPO::getAccountStatus, UserAccountStatusEnum.NORMAL.getValue());
        switch (userAccount.getAccountStatus()) {
            case LONG_TIME_LOCKED ->
                    // 长时间未登录锁定解锁
                    updateWrapper.set(SysUserAccountPO::getLastLoginTime, LocalDateTime.now());
            case LONG_TIME_PASSWORD_MODIFY_LOCKED ->
                    // 长时间密码未修改锁定解锁
                    updateWrapper.set(SysUserAccountPO::getPasswordModifyTime, LocalDateTime.now())
                            .set(SysUserAccountPO::getLastLoginTime, LocalDateTime.now());
            default -> {
                // do noting
            }
        }
        return this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlock(@NonNull Long userId, UserAccountStatusEnum lockStatus) {
        SysUserAccountPO userAccount = this.getById(userId);
        if (userAccount == null) {
            return false;
        }
        return this.unlock(userAccount, lockStatus);
    }

    /**
     * 通过用户ID查询用户账户信息
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 用户账户信息
     */
    @Override
    public SysUserAccountPO getByUserId(Long userId, Long tenantId) {
        return this.lambdaQuery()
                .eq(SysUserAccountPO::getUserId, userId)
                .eq(SysUserAccountPO::getTenantId, tenantId)
                .one();
    }
}
