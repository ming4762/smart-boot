package com.smart.monitor.server.manager.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 设置应用所在用户组DTO
 * @author ShiZhongMing
 * 2022/2/9
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class MonitorApplicationSetUserGroupDTO implements Serializable {

    private static final long serialVersionUID = -7350636025389366842L;

    @NotNull(message = "应用ID不能为空")
    private Long applicationId;

    @NotNull(message = "用户组ID不能为空")
    private List<Long> groupIdList;
}
