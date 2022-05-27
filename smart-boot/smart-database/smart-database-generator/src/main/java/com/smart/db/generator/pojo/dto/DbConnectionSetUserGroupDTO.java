package com.smart.db.generator.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 设置数据库连接对应的用户组信息
 * @author ShiZhongMing
 * 2021/6/24 9:53
 * @since 1.0
 */
@ToString
@Getter
@Setter
public class DbConnectionSetUserGroupDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -9190615310611428862L;
    @NotNull(message = "数据库连接ID不能为空")
    private Long connectionId;

    private List<Long> userGroupIdList;
}
