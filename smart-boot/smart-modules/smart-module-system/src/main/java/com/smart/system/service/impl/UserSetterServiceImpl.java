package com.smart.system.service.impl;

import com.google.common.collect.Sets;
import com.smart.crud.model.CreateUpdateUserSetter;
import com.smart.crud.model.CreateUserSetter;
import com.smart.crud.model.UpdateUserSetter;
import com.smart.crud.model.UserSetter;
import com.smart.crud.service.UserSetterService;
import com.smart.system.mapper.SysUserMapper;
import com.smart.system.model.SysUserPO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2022/1/11
 * @since 2.0.0
 */
@Component
public class UserSetterServiceImpl implements UserSetterService {

    private final SysUserMapper sysUserMapper;

    public UserSetterServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    /**
     * 设置创建人
     * @param modelList 数据
     */
    @Override
    public void setCreateUser(List<? extends CreateUserSetter> modelList) {
        this.setUser(
                modelList,
                item -> Sets.newHashSet(((CreateUserSetter)item).getCreateUserId()),
                (userSetter, userMap) -> ((CreateUserSetter)userSetter).setCreateUser(userMap.get(((CreateUserSetter) userSetter).getCreateUserId()))
        );
    }

    /**
     * 设置更新人
     * @param modelList 数据
     */
    @Override
    public void setUpdateUser(List<? extends UpdateUserSetter> modelList) {
        this.setUser(
                modelList,
                item -> Sets.newHashSet(((UpdateUserSetter)item).getUpdateUserId()),
                (userSetter, userMap) -> ((UpdateUserSetter)userSetter).setUpdateUser(userMap.get(((UpdateUserSetter) userSetter).getUpdateUserId()))
        );
    }

    /**
     * 设置创建/更新人
     * @param modelList 数据
     */
    @Override
    public void setCreateUpdateUser(List<? extends CreateUpdateUserSetter> modelList) {
        this.setUser(
                modelList,
                item -> Sets.newHashSet(((CreateUpdateUserSetter)item).getCreateUserId(), ((CreateUpdateUserSetter) item).getUpdateUserId()),
                (userSetter, userMap) -> {
                    ((CreateUpdateUserSetter)userSetter).setCreateUser(userMap.get(((CreateUpdateUserSetter) userSetter).getCreateUserId()));
                    ((CreateUpdateUserSetter)userSetter).setUpdateUser(userMap.get(((CreateUpdateUserSetter) userSetter).getUpdateUserId()));
                }
        );
    }

    /**
     * 设置用户
     * @param modelList 需要设置用户
     * @param userIdHandler 获取用户ID函数
     * @param setUserHandler 设置用户ID函数
     */
    protected void setUser(List<? extends UserSetter> modelList, Function<UserSetter, Set<Long>> userIdHandler, BiConsumer<UserSetter, Map<Long, SysUserPO>> setUserHandler) {
        if (CollectionUtils.isEmpty(modelList)) {
            return;
        }
        // 获取用户ID
        Set<Long> userIds = Sets.newHashSet();
        modelList.forEach(item -> userIds.addAll(userIdHandler.apply(item)));

        if (userIds.isEmpty()) {
            return;
        }
        // 查询用户信息
        List<SysUserPO> sysUserList = this.sysUserMapper.selectBatchIds(userIds.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
        if (sysUserList.isEmpty()) {
            return;
        }
        Map<Long, SysUserPO> userMap = sysUserList.stream()
                .collect(Collectors.toMap(SysUserPO :: getUserId, item -> item));
        modelList.forEach(item -> setUserHandler.accept(item, userMap));
    }
}
