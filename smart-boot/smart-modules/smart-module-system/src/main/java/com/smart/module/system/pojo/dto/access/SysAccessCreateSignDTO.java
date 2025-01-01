package com.smart.module.system.pojo.dto.access;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpMethod;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 生成签名参数
 * @author shizhongming
 * 2024/5/8 14:20
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SysAccessCreateSignDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3638740516566138128L;

    private Long accessId;

    @NotNull(message = "Date不能为空")
    private LocalDateTime date;

    @NotNull(message = "nonce不能为空")
    private String nonce;

    @NotNull(message = "Http method不能为空")
    private HttpMethod httpMethod;

    @NotNull(message = "Content-Type不能为空")
    private String contentType;

    private String tokenPrefix;
}
