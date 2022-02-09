package com.smart.monitor.server.manager.pojo.vo;

import com.smart.crud.model.BaseUser;
import com.smart.crud.model.CreateUpdateUserSetter;
import com.smart.monitor.server.manager.model.MonitorApplicationPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ShiZhongMing
 * 2022/2/9
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class MonitorApplicationUserVO extends MonitorApplicationPO implements CreateUpdateUserSetter {
    private static final long serialVersionUID = 2106853318556978043L;

    private BaseUser createUser;

    private BaseUser updateUser;
}
