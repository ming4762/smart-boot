package com.smart.document.converter.jod;

import com.smart.document.service.DocumentConverterService;
import org.jodconverter.core.DocumentConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/8/27 13:15
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class DocumentConvertJodAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DocumentConverterService.class)
    public DocumentConverterService documentConverterServiceJodImpl(DocumentConverter documentConverter) {
        return new DocumentConverterServiceJodImpl(documentConverter);
    }
}
