package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.extensions.jwt.resolver.JwtResolver;
import com.smart.commons.core.i18n.I18nException;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.i18n.SystemI18nMessage;
import com.smart.system.mapper.SysUserAccountMapper;
import com.smart.system.mapper.SysUserMapper;
import com.smart.system.model.SysUserAccountPO;
import com.smart.system.model.SysUserPO;
import com.smart.system.pojo.vo.SysOnlineUserVO;
import com.smart.system.service.SysUserAccountService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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

    private final AuthProperties.UserStatus userStatusProperties;

    public SysUserAccountServiceImpl(JwtResolver jwtResolver, SysUserMapper sysUserMapper, AuthProperties authProperties) {
        this.jwtResolver = jwtResolver;
        this.sysUserMapper = sysUserMapper;
        this.userStatusProperties = authProperties.getStatus();
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
    public boolean createAccount(@NonNull List<Long> userIdList) {
        // 查询用户信息
        List<SysUserPO> userList = this.sysUserMapper.selectBatchIds(userIdList);
        if (CollectionUtils.isEmpty(userList)) {
            return false;
        }
        // 验证用户是否已经删除
        List<SysUserPO> deleteUser = userList.stream()
                .filter(item -> Boolean.TRUE.equals(item.getDeleteYn()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(deleteUser)) {
            throw new I18nException(SystemI18nMessage.SYSTEM_ACCOUNT_HAS_DELETE_ERROR, deleteUser.stream().map(SysUserPO::getUsername).collect(Collectors.joining(",")));
        }
        // 验证用户是否未启用
        List<SysUserPO> noUserList = userList.stream()
                .filter(item -> Boolean.FALSE.equals(item.getUseYn()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(noUserList)) {
            throw new I18nException(SystemI18nMessage.SYSTEM_ACCOUNT_HAS_NO_USE_ERROR, noUserList.stream().map(SysUserPO::getUsername).collect(Collectors.joining(",")));
        }
        Map<Long, SysUserPO> userMap = userList.stream()
                .collect(Collectors.toMap(SysUserPO :: getUserId, item -> item));
        // 判断账户是否已经存在
        List<SysUserAccountPO> existAccountList = this.list(
                new QueryWrapper<SysUserAccountPO>().lambda()
                .select(SysUserAccountPO :: getUserId)
                .in(SysUserAccountPO :: getUserId, userMap.keySet())
        );
        if (CollectionUtils.isNotEmpty(existAccountList)) {
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
        List<SysUserAccountPO> userAccountList = userList.stream()
                .map(item -> SysUserAccountPO.builder()
                        .userId(item.getUserId())
                        .lastLoginTime(currentTime)
                        .createTime(currentTime)
                        .maxConnections(userStatusProperties.getMaxConnections())
                        .maxDaysSinceLogin(userStatusProperties.getMaxDaysSinceLogin())
                        .passwordLifeDays(userStatusProperties.getPasswordLifeDays())
                        .build())
                .collect(Collectors.toList());
        return this.saveBatch(userAccountList);
    }
}
