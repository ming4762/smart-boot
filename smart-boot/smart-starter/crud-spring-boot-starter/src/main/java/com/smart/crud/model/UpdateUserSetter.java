package com.smart.crud.model;

/**
 * @author ShiZhongMing
 * 2021/6/25 15:50
 * @since 1.0
 */
public interface UpdateUserSetter extends UserSetter {

    /**
     * 设置更新人
     * @param user update user
     */
    void setUpdateUser(BaseUser user);

    /**
     * 获取更新人ID
     * @return 更新人ID
     */
    Long getUpdateUserId();
}
