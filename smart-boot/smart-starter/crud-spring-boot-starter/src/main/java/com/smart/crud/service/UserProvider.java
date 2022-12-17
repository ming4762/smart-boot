package com.smart.crud.service;

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
}
