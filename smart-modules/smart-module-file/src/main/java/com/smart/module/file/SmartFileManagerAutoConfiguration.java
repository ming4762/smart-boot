package com.smart.module.file;

import com.smart.framework.file.core.service.FileService;
import com.smart.module.file.service.SmartFileService;
import com.smart.module.file.service.SmartFileStorageService;
import com.smart.module.file.service.impl.DefaultFileServiceImpl;
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
