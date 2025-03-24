package com.smart.module.api.auth.dto;

import lombok.*;

import java.io.Serializable;
import java.time.Duration;

/**
 * @author zhongming4762
 * 2023/3/23
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthCacheDTO implements Serializable {

    private String key;

    private transient Object data;

    private Duration timeout;
}
