package com.smart.boot.autoconfigure.file.ftp;

import com.smart.framework.file.extensions.ftp.service.FileStorageFtpServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * FTP自动配置类
 * @author shizhongming
 * 2023/3/21 17:32
 * @since 3.0.0
 */
@Configuration
@Import(FileFtpImportBeanDefinitionRegistrar.class)
@ConditionalOnClass(FileStorageFtpServiceImpl.class)
public class SmartFtpFileAutoConfiguration {
}
