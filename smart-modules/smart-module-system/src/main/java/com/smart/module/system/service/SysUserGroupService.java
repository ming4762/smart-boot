package com.smart.module.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.crud.service.BaseService;
import com.smart.module.system.model.SysUserGroupPO;
import com.smart.module.system.model.SysUserPO;
import com.smart.module.system.pojo.dto.UserGroupUserSaveDTO;
import com.smart.module.system.pojo.dto.UserUserGroupSaveDTO;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author jackson
 * 2020/1/24 3:04 下午
 */
public interface SysUserGroupService extends BaseService<SysUserGroupPO> {

    /**
     * 查询用户组ID包含的用户id集合
     * @param groupIds 用户组ID
     * @return 用户组ID包含的用户id集合
     */
    @NonNull
    Map<Long, List<Long>> listUserIdByIds(@NonNull Collection<Long> groupIds);

    /**
     * 查询用户组ID包含的用户集合
     * @param groupIds 用户组ID
     * @return 用户组ID包含的用户集合
     */
    @NonNull
    Map<Long, List<SysUserPO>> listUserByIds(@NonNull Collection<Long> groupIds);

    /**
     * 保存用户组的用户信息
     * @param parameter 查询参数
     * @return 用户组的用户信息
     */
    boolean saveUserGroupByGroupId(@NonNull UserGroupUserSaveDTO parameter);

    /**
     * 保存用户的用户组信息
     * @param parameter 参数
     * @return 结果
     */
    boolean saveUserGroupByUserId(@NonNull UserUserGroupSaveDTO parameter);

     /**
      * 查询用户组ID包含的未绑定用户集合
      * @param groupIds 用户组ID
      * @param queryWrapper 查询条件
      * @return 用户组ID包含的未绑定用户集合
      */
    @NonNull
    List<SysUserPO> listNoBindUserByIds(@NonNull Collection<Long> groupIds, @NonNull QueryWrapper<SysUserPO> queryWrapper);

    /**
     * 解绑用户组的用户
     * @param groupId 用户组ID
     * @param userIdList 用户ID列表
     * @return 是否解绑成功
     */
    boolean unBindUser(@org.jspecify.annotations.NonNull Long groupId, @org.jspecify.annotations.NonNull List<Long> userIdList);
}
