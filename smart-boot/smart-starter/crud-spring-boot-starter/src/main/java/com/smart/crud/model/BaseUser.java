package com.smart.crud.model;

import java.io.Serializable;

/**
 * @author ShiZhongMing
 * 2021/6/25 15:34
 * @since 1.0
 */
public interface BaseUser extends Serializable {

    /**
     * 获取用户ID
     * @return 用户ID
     */
    Long getUserId();

    /**
     * 获取用户名
     * @return 用户名
     */
    String getUsername();

    /**
     * 获取用户姓名
     * @return 用户姓名
     */
    String getFullName();
}
