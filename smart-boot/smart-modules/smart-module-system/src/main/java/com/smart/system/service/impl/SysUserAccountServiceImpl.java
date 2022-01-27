package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.extensions.jwt.resolver.JwtResolver;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.constants.UserAccountStatusEnum;
import com.smart.system.mapper.SysUserAccountMapper;
import com.smart.system.mapper.SysUserMapper;
import com.smart.system.model.SysUserAccountPO;
import com.smart.system.model.SysUserPO;
import com.smart.system.pojo.vo.SysOnlineUserVO;
import com.smart.system.service.SysUserAccountService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/12/31
 * @since 1.0.7
 */
@Service
public class SysUserAccountServiceImpl extends BaseServiceImpl<SysUserAccountMapper, SysUserAccountPO> implements SysUserAccountService {

    private final JwtResolver jwtResolver;

    private final SysUserMapper sysUserMapper;

    public SysUserAccountServiceImpl(JwtResolver jwtResolver, SysUserMapper sysUserMapper) {
        this.jwtResolver = jwtResolver;
        this.sysUserMapper = sysUserMapper;
    }

    /**
     * 查询在线用户信息
     * @param tokens token列表
     * @return 在线用户信息
     */
    @Override
    public List<SysOnlineUserVO> listOnlineUser(Set<String> tokens) {
        if (CollectionUtils.isEmpty(tokens)) {
            return new ArrayList<>(0);
        }
        // 解析所有jwt
        Map<Long, List<RestUserDetails>> restUserDetailsMap = tokens.stream().map(this.jwtResolver::resolver)
                .collect(Collectors.groupingBy(RestUserDetails::getUserId));
        // 查询用户信息
        List<SysUserPO> userList = this.sysUserMapper.selectBatchIds(restUserDetailsMap.keySet());
        return userList.stream().map(user -> {
            SysOnlineUserVO onlineUser = new SysOnlineUserVO();
            BeanUtils.copyProperties(user, onlineUser);
            List<RestUserDetails> userDetailsList = restUserDetailsMap.get(user.getUserId());
            if (CollectionUtils.isNotEmpty(userDetailsList)) {
                onlineUser.setUserLoginDataList(
                        userDetailsList.stream().map(userDetail -> {
                            SysOnlineUserVO.UserLoginData loginData = new SysOnlineUserVO.UserLoginData();
                            BeanUtils.copyProperties(userDetail, loginData);
                            return loginData;
                        }).collect(Collectors.toList())
                );
            }
            return onlineUser;
        }).collect(Collectors.toList());
    }

    @Override
    public SysUserAccountPO createInitUserAccount(@NonNull Long userId) {
        SysUserAccountPO userAccount = new SysUserAccountPO();
        userAccount.setUserId(userId);
        userAccount.setLoginFailTime(0);
        userAccount.setAccountStatus(UserAccountStatusEnum.NORMAL);
        userAccount.setInitialPasswordYn(true);
        userAccount.setCreateTime(LocalDateTime.now());
        return userAccount;
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
        SysUserAccountPO userAccount = this.getById(userId);
        if (userAccount == null) {
            userAccount = this.createInitUserAccount(userId);
            userAccount.setInitialPasswordYn(false);
            this.save(userAccount);
        }
        if (Boolean.TRUE.equals(userAccount.getInitialPasswordYn())) {
            this.update(
                    new UpdateWrapper<SysUserAccountPO>().lambda()
                    .set(SysUserAccountPO :: getInitialPasswordYn, Boolean.FALSE)
                    .eq(SysUserAccountPO :: getUserId, userId)
            );
        }
        // 更新密码
        return SqlHelper.retBool(this.sysUserMapper.update(null,
                new UpdateWrapper<SysUserPO>().lambda()
                        .set(SysUserPO :: getPassword, password)
                        .eq(SysUserPO :: getUserId, userId)
        ));
    }
}
