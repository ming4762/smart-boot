package com.smart.module.monitor.server.pojo.vo;

import com.smart.framework.crud.model.BaseUser;
import com.smart.framework.crud.model.CreateUpdateUserSetter;
import com.smart.module.monitor.server.model.MonitorApplicationPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2022/2/9
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class MonitorApplicationUserVO extends MonitorApplicationPO implements CreateUpdateUserSetter {
    @Serial
    private static final long serialVersionUID = 2106853318556978043L;

    private BaseUser createUser;

    private BaseUser updateUser;
}
