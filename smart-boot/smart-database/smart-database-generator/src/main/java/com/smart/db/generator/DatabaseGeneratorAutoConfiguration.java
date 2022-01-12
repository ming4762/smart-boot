package com.smart.db.generator;

import com.smart.db.generator.engine.FreemarkerTemplateEngine;
import freemarker.cache.ByteArrayTemplateLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/4/25 16:41
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan
public class DatabaseGeneratorAutoConfiguration {

    /**
     * 创建模板引擎
     * TODO: 使用配置指定加载的引擎
     * @param byteArrayTemplateLoader 模板加载器
     * @param configuration freemarker配置
     * @return FreemarkerTemplateEngine
     */
    @Bean
    public FreemarkerTemplateEngine freemarkerTemplateEngine(ByteArrayTemplateLoader byteArrayTemplateLoader, freemarker.template.Configuration configuration) {
        return new FreemarkerTemplateEngine(byteArrayTemplateLoader, configuration);
    }
}
