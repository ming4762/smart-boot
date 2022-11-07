package com.smart.system.pojo.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用户更新带有数据权限
 * @author ShiZhongMing
 * 2022/10/19
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class UserSaveWithDataScopeDTO implements Serializable {
    private static final long serialVersionUID = -6750189929943910845L;

    private UserUpdateDTO user;

    private UserDataScopeUpdateDTO dataScope;
}
