package com.smart.file.extensions.nfs;

import com.smart.commons.file.SmartFileProperties;
import com.smart.file.extensions.nfs.provider.FtpChannelProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ShiZhongMing
 * 2020/12/7 14:54
 * @since 1.0
 */
@Configuration
@Import(FileNfsImportBeanDefinitionRegistrar.class)
public class SmartNfsFileAutoConfiguration {


    @Bean
    public FtpChannelProvider ftpChannelProvider(SmartFileProperties properties) {
        return new FtpChannelProvider(properties.getNfs());
    }
}
