package com.smart.module.code;

import com.smart.module.code.config.FreemarkerProperties;
import freemarker.cache.ByteArrayTemplateLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author shizhongming
 * 2020/7/2 8:27 下午
 */
@Configuration
@EnableConfigurationProperties(FreemarkerProperties.class)
public class SmartFreemarkerAutoConfiguration {

    /**
     * 创建freemarker配置
     * @param properties 配置参数
     * @return freemarker配置
     */
//    @Bean
//    public freemarker.template.Configuration configuration(FreemarkerProperties properties, ApplicationContext applicationContext) {
//        final FreemarkerProperties.Config com.smart.framework.tool.code.config = properties.getConfig();
//        // 创建freemarker配置
//        final freemarker.template.Configuration configuration = new freemarker.template.Configuration(com.smart.framework.tool.code.config.getVersion());
//        configuration.setDefaultEncoding(com.smart.framework.tool.code.config.getEncoding().name());
//        // 设置加载器
//        Collection<TemplateLoader> loaders = applicationContext.getBeansOfType(TemplateLoader.class).values();
//        configuration.setTemplateLoader(new MultiTemplateLoader(loaders.toArray(new TemplateLoader[0])));
//        return configuration;
//    }

    /**
     * 创建ByteArrayTemplateLoader
     * @return ByteArrayTemplateLoader
     */
    @Bean
    @Order(Integer.MIN_VALUE)
    @ConditionalOnMissingBean(ByteArrayTemplateLoader.class)
    public ByteArrayTemplateLoader byteArrayTemplateLoader() {
        return new ByteArrayTemplateLoader();
    }

}
