package com.smart.system.service;

import com.smart.crud.service.BaseService;
import com.smart.system.model.SysUserGroupUserPO;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author jackson
 * 2020/1/30 1:51 下午
 */
public interface SysUserGroupUserService extends BaseService<SysUserGroupUserPO> {

    /**
     * 查询用户对应的用户组信息
     * @param userIds 用户ID集合
     * @return 用户-用户组Map
     */
    Map<Long, List<Long>> listGroupIdByUserId(@NonNull Collection<Long> userIds);
}
