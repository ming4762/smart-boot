package com.smart.file.extensions.sftp;

import com.smart.file.core.SmartFileProperties;
import com.smart.file.extensions.sftp.provider.FtpChannelProvider;
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
        return new FtpChannelProvider(properties.getSftp());
    }
}
