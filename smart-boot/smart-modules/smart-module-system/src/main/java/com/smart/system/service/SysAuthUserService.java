package com.smart.system.service;

import com.smart.crud.service.BaseService;
import com.smart.system.model.SysAuthUserPO;
import com.smart.system.pojo.vo.SysOnlineUserVO;

import java.util.List;
import java.util.Set;

/**
 * @author ShiZhongMing
 * 2021/12/31
 * @since 1.0
 */
public interface SysAuthUserService extends BaseService<SysAuthUserPO> {

    /**
     * 查询在线用户信息
     * @param tokens token列表
     * @return 在线用户信息
     */
    List<SysOnlineUserVO> listOnlineUser(Set<String> tokens);

}
