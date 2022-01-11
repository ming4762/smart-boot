package com.smart.system.pojo.vo;

import com.smart.crud.model.BaseUser;
import com.smart.crud.model.CreateUpdateUserSetter;
import com.smart.system.model.SysUserGroupPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ShiZhongMing
 * 2021/7/6 16:03
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class SysUserGroupListVO extends SysUserGroupPO implements CreateUpdateUserSetter {

    private static final long serialVersionUID = -9153208237080130517L;
    private BaseUser createUser;

    private BaseUser updateUser;
}
