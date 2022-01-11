package com.smart.file.extensions.mongo;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author shizhongming
 * 2020/11/14 10:41 下午
 */
@Configuration
@Import(MongoFileImportBeanDefinitionRegistrar.class)
public class SmartMongoFileAutoConfiguration {

}
