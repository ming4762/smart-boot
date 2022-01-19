package com.smart.system.pojo.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "查询在线用户参数")
public class OnlineUserQueryDTO implements Serializable {

    @ApiModelProperty("用户名")
    private String username;
}
