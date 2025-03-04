package com.smart.framework.crud.service;

import com.smart.framework.crud.model.UserDeptData;

/**
 * 获取人员信息
 * @author zhongming4762
 * 2022/12/16 21:11
 */
public interface UserProvider {

    /**
     * 获取当前登录人员ID
     * @return 人员ID
     */
    Long getCurrentUserId();

    /**
     * 获取当前登录人员username
     * @return username
     */
    String getCurrentUsername();


    /**
     * 获取当前登录人员姓名
     * @return 姓名
     */
    String getCurrentUserFullName();

    /**
     * 获取当前登录人员部门信息
     * @return 部门ID
     */
    UserDeptData getCurrentDept();
}
