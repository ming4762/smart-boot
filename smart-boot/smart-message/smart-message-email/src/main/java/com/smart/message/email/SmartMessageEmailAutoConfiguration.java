package com.smart.message.email;

import com.smart.message.email.sender.SmartEmailSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2024/10/31 9:12
 * @since 3.0.0
 */
@Configuration(proxyBeanMethods = false)
public class SmartMessageEmailAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SmartEmailSender smartEmailSender() {
        return new SmartEmailSender();
    }
}
