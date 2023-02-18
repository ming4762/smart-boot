package com.smart.file.manager;

import com.smart.file.core.service.FileService;
import com.smart.file.manager.service.SmartFileStorageService;
import com.smart.file.manager.service.SysFileService;
import com.smart.file.manager.service.impl.DefaultFileServiceImpl;
import com.smart.file.manager.service.impl.SysFileServiceImpl;
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

    /**
     * 创建文件服务实体类
     * @return 文件服务实体类
     */
    @Bean
    @ConditionalOnMissingBean(SysFileService.class)
    public SysFileService sysFileService() {
        return new SysFileServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(FileService.class)
    public FileService defaultFileServiceImpl(SmartFileStorageService smartFileStorageService, SysFileService sysFileService) {
        return new DefaultFileServiceImpl(smartFileStorageService, sysFileService);
    }
}
