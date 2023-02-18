package com.smart.file.extensions.disk;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author shizhongming
 * 2020/11/5 10:52 下午
 */
@Configuration
@Import(SmartDiskFileImportBeanDefinitionRegistrar.class)
public class SmartDiskFileAutoConfiguration {

}
