package com.smart.system.service.impl;

import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.extensions.jwt.resolver.JwtResolver;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.mapper.SysAuthUserMapper;
import com.smart.system.mapper.SysUserMapper;
import com.smart.system.model.SysAuthUserPO;
import com.smart.system.model.SysUserPO;
import com.smart.system.pojo.vo.SysOnlineUserVO;
import com.smart.system.service.SysAuthUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
public class SysAuthUserServiceImpl extends BaseServiceImpl<SysAuthUserMapper, SysAuthUserPO> implements SysAuthUserService {

    private final JwtResolver jwtResolver;

    private final SysUserMapper sysUserMapper;

    public SysAuthUserServiceImpl(JwtResolver jwtResolver, SysUserMapper sysUserMapper) {
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
}
