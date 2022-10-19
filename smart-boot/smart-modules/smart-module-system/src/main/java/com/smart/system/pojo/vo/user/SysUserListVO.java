package com.smart.system.pojo.vo.user;

import com.smart.crud.model.BaseUser;
import com.smart.crud.model.CreateUpdateUserSetter;
import com.smart.system.model.SysUserAccountPO;
import com.smart.system.model.SysUserPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ShiZhongMing
 * 2021/7/6 16:24
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class SysUserListVO extends SysUserPO implements CreateUpdateUserSetter {

    private static final long serialVersionUID = 561690731916700919L;
    private BaseUser createUser;

    private BaseUser updateUser;

    private SysUserAccountPO userAccount;
}
