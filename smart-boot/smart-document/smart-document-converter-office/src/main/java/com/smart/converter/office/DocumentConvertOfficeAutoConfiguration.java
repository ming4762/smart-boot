package com.smart.converter.office;

import com.smart.converter.office.service.OfficeDocumentConverterServiceImpl;
import com.smart.document.service.DocumentConverterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/8/27 13:15
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class DocumentConvertOfficeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DocumentConverterService.class)
    public DocumentConverterService officeDocumentConverterServiceImpl() {
        return new OfficeDocumentConverterServiceImpl();
    }
}
