package com.smart.framework.auth.core.config;

import lombok.*;

/**
 * 认证域配置
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-06 16:51
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmartAuthDomainConfig {

    private String loginUrl;
}
