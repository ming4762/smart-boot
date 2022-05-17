package com.smart.system.pojo.vo;

import com.smart.crud.model.BaseUser;
import com.smart.crud.model.CreateUserSetter;
import com.smart.system.model.SysLogPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ShiZhongMing
 * 2022/1/20 15:15
 * @since 1.0
 */
@ToString
@Getter
@Setter
public class SysLogListVO extends SysLogPO implements CreateUserSetter {

    private static final long serialVersionUID = 8493022723816142778L;
    private BaseUser createUser;
}
