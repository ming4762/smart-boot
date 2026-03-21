package com.smart.boot.autoconfigure.file.sftp;

import com.smart.framework.file.extensions.sftp.sshj.service.FileStorageSftpSshjServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ShiZhongMing
 * 2020/12/7 14:54
 * @since 1.0
 */
@Configuration
@Import(FileSftpSshjImportBeanDefinitionRegistrar.class)
@ConditionalOnClass(FileStorageSftpSshjServiceImpl.class)
public class SmartSftpSshjFileAutoConfiguration {

}
