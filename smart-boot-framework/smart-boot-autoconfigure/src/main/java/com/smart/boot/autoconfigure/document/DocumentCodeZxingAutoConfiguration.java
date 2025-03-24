package com.smart.boot.autoconfigure.document;

import com.smart.framework.document.code.service.ZxingCodeServiceImpl;
import com.smart.framework.document.service.CodeService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/8/24 15:44
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ZxingCodeServiceImpl.class)
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
