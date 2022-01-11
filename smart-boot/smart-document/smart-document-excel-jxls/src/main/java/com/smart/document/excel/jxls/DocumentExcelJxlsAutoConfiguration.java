package com.smart.document.excel.jxls;

import com.smart.document.service.ExcelService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/8/27 14:42
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class DocumentExcelJxlsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ExcelService.class)
    public ExcelService jxlsExcelService() {
        return new JxlsExcelServiceImpl();
    }
}
