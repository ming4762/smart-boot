package com.smart.system;

import com.smart.file.core.service.FileService;
import com.smart.system.auth.AuthEventLogHandler;
import com.smart.system.service.SysLogService;
import com.smart.system.service.SysUserAccountService;
import com.smart.system.service.SysUserService;
import com.smart.system.service.file.SmartFileService;
import com.smart.system.service.file.SmartFileStorageService;
import com.smart.system.service.file.impl.DefaultFileServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 系统模块自动配置类
 * @author jackson
 * 2020/1/22 9:44 上午
 */
@Configuration
@AutoConfigureBefore(JacksonAutoConfiguration.class)
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

    @Bean
    @ConditionalOnMissingBean(FileService.class)
    public FileService defaultFileServiceImpl(SmartFileStorageService smartFileStorageService, SmartFileService sysFileService) {
        return new DefaultFileServiceImpl(smartFileStorageService, sysFileService);
    }
}
