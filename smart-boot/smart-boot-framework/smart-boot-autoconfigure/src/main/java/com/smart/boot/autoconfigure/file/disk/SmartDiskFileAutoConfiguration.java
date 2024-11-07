package com.smart.boot.autoconfigure.file.disk;

import com.smart.framework.file.extensions.disk.service.FileStorageDiskServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author shizhongming
 * 2020/11/5 10:52 下午
 */
@Configuration
@Import(SmartDiskFileImportBeanDefinitionRegistrar.class)
@ConditionalOnClass(FileStorageDiskServiceImpl.class)
public class SmartDiskFileAutoConfiguration {

}
