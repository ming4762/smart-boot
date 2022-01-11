package com.smart.document.code;

import com.smart.document.code.service.ZxingCodeServiceImpl;
import com.smart.document.service.CodeService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/8/24 15:44
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class DocumentCodeZxingAutoConfiguration {

    /**
     * 创建 条形码/二维码生成服务
     * @return CodeService
     */
    @Bean
    @ConditionalOnMissingBean(CodeService.class)
    public CodeService codeService() {
        return new ZxingCodeServiceImpl();
    }
}
