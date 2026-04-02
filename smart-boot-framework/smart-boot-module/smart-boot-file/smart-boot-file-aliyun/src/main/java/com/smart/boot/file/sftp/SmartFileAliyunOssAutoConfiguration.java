package com.smart.boot.file.sftp;

import com.smart.framework.file.extensions.aliyun.oss.AliyunOssService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhongming4762
 * 2022/12/31 21:21
 */
@Configuration
@Import(SmartAliyunOssImportBeanDefinitionRegistrar.class)
@ConditionalOnClass(AliyunOssService.class)
public class SmartFileAliyunOssAutoConfiguration {

}
