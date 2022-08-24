package com.smart.file.manager;

import com.smart.file.core.SmartFileProperties;
import com.smart.file.manager.service.FileHandler;
import com.smart.file.manager.service.SysFileService;
import com.smart.file.manager.service.impl.DefaultFileServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author jacksons
 * 2020/1/27 12:37 下午
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(SmartFileProperties.class)
public class ModuleFileAutoConfiguration {

    /**
     * 创建文件服务实体类
     * @return 文件服务实体类
     */
    @Bean
    @ConditionalOnMissingBean(SysFileService.class)
    public SysFileService sysFileService() {
        return new DefaultFileServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(FileHandler.class)
    public FileHandler fileHandler(SmartFileProperties properties, SysFileService sysFileService) {
        return new FileHandler(properties, sysFileService);
    }
}
