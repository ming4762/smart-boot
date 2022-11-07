package com.smart.system.pojo.vo;

import com.smart.crud.model.BaseUser;
import com.smart.crud.model.CreateUpdateUserSetter;
import com.smart.system.model.SysDeptPO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ShiZhongMing
 * 2022/10/13
 * @since 1.0
 */
@Getter
@Setter
public class SysDeptListVo extends SysDeptPO implements CreateUpdateUserSetter {

    private static final long serialVersionUID = -2370358811334567841L;

    private BaseUser createUser;

    private BaseUser updateUser;

    private SysDeptPO parentDept;
}
