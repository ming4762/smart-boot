package com.smart.module.code.pojo.vo.connection;

import com.smart.framework.crud.model.BaseUser;
import com.smart.framework.crud.model.CreateUpdateUserSetter;
import com.smart.module.code.model.DbConnectionPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2021/6/25 15:22
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class DbConnectionResultVO extends DbConnectionPO implements CreateUpdateUserSetter {
    @Serial
    private static final long serialVersionUID = 7075305604905476783L;

    private BaseUser createUser;

    private BaseUser updateUser;
}
