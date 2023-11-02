package com.smart.crud;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.smart.crud.plus.injector.EnhanceSqlInjector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2023/10/24 20:16
 * @since 1.0.0
 */
@Configuration
public class MybatisPlusEnhanceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ISqlInjector enhanceSqlInjector() {
        return new EnhanceSqlInjector();
    }
}
