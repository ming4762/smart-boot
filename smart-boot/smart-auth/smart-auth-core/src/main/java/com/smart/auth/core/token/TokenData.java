package com.smart.auth.core.token;

import com.smart.auth.core.userdetails.RestUserDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * token信息
 * @author ShiZhongMing
 * 2022/4/12
 * @since 2.0.0
 */
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class TokenData implements Serializable {

    private static final long serialVersionUID = -1673739084242479566L;

    private String token;

    private LocalDateTime createTime;

    private LocalDateTime refreshTime;

    private Duration timeout;

    private RestUserDetails user;
}
