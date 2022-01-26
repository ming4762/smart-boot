package com.smart.system;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.system.auth.AuthEventLockedHandler;
import com.smart.system.auth.AuthEventLogHandler;
import com.smart.system.service.SysUserAccountService;
import com.smart.system.service.SysLogService;
import com.smart.system.service.SysUserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 系统模块自动配置类
 * @author jackson
 * 2020/1/22 9:44 上午
 */
@Configuration
@ComponentScan
public class SmartSystemAutoConfiguration {

    /**
     * 创建认证日志处理器
     * @param sysLogService sysLogService
     * @return AuthEventLogHandler
     */
    @Bean
    @ConditionalOnMissingBean(AuthEventLogHandler.class)
    public AuthEventLogHandler authEventLogHandler(SysLogService sysLogService) {
        return new AuthEventLogHandler(sysLogService);
    }

    /**
     * 认证失败用户锁定处理器
     * @return AuthEventLockedHandler
     */
    @Bean
    @ConditionalOnMissingBean(AuthEventLockedHandler.class)
    public AuthEventLockedHandler authEventLockedHandler(AuthProperties properties, SysUserService sysUserService, SysUserAccountService sysAuthUserService) {
        return new AuthEventLockedHandler(properties, sysUserService, sysAuthUserService);
    }
}
