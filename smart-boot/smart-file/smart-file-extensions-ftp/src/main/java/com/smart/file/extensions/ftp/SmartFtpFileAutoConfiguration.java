package com.smart.file.extensions.ftp;

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
public class SmartFtpFileAutoConfiguration {
}
