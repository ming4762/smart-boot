package com.smart.module.system.pojo.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 * @author shizhongming
 * 2025/9/9 19:02
 * @since 5.0.0
 */
@Getter
@Setter
public class SmartAuthAccessTestDTO implements Serializable {

    private String queryParameter;

    private String jsonParameter;

    @NotNull(message = "accessId不能为空")
    private Long accessId;

    @NotNull(message = "tokenPrefix不能为空")
    private String tokenPrefix;

    @NotNull(message = "nonce不能为空")
    private String nonce;

    private String contentType;

}
