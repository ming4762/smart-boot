package com.smart.db.generator.pojo.vo.connection;

import com.smart.crud.model.BaseUser;
import com.smart.crud.model.CreateUpdateUserSetter;
import com.smart.db.generator.model.DbConnectionPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ShiZhongMing
 * 2021/6/25 15:22
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class DbConnectionResultVO extends DbConnectionPO implements CreateUpdateUserSetter {
    private static final long serialVersionUID = 7075305604905476783L;

    private BaseUser createUser;

    private BaseUser updateUser;
}
