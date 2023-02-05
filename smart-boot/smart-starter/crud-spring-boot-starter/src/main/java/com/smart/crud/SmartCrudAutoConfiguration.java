package com.smart.crud;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.smart.crud.mybatis.plus.ShortenIdGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author zhongming4762
 * 2022/12/17 8:04
 */
@org.springframework.context.annotation.Configuration
public class SmartCrudAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(IdentifierGenerator.class)
    public ShortenIdGenerator shortenIdGenerator() {
        return new ShortenIdGenerator();
    }
}
