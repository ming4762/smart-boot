package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.mapper.SysUserGroupUserMapper;
import com.smart.system.model.SysUserGroupUserPO;
import com.smart.system.service.SysUserGroupUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jackson
 * 2020/1/30 1:51 下午
 */
@Service
public class SysUserGroupUserServiceImpl extends BaseServiceImpl<SysUserGroupUserMapper, SysUserGroupUserPO> implements SysUserGroupUserService {

    /**
     * 查询用户对应的用户组信息
     * @param userIds 用户ID集合
     * @return 用户-用户组Map
     */
    @Override
    public Map<Long, List<Long>> listGroupIdByUserId(@NonNull Collection<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Maps.newHashMap();
        }
        return this.list(
                new QueryWrapper<SysUserGroupUserPO>().lambda()
                        .select(SysUserGroupUserPO :: getUserGroupId, SysUserGroupUserPO :: getUserId)
                        .in(SysUserGroupUserPO :: getUserId, userIds)
                        .eq(SysUserGroupUserPO :: getUseYn, Boolean.TRUE)
        ).stream().collect(Collectors.groupingBy(SysUserGroupUserPO :: getUserId, Collectors.mapping(SysUserGroupUserPO :: getUserGroupId, Collectors.toList())));
    }
}
