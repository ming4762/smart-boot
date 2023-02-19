package com.smart.file.manager;

import com.smart.file.core.service.FileService;
import com.smart.file.manager.service.SmartFileStorageService;
import com.smart.file.manager.service.SmartFileService;
import com.smart.file.manager.service.impl.DefaultFileServiceImpl;
import com.smart.file.manager.service.impl.SmartFileServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author jacksons
 * 2020/1/27 12:37 下午
 */
@Configuration
@ComponentScan
public class ModuleFileAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(FileService.class)
    public FileService defaultFileServiceImpl(SmartFileStorageService smartFileStorageService, SmartFileService sysFileService) {
        return new DefaultFileServiceImpl(smartFileStorageService, sysFileService);
    }
}
