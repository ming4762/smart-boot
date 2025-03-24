package com.smart.boot.autoconfigure.freemarker;

import com.smart.framework.freemarker.engine.FreemarkerTemplateEngine;
import freemarker.cache.ByteArrayTemplateLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * freemarker自动配置
 * @author shizhongming
 * 2024/11/11 10:35
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(FreemarkerTemplateEngine.class)
public class SmartFreemarkerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ByteArrayTemplateLoader byteArrayTemplateLoader() {
        return new ByteArrayTemplateLoader();
    }

    @Bean
    public FreemarkerTemplateEngine freemarkerTemplateEngine(freemarker.template.Configuration configuration, ByteArrayTemplateLoader byteArrayTemplateLoader) {
        return new FreemarkerTemplateEngine(configuration, byteArrayTemplateLoader);
    }
}
