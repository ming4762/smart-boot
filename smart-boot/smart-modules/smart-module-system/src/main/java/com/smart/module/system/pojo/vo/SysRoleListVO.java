package com.smart.module.system.pojo.vo;

import com.smart.framework.crud.model.BaseUser;
import com.smart.framework.crud.model.CreateUpdateUserSetter;
import com.smart.module.system.model.SysRolePO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2021/7/6 15:38
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class SysRoleListVO extends SysRolePO implements CreateUpdateUserSetter {

    @Serial
    private static final long serialVersionUID = -1832277819524356352L;
    private BaseUser createUser;

    private BaseUser updateUser;
}
