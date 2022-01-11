package com.smart.crud.model;

/**
 * @author ShiZhongMing
 * 2021/6/25 15:32
 * @since 1.0
 */
public interface CreateUserSetter extends UserSetter {

    /**
     * 设置创建人
     * @param user 创建人
     */
    void setCreateUser(BaseUser user);

    /**
     * 获取创建用户的ID
     * @return 用户ID
     */
    Long getCreateUserId();
}
