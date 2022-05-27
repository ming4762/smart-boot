package com.smart.system.pojo.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
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
@Schema(title = "用户离线参数")
public class OfflineDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 16332489185546505L;

    @Schema(title = "用户名")
    private String username;

    @Schema(title = "token")
    private String token;
}
