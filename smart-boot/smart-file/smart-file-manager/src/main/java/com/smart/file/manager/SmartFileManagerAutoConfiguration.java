package com.smart.file.manager;

import com.smart.file.core.service.FileService;
import com.smart.file.manager.service.SmartFileService;
import com.smart.file.manager.service.SmartFileStorageService;
import com.smart.file.manager.service.impl.DefaultFileServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/3/21
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan
public class SmartFileManagerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(FileService.class)
    public FileService defaultFileServiceImpl(SmartFileStorageService smartFileStorageService, SmartFileService sysFileService) {
        return new DefaultFileServiceImpl(smartFileStorageService, sysFileService);
    }
}
