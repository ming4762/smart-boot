package com.smart.file.extensions.qiniu;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhongming4762
 * 2023/3/30 18:14
 */
@Configuration(proxyBeanMethods = false)
@Import(SmartQiniuImportBeanDefinitionRegistrar.class)
public class SmartFileQiniuAutoConfiguration {
}
