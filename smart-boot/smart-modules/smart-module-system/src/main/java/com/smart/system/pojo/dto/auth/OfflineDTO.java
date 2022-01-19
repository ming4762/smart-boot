package com.smart.system.pojo.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ShiZhongMing
 * 2022/1/18
 * @since 2.0.0
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@ApiModel("用户离线参数")
public class OfflineDTO implements Serializable {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("token")
    private String token;
}
