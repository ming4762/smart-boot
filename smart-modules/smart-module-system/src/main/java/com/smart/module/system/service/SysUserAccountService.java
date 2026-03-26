package com.smart.module.system.service;

import com.smart.framework.commons.core.dto.auth.UserAccountStatusEnum;
import com.smart.framework.crud.service.BaseService;
import com.smart.module.system.model.SysUserAccountPO;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/12/31
 * @since 1.0
 */
public interface SysUserAccountService extends BaseService<SysUserAccountPO> {


    /**
     * 更新密码
     * @param userId 用户ID
     * @param password 用户密码
     * @return 是否更改成功
     */
    boolean changePassword(@NonNull Long userId, @NonNull String password);

    /**
     * 批量创建账号
     * @param tenantId 租户ID
     * @param userIdList 用户ID列表
     * @return 是否创建成功
     */
    boolean createAccount(@NonNull Long tenantId, @NonNull List<Long> userIdList);

    /**
     * 结果账户
     * @param userAccount 账户
     * @param lockStatus 解锁的锁定状态，null则所有锁定状态都解锁
     * @return 是否解锁成功
     */
    boolean unlock(@NonNull SysUserAccountPO userAccount, UserAccountStatusEnum lockStatus);


    /**
     * 解锁账户
     * @param userId 用户ID
     * @param lockStatus 解锁的锁定状态，null则所有锁定状态都解锁
     * @return 是否解锁成功
     */
    boolean unlock(@NonNull Long userId, UserAccountStatusEnum lockStatus);

    /**
     * 通过用户ID查询用户账户信息
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 用户账户信息
     */
    SysUserAccountPO getByUserId(Long userId, Long tenantId);
}
