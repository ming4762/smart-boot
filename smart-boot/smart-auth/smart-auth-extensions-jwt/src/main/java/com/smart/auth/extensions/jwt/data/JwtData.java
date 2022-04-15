package com.smart.auth.extensions.jwt.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * JWT数据
 * @author ShiZhongMing
 * 2022/4/12
 * @since 2.0.0
 */
@Getter
@AllArgsConstructor
@Setter
public class JwtData implements Serializable {

    private static final long serialVersionUID = -1673739084242479566L;

    private String jwt;

    private LocalDateTime createTime;

    private LocalDateTime refreshTime;

    private Duration timeout;
}
