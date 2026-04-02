package com.smart.boot.file.sftp;

import com.smart.framework.file.extensions.sftp.provider.FtpChannelProvider;
import com.smart.framework.file.extensions.sftp.service.FileStorageNfsServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ShiZhongMing
 * 2020/12/7 14:54
 * @since 1.0
 */
@Configuration
@Import(FileSftpJschImportBeanDefinitionRegistrar.class)
@ConditionalOnClass(FileStorageNfsServiceImpl.class)
public class SmartFileSftpJschAutoConfiguration {


    @Bean
    public FtpChannelProvider ftpChannelProvider() {
        return new FtpChannelProvider();
    }
}
