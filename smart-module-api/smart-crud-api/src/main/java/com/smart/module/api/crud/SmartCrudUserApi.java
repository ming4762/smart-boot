package com.smart.module.api.crud;

import com.smart.module.api.crud.module.UserDeptData;

/**
 * crud模块获取用户信息API
 * @author shizhongming
 * 2025/3/17 19:52
 * @since 5.0.0
 */
public interface SmartCrudUserApi {

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
