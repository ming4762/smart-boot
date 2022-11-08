package com.smart.system.pojo.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 查询在线用户DTO
 * @author ShiZhongMing
 * 2022/1/18
 * @since 2.0.0
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Tag(name = "查询在线用户参数")
public class OnlineUserQueryDTO implements Serializable {

    private static final long serialVersionUID = 7731927759841756765L;
    @Schema(name = "用户名")
    private String username;
}
