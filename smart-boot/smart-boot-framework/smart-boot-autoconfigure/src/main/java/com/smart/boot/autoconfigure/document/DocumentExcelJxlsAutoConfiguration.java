package com.smart.boot.autoconfigure.document;

import com.smart.framework.document.excel.jxls.JxlsExcelServiceImpl;
import com.smart.framework.document.service.ExcelService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/8/27 14:42
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(JxlsExcelServiceImpl.class)
public class DocumentExcelJxlsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ExcelService.class)
    public ExcelService jxlsExcelService() {
        return new JxlsExcelServiceImpl();
    }
}
