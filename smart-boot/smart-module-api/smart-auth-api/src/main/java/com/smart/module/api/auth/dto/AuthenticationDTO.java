package com.smart.module.api.auth.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author zhongming4762
 * 2023/3/10
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationDTO implements Serializable {

    private String url;

    private String httpMethod;
}
