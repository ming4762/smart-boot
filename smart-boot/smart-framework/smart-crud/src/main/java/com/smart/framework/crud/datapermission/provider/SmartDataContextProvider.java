package com.smart.framework.crud.datapermission.provider;

import com.smart.framework.crud.datapermission.model.SmartDataContextUserModel;

import java.util.List;

/**
 * 数据权限上下文provider
 * @author shizhongming
 * 2025/3/5 19:59
 * @since 5.0.0
 */
public interface SmartDataContextProvider {


    /**
     * 获取用户信息
     * @return 用户信息
     */
    SmartDataContextUserModel getUserContext();

    /**
     * 获取用户部门列表
     * @return 用户部门列表
     */
    List<Long> getUserDeptList();

    /**
     * 获取用户部门及子部门列表
     * @return 用户部门及子部门列表
     */
    List<Long> getUserDeptChildren();
}
