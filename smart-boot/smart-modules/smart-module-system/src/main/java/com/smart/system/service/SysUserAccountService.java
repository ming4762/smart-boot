package com.smart.system.service;

import com.smart.crud.service.BaseService;
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
     * 创建初始化的用户账号
     * @param userId 用户ID
     * @return 用户账户信息
     */
    SysUserAccountPO createInitUserAccount(@NonNull Long userId);

    /**
     * 更新密码
     * @param userId 用户ID
     * @param password 用户密码
     * @return 是否更改成功
     */
    boolean changePassword(@NonNull Long userId, @NonNull String password);

}
