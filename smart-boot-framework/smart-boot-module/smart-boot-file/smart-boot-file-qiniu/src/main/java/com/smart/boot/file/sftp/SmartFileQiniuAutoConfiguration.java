package com.smart.boot.file.sftp;

import com.smart.framework.file.extensions.qiniu.QiniuService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhongming4762
 * 2023/3/30 18:14
 */
@Configuration(proxyBeanMethods = false)
@Import(SmartQiniuImportBeanDefinitionRegistrar.class)
@ConditionalOnClass(QiniuService.class)
public class SmartFileQiniuAutoConfiguration {
}
