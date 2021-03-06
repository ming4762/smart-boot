package com.smart.system.service;

import com.smart.crud.service.BaseService;
import com.smart.system.constants.UserAccountStatusEnum;
import com.smart.system.model.SysUserAccountPO;
import com.smart.system.pojo.vo.SysOnlineUserVO;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

/**
 * @author ShiZhongMing
 * 2021/12/31
 * @since 1.0
 */
public interface SysUserAccountService extends BaseService<SysUserAccountPO> {

    /**
     * 查询在线用户信息
     * @param tokens token列表
     * @return 在线用户信息
     */
    List<SysOnlineUserVO> listOnlineUser(Set<String> tokens);


    /**
     * 更新密码
     * @param userId 用户ID
     * @param password 用户密码
     * @return 是否更改成功
     */
    boolean changePassword(@NonNull Long userId, @NonNull String password);

    /**
     * 批量创建账号
     * @param userIdList 用户ID列表
     * @return 是否创建成功
     */
    boolean createAccount(@NonNull List<Long> userIdList);

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
}
