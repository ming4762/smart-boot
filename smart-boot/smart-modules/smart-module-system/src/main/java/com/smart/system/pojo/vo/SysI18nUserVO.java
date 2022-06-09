package com.smart.system.pojo.vo;

import com.smart.crud.model.BaseUser;
import com.smart.crud.model.CreateUpdateUserSetter;
import com.smart.system.model.SysI18nPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2021/11/12
 * @since 1.0.7
 */
@Getter
@Setter
@ToString
public class SysI18nUserVO extends SysI18nPO implements CreateUpdateUserSetter {

    @Serial
    private static final long serialVersionUID = 8874393211738668467L;
    private BaseUser createUser;

    private BaseUser updateUser;
}
